package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import javax.inject.Inject

class UpdateCallLogOngoingUseCase @Inject constructor(
    private val repository: CallLogRepository
) : NoResultUseCase<UpdateCallLogOngoingUseCase.Params> {
    override suspend fun invoke(parameter: Params) {
        return repository.updateCallLogOngoing(
            callId = parameter.id,
            isOngoing = parameter.isOngoing
        )
    }

    class Params(
        val id: Long,
        val isOngoing: Boolean
    )
}