package com.ramzisai.callmonitor.presentation.util

import android.content.Context
import android.provider.CallLog

object AndroidCallLogUtil {
    fun getPhoneNumberFromLog(context: Context): Pair<String?, String?> {
        val uri = CallLog.Calls.CONTENT_URI
        val projection = arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.CACHED_NAME)
        val sortOrder = "${CallLog.Calls.DATE} DESC"
        val cursor = context.contentResolver.query(uri, projection, null, null, sortOrder)

        cursor?.use {
            if (it.moveToFirst()) {
                val numberColumnIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
                val nameColumnIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val number = if (numberColumnIndex >= 0) it.getString(numberColumnIndex) else null
                val name = if (nameColumnIndex >= 0) it.getString(nameColumnIndex) else null
                return number to name
            }
        }
        return null to null
    }
}