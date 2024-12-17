package com.ramzisai.callmonitor.presentation.model

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
    val onGoing: Boolean,
    val number: String?,
    val name: String?
)