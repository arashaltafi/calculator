package com.arash.altafi.calculator.util.calculator

import androidx.compose.runtime.Immutable
import com.arash.altafi.calculator.data.model.Calculation

@Immutable
data class CalculatorUiState(
    val calculation: Calculation = Calculation()
)
