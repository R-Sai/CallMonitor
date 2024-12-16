package com.ramzisai.callmonitor.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val name: String,
    val uri: String
)