package com.arash.altafi.calculator.data.source.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arash.altafi.calculator.data.model.Calculation
import com.arash.altafi.calculator.data.model.History
import com.arash.altafi.calculator.util.format
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Entity(tableName = "History")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "epoch_day") val date: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val expression: String,
    val result: String,
)

fun HistoryEntity.asHistory() = History(
    id = id,
    date = date.format(),
    calculation = Calculation(
        expression = expression,
        result = result
    )
)
