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
import com.ramzisai.callmonitor.domain.usecase.GetCallLogAndUpdateQueriedUseCase
import com.ramzisai.callmonitor.domain.usecase.GetOngoingCallUseCase
import com.ramzisai.callmonitor.presentation.Constants.FOREGROUND_SERVICE_CHANNEL
import com.ramzisai.callmonitor.presentation.Constants.SERVER.ROUTE_LOG
import com.ramzisai.callmonitor.presentation.Constants.SERVER.ROUTE_ROOT
import com.ramzisai.callmonitor.presentation.Constants.SERVER.ROUTE_STATUS
import com.ramzisai.callmonitor.presentation.Constants.SERVER.SERVER_PORT
import com.ramzisai.callmonitor.presentation.MainActivity
import com.ramzisai.callmonitor.presentation.model.LogResponse
import com.ramzisai.callmonitor.presentation.model.RootResponse
import com.ramzisai.callmonitor.presentation.model.StatusResponse
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class ServerService : ScopedService() {

    private var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null
    var startTime: Long? = null

    @Inject
    lateinit var getOngoingCallUseCase: GetOngoingCallUseCase

    @Inject
    lateinit var getCallLogAndUpdateQueriedUseCase: GetCallLogAndUpdateQueriedUseCase

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
        server = embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0") {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }

            routing {
                get(ROUTE_ROOT) {
                    call.respond(
                        RootResponse.create(
                            start = startTime,
                            port = SERVER_PORT,
                            address = NetworkUtil.getWifiIpAddress(this@ServerService)
                        )
                    )
                }
                get(ROUTE_STATUS) {
                    val onGoingCall = getOngoingCallUseCase(Unit).firstOrNull()
                    call.respond(
                        StatusResponse(
                            onGoing = onGoingCall?.isOngoing ?: false,
                            number = onGoingCall?.number,
                            name = onGoingCall?.name
                        )
                    )
                }
                get(ROUTE_LOG) {
                    val callLog = getCallLogAndUpdateQueriedUseCase(Unit).firstOrNull()
                    call.respond(
                        LogResponse.create(callLog)
                    )
                }
            }

        }.start(wait = false)
        Log.i(TAG, "Server started successfully")
    }

    companion object {
        val TAG = ServerService::class.simpleName
        const val FOREGROUND_SERVICE_ID = 1010
    }
}