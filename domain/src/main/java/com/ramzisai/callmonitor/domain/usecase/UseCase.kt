package com.ramzisai.callmonitor.domain.usecase

import kotlinx.coroutines.flow.Flow

interface UseCase<in P, out R> {
    suspend operator fun invoke(parameter: P): Flow<R>
}
