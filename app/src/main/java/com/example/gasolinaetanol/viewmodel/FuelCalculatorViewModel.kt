package com.example.gasolinaetanol.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gasolinaetanol.data.FuelCalculatorRepository
import com.example.gasolinaetanol.data.FuelPrices
import com.example.gasolinaetanol.data.FuelRecommendation
import com.example.gasolinaetanol.data.VehicleConsumption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CalculatorUiState(
    val ethanolInput: String = "",
    val gasolineInput: String = "",
    val useCustomConsumption: Boolean = false,
    val ethanolKmlInput: String = "7.0",
    val gasolineKmlInput: String = "10.0",
    val recommendation: FuelRecommendation = FuelRecommendation.INVALID,
    val ratioPercentage: Double = 0.0,
    val ethanolCostPerKm: Double = 0.0,
    val gasolineCostPerKm: Double = 0.0,
    val accessibilityAnnounceText: String = ""
)

class FuelCalculatorViewModel(
    private val repository: FuelCalculatorRepository = FuelCalculatorRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onEthanolChanged(value: String) {
        _uiState.update { it.copy(ethanolInput = value) }
        recalculate()
    }

    fun onGasolineChanged(value: String) {
        _uiState.update { it.copy(gasolineInput = value) }
        recalculate()
    }

    fun onToggleCustomConsumption(enabled: Boolean) {
        _uiState.update { it.copy(useCustomConsumption = enabled) }
        recalculate()
    }

    fun onEthanolKmlChanged(value: String) {
        _uiState.update { it.copy(ethanolKmlInput = value) }
        recalculate()
    }

    fun onGasolineKmlChanged(value: String) {
        _uiState.update { it.copy(gasolineKmlInput = value) }
        recalculate()
    }

    private fun recalculate() {
        val ethanol = _uiState.value.ethanolInput.toDoubleOrNull()
        val gasoline = _uiState.value.gasolineInput.toDoubleOrNull()

        if (ethanol == null || gasoline == null || gasoline == 0.0 || ethanol == 0.0) {
            _uiState.update { 
                it.copy(
                    recommendation = FuelRecommendation.INVALID,
                    ratioPercentage = 0.0,
                    ethanolCostPerKm = 0.0,
                    gasolineCostPerKm = 0.0,
                    accessibilityAnnounceText = "Aguardando preenchimento dos valores."
                ) 
            }
            return
        }

        val efficiencyRatio = if (_uiState.value.useCustomConsumption) {
            val ethKml = _uiState.value.ethanolKmlInput.toDoubleOrNull() ?: 7.0
            val gasKml = _uiState.value.gasolineKmlInput.toDoubleOrNull() ?: 10.0
            VehicleConsumption(ethKml, gasKml).efficiencyRatio
        } else {
            0.70
        }

        val prices = FuelPrices(ethanol, gasoline, efficiencyRatio)
        val result = repository.calculateBestOption(prices)
        val ratio = (ethanol / gasoline) * 100

        val ethKml = _uiState.value.ethanolKmlInput.toDoubleOrNull() ?: 7.0
        val gasKml = _uiState.value.gasolineKmlInput.toDoubleOrNull() ?: 10.0
        val ethCostKm = repository.calculateCostPerKm(ethanol, ethKml)
        val gasCostKm = repository.calculateCostPerKm(gasoline, gasKml)

        val announce = when (result) {
            FuelRecommendation.ETHANOL -> "Recomendação: Abasteça com Etanol. Relação de preço é de ${String.format("%.1f", ratio)}%."
            FuelRecommendation.GASOLINE -> "Recomendação: Abasteça com Gasolina. Relação de preço é de ${String.format("%.1f", ratio)}%."
            FuelRecommendation.EQUAL -> "Tanto faz abastecer com Etanol ou Gasolina."
            else -> "Valores inválidos."
        }

        _uiState.update {
            it.copy(
                recommendation = result,
                ratioPercentage = ratio,
                ethanolCostPerKm = ethCostKm,
                gasolineCostPerKm = gasCostKm,
                accessibilityAnnounceText = announce
            )
        }
    }
}
