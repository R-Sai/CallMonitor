package com.ramzisai.callmonitor.presentation.model

data class CallState(
    val ongoing: Boolean,
    val number: String? = null
)