package com.example.gasolinaetanol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.gasolinaetanol.ui.FuelCalculatorScreen
import com.example.gasolinaetanol.viewmodel.FuelCalculatorViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: FuelCalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FuelCalculatorScreen(viewModel = viewModel)
        }
    }
}