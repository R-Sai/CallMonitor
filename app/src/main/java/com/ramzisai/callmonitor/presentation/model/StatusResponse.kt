package com.ramzisai.callmonitor.presentation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
    @SerialName("onGoing")
    val onGoing: Boolean,
    @SerialName("number")
    val number: String?,
    @SerialName("name")
    val name: String?
)