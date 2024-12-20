package com.ramzisai.callmonitor.di

import com.ramzisai.callmonitor.data.repository.CallLogRepositoryImpl
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCallLogRepository(repositoryImpl: CallLogRepositoryImpl): CallLogRepository
}