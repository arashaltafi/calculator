package com.arash.altafi.calculator.data.repository

import com.arash.altafi.calculator.data.model.Calculation
import com.arash.altafi.calculator.data.model.asHistoryEntity
import com.arash.altafi.calculator.data.source.local.db.dao.HistoryDao
import com.arash.altafi.calculator.data.source.local.db.model.HistoryEntity
import com.arash.altafi.calculator.data.source.local.db.model.asHistory
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao,
) : HistoryRepository {

    override val historyItemsStream = historyDao.getHistoryEntitiesStream()
        .map {
            it.map(HistoryEntity::asHistory)
        }

    override suspend fun clearAllHistory() {
        historyDao.deleteAllHistoryEntities()
    }

    override suspend fun saveCalculation(calculation: Calculation) {
        historyDao.insertHistoryEntity(calculation.asHistoryEntity())
    }
}