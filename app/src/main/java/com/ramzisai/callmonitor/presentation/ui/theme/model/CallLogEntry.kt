package com.ramzisai.callmonitor.presentation.ui.theme.model

data class CallLogEntry(
    val beginning: String,
    val duration: Long,
    val number: String,
    val name: String,
    var timesQueried: Int = 0
)