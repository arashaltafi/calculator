package com.arash.altafi.calculator.data.source.local.db.dao

import androidx.room.*
import com.arash.altafi.calculator.data.source.local.db.model.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertHistoryEntity(historyEntity: HistoryEntity)

    @Query("SELECT * FROM History")
    fun getHistoryEntitiesStream(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM History")
    suspend fun deleteAllHistoryEntities()
}