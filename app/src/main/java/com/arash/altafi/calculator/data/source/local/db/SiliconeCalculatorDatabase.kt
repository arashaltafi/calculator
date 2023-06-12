package com.arash.altafi.calculator.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arash.altafi.calculator.data.source.local.db.dao.HistoryDao
import com.arash.altafi.calculator.data.source.local.db.model.HistoryEntity
import com.arash.altafi.calculator.util.DateConverter

@Database(entities = [HistoryEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class SiliconeCalculatorDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
