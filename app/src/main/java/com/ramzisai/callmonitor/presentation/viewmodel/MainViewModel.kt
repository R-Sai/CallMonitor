package com.ramzisai.callmonitor.presentation.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import com.ramzisai.callmonitor.domain.usecase.ObserveCallLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    val observeCallLogUseCase: ObserveCallLogUseCase
) : AndroidViewModel(application) {

    private val _callLog = MutableStateFlow<List<CallLogDomainModel>>(emptyList())
    val callLog: StateFlow<List<CallLogDomainModel>> = _callLog

    private val _isWifiEnabled = mutableStateOf(false)
    val isWifiEnabled: State<Boolean> = _isWifiEnabled

    private val _address = mutableStateOf("")
    val address: State<String> = _address

    private var callLogJob: Job? = null

    init {
        loadCallLog()
    }

    private fun loadCallLog() {
        callLogJob?.cancel()
        callLogJob = viewModelScope.launch {
            observeCallLogUseCase(Unit).collect { callLog ->
                _callLog.value = callLog
            }
        }
    }

    fun onWifiConnected(address: String?) {
        _isWifiEnabled.value = true
        _address.value = address ?: ""
    }

    fun onWifiDisconnected() {
        _isWifiEnabled.value = false
    }
}