package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetOngoingCallUseCase @Inject constructor(
    private val repository: CallLogRepository
) : UseCase<Unit, CallLogEntry?> {
    override suspend fun invoke(parameter: Unit): Flow<CallLogEntry?> {
        return repository.getCallLog().map { log ->
            log.sortedByDescending { it.timestamp }.firstOrNull { it.isOngoing }
        }
    }
}