package com.arash.altafi.calculator.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.arash.altafi.calculator.data.repository.HistoryRepository
import com.arash.altafi.calculator.data.repository.HistoryRepositoryImpl
import com.arash.altafi.calculator.data.source.local.db.SiliconeCalculatorDatabase
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindsHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl,
    ): HistoryRepository

    companion object {

        @[Provides Singleton]
        fun providesRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
            context,
            SiliconeCalculatorDatabase::class.java,
            "silicone_calculator"
        ).build()

        @Provides
        fun providesHistoryDao(siliconeCalculatorDatabase: SiliconeCalculatorDatabase) =
            siliconeCalculatorDatabase.historyDao()

        @Provides
        fun providesDefaultDispatcher() = Dispatchers.Default
    }
}