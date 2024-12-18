package com.ramzisai.callmonitor.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ramzisai.callmonitor.R
import com.ramzisai.callmonitor.domain.usecase.SaveCallLogUseCase
import com.ramzisai.callmonitor.domain.usecase.UpdateCallLogDurationUseCase
import com.ramzisai.callmonitor.domain.usecase.UpdateCallLogOngoingUseCase
import com.ramzisai.callmonitor.presentation.Constants.FOREGROUND_SERVICE_CHANNEL
import com.ramzisai.callmonitor.presentation.MainActivity
import com.ramzisai.callmonitor.presentation.util.AndroidCallLogUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CallMonitorService : ScopedService() {

    private lateinit var telephonyManager: TelephonyManager
    private var currentCallId: Long? = null
    private var currentCallStartTime: Long? = null

    @Inject
    lateinit var saveCallLogUseCase: SaveCallLogUseCase

    @Inject
    lateinit var updateCallLogOngoingUseCase: UpdateCallLogOngoingUseCase

    @Inject
    lateinit var updateCallLogDurationUseCase: UpdateCallLogDurationUseCase

    @Suppress("DEPRECATION")
    private val phoneStateListener = object : PhoneStateListener() {
        @Suppress("OVERRIDE_DEPRECATION")
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            val name = phoneNumber
                .takeUnless { it.isNullOrEmpty() }
                ?.let { AndroidCallLogUtil.getContactName(this@CallMonitorService, it) }
            when (state) {
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    currentCallStartTime = System.currentTimeMillis()
                    currentCallId?.let {
                        updateCurrentCallState(it, isOngoing = true)
                    } ?: createNewCallLogEntry(number = phoneNumber, name = name)
                }

                TelephonyManager.CALL_STATE_RINGING -> {
                    createNewCallLogEntry(number = phoneNumber, name = name)
                }

                TelephonyManager.CALL_STATE_IDLE -> {
                    onCallEnded()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        startForegroundServiceNotification()
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
    }

    private fun startForegroundServiceNotification() {
        createNotificationChannel()
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, FOREGROUND_SERVICE_CHANNEL)
            .setContentTitle(getString(R.string.notification_call_monitor_service_running))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(FOREGROUND_SERVICE_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                FOREGROUND_SERVICE_CHANNEL,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun createNewCallLogEntry(number: String?, name: String?) {
        val startTime = System.currentTimeMillis()
        serviceScope.launch {
            saveCallLogUseCase(
                SaveCallLogUseCase.Params(
                    name = name,
                    number = number,
                    timestamp = startTime
                )
            ).collect { id ->
                currentCallId = id
            }
        }
    }

    fun updateCurrentCallState(callId: Long, isOngoing: Boolean) {
        serviceScope.launch {
            updateCallLogOngoingUseCase(
                UpdateCallLogOngoingUseCase.Params(
                    id = callId,
                    isOngoing = isOngoing,
                )
            )
        }
    }

    private fun onCallEnded() {
        val duration = currentCallStartTime?.let { (System.currentTimeMillis() - it) / 1000 }
        currentCallId?.let { callId ->
            updateCurrentCallState(callId, isOngoing = false)
            duration?.let { updateCurrentCallDuration(callId, duration = it) }
        }
        currentCallId = null
        currentCallStartTime = null
    }

    private fun updateCurrentCallDuration(callId: Long, duration: Long) {
        serviceScope.launch {
            updateCallLogDurationUseCase(
                UpdateCallLogDurationUseCase.Params(
                    id = callId,
                    duration = duration,
                )
            )
        }
    }

    companion object {
        val TAG = CallMonitorService::class.simpleName
        const val FOREGROUND_SERVICE_ID = 1000
    }
}