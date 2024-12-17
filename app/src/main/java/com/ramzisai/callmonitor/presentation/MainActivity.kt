package com.ramzisai.callmonitor.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import androidx.core.content.ContextCompat
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
            Toast.makeText(this, getString(R.string.error_permissions), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isEmpty()) {
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CallMonitorTheme {
                CallMonitorApp(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel,
                    onStartServerClicked = { startServerService() }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkAndRequestPermissions()
    }
}

@Composable
fun CallMonitorApp(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onStartServerClicked: () -> Unit
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
            onStartServerClicked = onStartServerClicked
        )
    }
}
