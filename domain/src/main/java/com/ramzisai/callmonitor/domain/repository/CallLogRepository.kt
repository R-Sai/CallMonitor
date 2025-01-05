package com.ramzisai.callmonitor.domain.repository

import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import kotlinx.coroutines.flow.Flow

interface CallLogRepository {
    suspend fun putCallLog(callLog: CallLogDomainModel): Flow<Long>
    suspend fun getCallLog(): Flow<List<CallLogDomainModel>>
    suspend fun updateCallLogs(callLogs: List<CallLogDomainModel>)
    suspend fun updateCallLogOngoing(callId: Long, isOngoing: Boolean)
    suspend fun updateCallLogDuration(callId: Long, duration: Long)
}