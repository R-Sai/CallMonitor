package com.ramzisai.callmonitor.presentation.model

import com.ramzisai.callmonitor.presentation.Constants.SERVER.ROUTE_LOG
import com.ramzisai.callmonitor.presentation.Constants.SERVER.ROUTE_STATUS
import com.ramzisai.callmonitor.presentation.util.DateUtil
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RootResponse(
    @SerialName("start")
    val start: String,
    @SerialName("services")
    val services: List<Service>
) {
    companion object {
        const val STATUS_NAME = "status"
        const val LOG_NAME = "log"

        fun create(start: Long?, address: String?, port: Int) = RootResponse(
            start = DateUtil.format(start ?: System.currentTimeMillis()),
            services = listOf(
                Service(STATUS_NAME, "http://${address.orEmpty()}:$port$ROUTE_STATUS"),
                Service(LOG_NAME, "http://${address.orEmpty()}:$port$ROUTE_LOG")
            )
        )
    }
}