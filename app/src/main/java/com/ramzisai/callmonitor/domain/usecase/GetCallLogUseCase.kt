package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCallLogUseCase @Inject constructor(
    private val repository: CallLogRepository
): UseCase<Unit, List<CallLogEntry>> {
    override suspend fun invoke(parameter: Unit): Flow<List<CallLogEntry>> {
        return repository.getCallLog().map { log ->
            log.sortedByDescending { it.timestamp }
        }
    }
}