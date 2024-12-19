package com.ramzisai.callmonitor.presentation.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Service(
    @SerialName("name")
    val name: String,
    @SerialName("uri")
    val uri: String
)