package com.ramzisai.callmonitor.di

import android.content.Context
import androidx.room.Room
import com.ramzisai.callmonitor.data.dao.CallLogDao
import com.ramzisai.callmonitor.data.db.AppDatabase
import com.ramzisai.callmonitor.data.mapper.CallLogMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCallLogDao(database: AppDatabase): CallLogDao {
        return database.callLogDao()
    }

    @Provides
    @Singleton
    fun provideCallLogMapper(): CallLogMapper {
        return CallLogMapper()
    }
}