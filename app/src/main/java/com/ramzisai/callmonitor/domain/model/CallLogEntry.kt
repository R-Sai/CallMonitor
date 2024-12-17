package com.ramzisai.callmonitor.domain.model

data class CallLogEntry(
    val timestamp: Long,
    val duration: Long = 0,
    val number: String?,
    val name: String?,
    var timesQueried: Int = 0,
    var isOngoing: Boolean
)