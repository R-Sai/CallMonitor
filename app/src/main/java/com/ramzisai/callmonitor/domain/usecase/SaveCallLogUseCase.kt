package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import javax.inject.Inject

class SaveCallLogUseCase @Inject constructor(
    private val repository: CallLogRepository
) : NoResultUseCase<SaveCallLogUseCase.Params> {
    override suspend fun invoke(parameter: Params) {
        repository.putCallLog(
            CallLogEntry(
                name = parameter.name,
                number = parameter.number,
                timestamp = parameter.timestamp
            )
        )
    }

    class Params(
        val name: String?,
        val number: String?,
        val timestamp: Long
    )
}