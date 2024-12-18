package com.ramzisai.callmonitor.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.ramzisai.callmonitor.R
import com.ramzisai.callmonitor.presentation.Constants.SERVER.SERVER_PORT
import com.ramzisai.callmonitor.presentation.service.CallMonitorService
import com.ramzisai.callmonitor.presentation.service.ServerService
import com.ramzisai.callmonitor.presentation.ui.screens.MainScreen
import com.ramzisai.callmonitor.presentation.ui.theme.CallMonitorTheme
import com.ramzisai.callmonitor.presentation.util.NetworkUtil
import com.ramzisai.callmonitor.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var serviceBinder: ServerService.ServerServiceBinder? = null
    private var isServiceBound: Boolean = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
            serviceBinder = binder as ServerService.ServerServiceBinder?
            isServiceBound = true
            Log.d(TAG, "onServiceConnected")
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            isServiceBound = false
            Log.d(TAG, "onServiceDisconnected")
        }

    }

    @SuppressLint("InlinedApi")
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.POST_NOTIFICATIONS
    )

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            startCallMonitorService()
        } else {
            checkAndRequestPermissions()
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = requiredPermissions.filter {
            ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        val showPermissionRationale = permissionsToRequest.any {
            ActivityCompat.shouldShowRequestPermissionRationale(this, it)
        }

        if (showPermissionRationale) {
            Toast.makeText(this, getString(R.string.error_permissions), Toast.LENGTH_LONG).show()
        } else if (permissionsToRequest.isEmpty()) {
            startCallMonitorService()
        } else {
            permissionLauncher.launch(permissionsToRequest)
        }
    }

    private fun startCallMonitorService() {
        val intent = Intent(this, CallMonitorService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun startServerService() {
        val intent = Intent(this, ServerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun stopServerService() {
        if (isServiceBound) {
            serviceBinder?.stopServer()
            unbindService(serviceConnection)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CallMonitorTheme {
                CallMonitorApp(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel,
                    onStartServer = { startServerService() },
                    onStopServer = { stopServerService() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkAndRequestPermissions()
    }

    companion object {
        val TAG = MainActivity::class.simpleName
    }
}

@Composable
fun CallMonitorApp(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onStartServer: () -> Unit,
    onStopServer: () -> Unit
) {
    val context = LocalContext.current
    val callLog by viewModel.callLog.collectAsState()
    val address by rememberSaveable { mutableStateOf("http://${NetworkUtil.getWifiIpAddress(context)}:$SERVER_PORT") }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        MainScreen(
            callLog = callLog,
            address = address,
            onServerButtonClicked = { isServerRunning ->
                if (isServerRunning) {
                    onStartServer()
                } else {
                    onStopServer()
                }
            }
        )
    }
}
