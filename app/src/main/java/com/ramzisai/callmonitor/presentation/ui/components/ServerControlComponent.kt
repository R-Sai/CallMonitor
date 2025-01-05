package com.ramzisai.callmonitor.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramzisai.callmonitor.R

@Composable
fun ServerControlCardComponent(
    modifier: Modifier = Modifier,
    address: String,
    isServerRunning: Boolean,
    isWifiEnabled: Boolean,
    onOpenWifiSettingsClicked: () -> Unit,
    onServerButtonClicked: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        if (isWifiEnabled) {
            EnabledServerControl(isServerRunning, address, onServerButtonClicked)
        } else {
            DisabledServerControl(onOpenWifiSettingsClicked)
        }
    }
}

@Composable
private fun EnabledServerControl(isServerRunning: Boolean, address: String, onServerButtonClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isServerRunning) stringResource(R.string.label_server_status_running) else stringResource(R.string.label_server_status_stopped),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = address,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                onServerButtonClicked()
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = if (isServerRunning) stringResource(R.string.button_stop_server) else stringResource(R.string.button_start_server)
            )
        }
    }
}

@Composable
private fun DisabledServerControl(onOpenWifiSettingsClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.label_no_wifi),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = stringResource(R.string.label_no_wifi_desc),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                onOpenWifiSettingsClicked()
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.button_open_wifi_settings)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServerControlCardComponentWifiEnabledPreview() {
    ServerControlCardComponent(
        address = "http://192.168.10.43:12345",
        isServerRunning = false,
        isWifiEnabled = true,
        onOpenWifiSettingsClicked = {},
        onServerButtonClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun ServerControlCardComponentWifiDisabledPreview() {
    ServerControlCardComponent(
        address = "http://192.168.10.43:12345",
        isServerRunning = false,
        isWifiEnabled = false,
        onOpenWifiSettingsClicked = {},
        onServerButtonClicked = {}
    )
}