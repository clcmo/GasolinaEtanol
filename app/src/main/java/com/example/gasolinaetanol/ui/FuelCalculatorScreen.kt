package com.example.gasolinaetanol.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gasolinaetanol.data.FuelRecommendation
import com.example.gasolinaetanol.viewmodel.FuelCalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelCalculatorScreen(
    viewModel: FuelCalculatorViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Simulador Gasolina x Etanol", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Informe os Preços no Posto",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = uiState.ethanolInput,
                onValueChange = { viewModel.onEthanolChanged(it) },
                label = { Text("Preço do Etanol (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de texto para o preço do Etanol em reais" },
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.gasolineInput,
                onValueChange = { viewModel.onGasolineChanged(it) },
                label = { Text("Preço da Gasolina (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Campo de texto para o preço da Gasolina em reais" },
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Usar consumo real do veículo (km/l)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = uiState.useCustomConsumption,
                    onCheckedChange = { viewModel.onToggleCustomConsumption(it) },
                    modifier = Modifier.semantics {
                        contentDescription = "Ativar ou desativar consumo personalizado em quilômetros por litro"
                    }
                )
            }

            if (uiState.useCustomConsumption) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.ethanolKmlInput,
                        onValueChange = { viewModel.onEthanolKmlChanged(it) },
                        label = { Text("Etanol (km/l)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = uiState.gasolineKmlInput,
                        onValueChange = { viewModel.onGasolineKmlChanged(it) },
                        label = { Text("Gasolina (km/l)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ResultCard(uiState = uiState)
        }
    }
}

@Composable
fun ResultCard(uiState: com.example.gasolinaetanol.viewmodel.CalculatorUiState) {
    val (bgColor, textColor, title, desc) = when (uiState.recommendation) {
        FuelRecommendation.ETHANOL -> Quadruple(
            Color(0xFF2E7D32),
            Color.White,
            "ABASTEÇA COM ETANOL 🌿",
            "O Etanol está custando ${String.format("%.1f", uiState.ratioPercentage)}% do preço da gasolina."
        )
        FuelRecommendation.GASOLINE -> Quadruple(
            Color(0xFF1565C0),
            Color.White,
            "ABASTEÇA COM GASOLINA ⛽",
            "O Etanol está custando ${String.format("%.1f", uiState.ratioPercentage)}% do preço da gasolina."
        )
        FuelRecommendation.EQUAL -> Quadruple(
            Color(0xFFE65100),
            Color.White,
            "TANTO FAZ!",
            "Ambos os combustíveis entregam o mesmo custo-benefício."
        )
        FuelRecommendation.INVALID -> Quadruple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "INFORME OS VALORES",
            "Preencha os valores para calcular a melhor opção."
        )
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = uiState.accessibilityAnnounceText
            }
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = desc,
                fontSize = 14.sp,
                color = textColor
            )

            if (uiState.useCustomConsumption && uiState.recommendation != FuelRecommendation.INVALID) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = textColor.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Custo/km Etanol: R$ ${String.format("%.2f", uiState.ethanolCostPerKm)}",
                    color = textColor,
                    fontSize = 13.sp
                )
                Text(
                    text = "Custo/km Gasolina: R$ ${String.format("%.2f", uiState.gasolineCostPerKm)}",
                    color = textColor,
                    fontSize = 13.sp
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
