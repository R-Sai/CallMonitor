package com.ramzisai.callmonitor.data.mapper

import com.ramzisai.callmonitor.data.model.CallLogDataModel
import com.ramzisai.callmonitor.domain.model.CallLogDomainModel

class CallLogMapper {
    fun map(callLogEntity: CallLogDataModel) =
        CallLogDomainModel(
            id = callLogEntity.id,
            timestamp = callLogEntity.timestamp,
            duration = callLogEntity.duration,
            number = callLogEntity.number,
            name = callLogEntity.name,
            timesQueried = callLogEntity.timesQueried,
            isOngoing = callLogEntity.isOngoing
        )

    fun map(callLogEntry: CallLogDomainModel) =
        CallLogDataModel(
            id = callLogEntry.id,
            timestamp = callLogEntry.timestamp,
            duration = callLogEntry.duration,
            number = callLogEntry.number,
            name = callLogEntry.name,
            timesQueried = callLogEntry.timesQueried,
            isOngoing = callLogEntry.isOngoing
        )
}