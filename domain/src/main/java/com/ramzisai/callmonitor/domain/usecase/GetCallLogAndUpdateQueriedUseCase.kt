package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class GetCallLogAndUpdateQueriedUseCase @Inject constructor(
    private val repository: CallLogRepository
) : UseCase<Unit, List<CallLogDomainModel>> {
    override suspend fun invoke(parameter: Unit): Flow<List<CallLogDomainModel>> {
        return repository.getCallLog().take(1).onEach { callLogs ->
            val updatedLogs = callLogs.map { callLogEntry ->
                callLogEntry.copy(timesQueried = callLogEntry.timesQueried + 1)
            }
            repository.updateCallLogs(updatedLogs)
        }
    }
}