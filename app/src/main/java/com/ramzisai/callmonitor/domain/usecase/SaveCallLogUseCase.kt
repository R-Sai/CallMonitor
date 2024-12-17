package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveCallLogUseCase @Inject constructor(
    private val repository: CallLogRepository
) : UseCase<SaveCallLogUseCase.Params, Long> {
    override suspend fun invoke(parameter: Params): Flow<Long> {
        return repository.putCallLog(
            CallLogEntry(
                name = parameter.name,
                number = parameter.number,
                timestamp = parameter.timestamp,
                isOngoing = true
            )
        )
    }

    class Params(
        val name: String?,
        val number: String?,
        val timestamp: Long
    )
}