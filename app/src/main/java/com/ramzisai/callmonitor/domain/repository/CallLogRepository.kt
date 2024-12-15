package com.ramzisai.callmonitor.domain.repository

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import kotlinx.coroutines.flow.Flow

interface CallLogRepository {
    suspend fun putCallLog(callLog: CallLogEntry)
    fun getCallLog(): Flow<List<CallLogEntry>>
}