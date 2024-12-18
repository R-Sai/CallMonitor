package com.ramzisai.callmonitor.presentation.model

import kotlinx.serialization.SerialName

data class CallState(
    @SerialName("ongoing")
    val ongoing: Boolean,
    @SerialName("number")
    val number: String? = null
)