package com.arash.altafi.calculator.history

import androidx.compose.runtime.Immutable
import com.arash.altafi.calculator.data.model.History

@Immutable
data class HistoryUiState(
    val historyItems: List<History> = emptyList()
)