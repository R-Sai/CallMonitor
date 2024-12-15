package com.ramzisai.callmonitor.di

import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import com.ramzisai.callmonitor.domain.usecase.GetCallLogUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class, ServiceComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetCallLogUseCase(
        repository: CallLogRepository
    ): GetCallLogUseCase {
        return GetCallLogUseCase(repository)
    }
}