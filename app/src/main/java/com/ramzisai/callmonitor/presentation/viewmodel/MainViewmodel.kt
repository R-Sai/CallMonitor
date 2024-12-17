package com.ramzisai.callmonitor.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.domain.usecase.GetCallLogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    val getCallLogUseCase: GetCallLogUseCase
) : AndroidViewModel(application) {

    private val _callLog = MutableStateFlow<List<CallLogEntry>>(emptyList())
    val callLog: StateFlow<List<CallLogEntry>> = _callLog

    private var callLogJob: Job? = null

    init {
        loadCallLog()
    }

    private fun loadCallLog() {
        callLogJob?.cancel()
        callLogJob = viewModelScope.launch {
            getCallLogUseCase(Unit).collect { callLog ->
                _callLog.value = callLog
            }
        }
    }
}