package com.arash.altafi.calculator.data.repository

import com.arash.altafi.calculator.data.model.Calculation
import com.arash.altafi.calculator.data.model.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    val historyItemsStream: Flow<List<History>>
    suspend fun clearAllHistory()
    suspend fun saveCalculation(calculation: Calculation)
}