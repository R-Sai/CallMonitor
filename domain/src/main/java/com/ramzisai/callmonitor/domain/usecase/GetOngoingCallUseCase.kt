package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetOngoingCallUseCase @Inject constructor(
    private val repository: CallLogRepository
) : UseCase<Unit, CallLogDomainModel?> {
    override suspend fun invoke(parameter: Unit): Flow<CallLogDomainModel?> {
        return repository.getCallLog().take(1).map { log ->
            log.sortedByDescending { it.timestamp }.firstOrNull { it.isOngoing }
        }
    }
}