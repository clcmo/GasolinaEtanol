package com.example.gasolinaetanol

import com.example.gasolinaetanol.data.FuelCalculatorRepository
import com.example.gasolinaetanol.data.FuelPrices
import com.example.gasolinaetanol.data.FuelRecommendation
import com.example.gasolinaetanol.viewmodel.FuelCalculatorViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FuelCalculatorTest {

    private lateinit var repository: FuelCalculatorRepository
    private lateinit var viewModel: FuelCalculatorViewModel

    @Before
    fun setup() {
        repository = FuelCalculatorRepository()
        viewModel = FuelCalculatorViewModel(repository)
    }

    @Test
    fun repository_calculatesEthanolWhenRatioUnder70Percent() {
        val prices = FuelPrices(ethanolPrice = 3.50, gasolinePrice = 5.50)
        val result = repository.calculateBestOption(prices)
        assertEquals(FuelRecommendation.ETHANOL, result)
    }

    @Test
    fun repository_calculatesGasolineWhenRatioOver70Percent() {
        val prices = FuelPrices(ethanolPrice = 4.20, gasolinePrice = 5.50)
        val result = repository.calculateBestOption(prices)
        assertEquals(FuelRecommendation.GASOLINE, result)
    }

    @Test
    fun viewModel_updatesUiStateAndCalculatesCostPerKm() {
        viewModel.onEthanolChanged("3.50")
        viewModel.onGasolineChanged("5.00")
        
        assertEquals(FuelRecommendation.EQUAL, viewModel.uiState.value.recommendation)
        assertEquals(70.0, viewModel.uiState.value.ratioPercentage, 0.01)

        viewModel.onToggleCustomConsumption(true)
        assertEquals(0.50, viewModel.uiState.value.ethanolCostPerKm, 0.01)
        assertEquals(0.50, viewModel.uiState.value.gasolineCostPerKm, 0.01)
    }
}
