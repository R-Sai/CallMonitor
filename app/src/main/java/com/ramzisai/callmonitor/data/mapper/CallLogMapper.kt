package com.ramzisai.callmonitor.data.mapper

import com.ramzisai.callmonitor.data.model.CallLogEntity
import com.ramzisai.callmonitor.domain.model.CallLogEntry

class CallLogMapper {
    fun map(callLogEntity: CallLogEntity) =
        CallLogEntry(
            timestamp = callLogEntity.timestamp,
            duration = callLogEntity.duration,
            number = callLogEntity.number,
            name = callLogEntity.name,
            timesQueried = callLogEntity.timesQueried,
            isOngoing = callLogEntity.isOngoing
        )

    fun map(callLogEntry: CallLogEntry) =
        CallLogEntity(
            timestamp = callLogEntry.timestamp,
            duration = callLogEntry.duration,
            number = callLogEntry.number,
            name = callLogEntry.name,
            timesQueried = callLogEntry.timesQueried,
            isOngoing = callLogEntry.isOngoing
        )
}