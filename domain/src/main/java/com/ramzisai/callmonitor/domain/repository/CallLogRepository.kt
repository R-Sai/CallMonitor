package com.ramzisai.callmonitor.domain.repository

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import kotlinx.coroutines.flow.Flow

interface CallLogRepository {
    suspend fun putCallLog(callLog: CallLogEntry): Flow<Long>
    suspend fun getCallLog(): Flow<List<CallLogEntry>>
    suspend fun updateCallLogs(callLogs: List<CallLogEntry>)
    suspend fun updateCallLogOngoing(callId: Long, isOngoing: Boolean)
    suspend fun updateCallLogDuration(callId: Long, duration: Long)
}