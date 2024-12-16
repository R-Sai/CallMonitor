package com.ramzisai.callmonitor.presentation.model

import com.ramzisai.callmonitor.presentation.util.DateUtil
import kotlinx.serialization.Serializable

@Serializable
data class RootResponse(
    val start: String,
    val services: List<Service>
) {
    companion object {
        const val STATUS_NAME = "status"
        const val STATUS_PATH = "/status"
        const val LOG_NAME = "log"
        const val LOG_PATH = "/log"

        fun create(start: Long?, address: String?, port: Int) = RootResponse(
            start = DateUtil.format(start ?: System.currentTimeMillis()),
            services = listOf(
                Service(STATUS_NAME, "http://${address.orEmpty()}:$port$STATUS_PATH"),
                Service(LOG_NAME, "http://${address.orEmpty()}:$port$LOG_PATH")
            )
        )
    }
}