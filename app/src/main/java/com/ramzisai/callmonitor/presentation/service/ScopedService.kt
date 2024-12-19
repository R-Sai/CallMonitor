package com.ramzisai.callmonitor.presentation.service

import android.app.Service
import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ScopedService : Service() {
    private val serviceJob = SupervisorJob()
    val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}