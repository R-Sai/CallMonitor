package com.ramzisai.callmonitor.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ramzisai.callmonitor.domain.model.CallLogEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _callLog = MutableStateFlow<List<CallLogEntry>>(emptyList())
    val callLog = _callLog.asStateFlow()

    fun loadCallLog() {
        viewModelScope.launch {
            _callLog.value = List(10) {
                CallLogEntry(
                    timestamp = 12345,
                    duration = 12345L,
                    number = "123-456-789",
                    name = "John Doe",
                    timesQueried = 1,
                )
            }
        }
    }
}