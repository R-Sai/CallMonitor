package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import javax.inject.Inject

class UpdateCallLogDurationUseCase @Inject constructor(
    private val repository: CallLogRepository
) : NoResultUseCase<UpdateCallLogDurationUseCase.Params> {
    override suspend fun invoke(parameter: Params) {
        return repository.updateCallLogDuration(
            callId = parameter.id,
            duration = parameter.duration
        )
    }

    class Params(
        val id: Long,
        val duration: Long
    )
}