package com.ramzisai.callmonitor.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ramzisai.callmonitor.R
import com.ramzisai.callmonitor.presentation.MainActivity

class CallMonitorService: Service() {

    private lateinit var telephonyManager: TelephonyManager
    private var telephonyCallback: TelephonyCallbackImpl? = null

    @RequiresApi(Build.VERSION_CODES.S)
    private inner class TelephonyCallbackImpl : TelephonyCallback(), TelephonyCallback.CallStateListener {
        override fun onCallStateChanged(state: Int) {
            Log.d(TAG, "onCallStateChanged - state: $state")
        }
    }

    @Suppress("DEPRECATION")
    private val phoneStateListener = object : PhoneStateListener() {
        @Suppress("OVERRIDE_DEPRECATION")
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            Log.d(TAG, "onCallStateChanged - state: $state, phoneNumber: $phoneNumber")
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        startForegroundServiceNotification()
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyCallback = TelephonyCallbackImpl()
            telephonyCallback?.let { telephonyManager.registerTelephonyCallback(baseContext.mainExecutor, it) }
        } else {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            telephonyCallback?.let {
                telephonyManager.unregisterTelephonyCallback(it)
            }
            telephonyCallback = null
        } else {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                FOREGROUND_SERVICE_CHANNEL,
                SERVICE_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
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

    companion object {
        val TAG = CallMonitorService::class.simpleName
        val FOREGROUND_SERVICE_ID = 1000
        val FOREGROUND_SERVICE_CHANNEL = "com.ramzisai.callmonitor.presentation.service.CallMonitorService.FOREGROUND_SERVICE_CHANNEL"
        val SERVICE_NAME = "com.ramzisai.callmonitor.presentation.service.CallMonitorService"
    }
}