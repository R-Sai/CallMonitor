package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveCallLogUseCase @Inject constructor(
    private val repository: CallLogRepository
) : UseCase<Unit, List<CallLogDomainModel>> {
    override suspend fun invoke(parameter: Unit): Flow<List<CallLogDomainModel>> {
        return repository.getCallLog().map { log ->
            log.sortedByDescending { it.timestamp }
        }
    }
}