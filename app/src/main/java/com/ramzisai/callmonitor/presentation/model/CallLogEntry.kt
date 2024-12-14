package com.ramzisai.callmonitor.presentation.model

data class CallLogEntry(
    val beginning: String,
    val duration: Long,
    val number: String,
    val name: String,
    var timesQueried: Int = 0
)