package com.ramzisai.callmonitor.data.repository

import com.ramzisai.callmonitor.data.dao.CallLogDao
import com.ramzisai.callmonitor.data.mapper.CallLogMapper
import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CallLogRepositoryImpl @Inject constructor(
    private val dao: CallLogDao,
    private val mapper: CallLogMapper
) : CallLogRepository {

    override suspend fun putCallLog(callLog: CallLogEntry): Flow<Long> {
        return flowOf(dao.insert(mapper.map(callLog)))
    }

    override suspend fun getCallLog(): Flow<List<CallLogEntry>> {
        return dao.getCallLogs().map { entities ->
            entities.map { mapper.map(it) }
        }
    }

    override suspend fun updateCallLogs(callLogs: List<CallLogEntry>) {
        val callLogEntities = callLogs.map(mapper::map)
        dao.updateCallLogs(callLogEntities)
    }

    override suspend fun updateCallLogOngoing(callId: Long, isOngoing: Boolean) {
        dao.updateCallLogOnGoing(callId, isOngoing)
    }

    override suspend fun updateCallLogDuration(callId: Long, duration: Long) {
        dao.updateCallLogDuration(callId, duration)
    }
}