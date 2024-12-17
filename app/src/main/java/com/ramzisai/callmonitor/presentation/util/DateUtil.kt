package com.ramzisai.callmonitor.presentation.util

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


object DateUtil {
    fun format(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return formatter.format(instant)
    }

    fun formatPretty(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return formatter.format(instant)
    }
}