package com.ramzisai.callmonitor.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import com.ramzisai.callmonitor.presentation.ui.components.CallLogComponent
import com.ramzisai.callmonitor.presentation.ui.components.ServerControlCardComponent


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    callLog: List<CallLogDomainModel>,
    address: String,
    isWifiEnabled: Boolean,
    onOpenWifiSettingsClicked: () -> Unit,
    onServerButtonClicked: (Boolean) -> Unit,
) {
    var isServerRunning by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ServerControlCardComponent(
            address = address,
            isServerRunning = isServerRunning,
            isWifiEnabled = isWifiEnabled,
            onOpenWifiSettingsClicked = onOpenWifiSettingsClicked
        ) {
            isServerRunning = !isServerRunning
            onServerButtonClicked(isServerRunning)
        }

        CallLogComponent(callLog = callLog)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        callLog = listOf(
            CallLogDomainModel(
                timestamp = 12345,
                duration = 12345L,
                number = "123-456-789",
                name = "John Doe",
                timesQueried = 1,
                isOngoing = false
            )
        ),
        address = "192.10.0.1",
        isWifiEnabled = false,
        onOpenWifiSettingsClicked = {},
        onServerButtonClicked = {},
    )
}
