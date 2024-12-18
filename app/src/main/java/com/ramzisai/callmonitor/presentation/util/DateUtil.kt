package com.ramzisai.callmonitor.presentation.util

import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale


object DateUtil {
    fun format(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        return formatter.format(instant)
    }

    fun formatPretty(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd")
        return formatter.format(instant)
    }

    fun formatDuration(seconds: Long): String {
        val duration = Duration.ofSeconds(seconds)
        return String.format(locale = Locale.getDefault(), "%02d:%02d", duration.toMinutes(), duration.toSecondsPart())
    }
}