package com.ramzisai.callmonitor.presentation.model

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.presentation.util.DateUtil
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogResponse(
    @SerialName("beginning")
    val beginning: String,
    @SerialName("duration")
    val duration: Long,
    @SerialName("number")
    val number: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("timesQueried")
    val timesQueried: Int
) {
    companion object {
        fun create(callLogs: List<CallLogEntry>?): List<LogResponse> {
            return callLogs?.map { callLog ->
                LogResponse(
                    beginning = DateUtil.format(callLog.timestamp),
                    duration = callLog.duration,
                    number = callLog.number,
                    name = callLog.name,
                    timesQueried = callLog.timesQueried
                )
            } ?: emptyList()
        }
    }
}