package com.ramzisai.callmonitor.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ramzisai.callmonitor.R
import com.ramzisai.callmonitor.presentation.Constants.FOREGROUND_SERVICE_CHANNEL
import com.ramzisai.callmonitor.presentation.MainActivity
import com.ramzisai.callmonitor.presentation.model.RootResponse
import com.ramzisai.callmonitor.presentation.util.NetworkUtil
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class ServerService : ScopedService() {

    private var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null
    var startTime: Long? = null

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
        startTime = System.currentTimeMillis()
        startForegroundServiceNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        if (server == null) {
            startWebServer()
        }
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        server?.stop(1000, 2000)
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
            .setContentTitle(getString(R.string.notification_call_server_service_running))
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

    private fun startWebServer() {
        server = embeddedServer(Netty, port = PORT, host = "0.0.0.0") {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }

            routing {
                get("/") {
                    call.respond(
                        RootResponse.create(
                            start = startTime,
                            port = PORT,
                            address = NetworkUtil.getWifiIpAddress(this@ServerService)
                        )
                    )
                }
            }

        }.start(wait = false)
        Log.i(TAG, "Server started successfully")
    }

    companion object {
        val TAG = ServerService::class.simpleName
        const val FOREGROUND_SERVICE_ID = 1010
        const val PORT = 12345
    }
}