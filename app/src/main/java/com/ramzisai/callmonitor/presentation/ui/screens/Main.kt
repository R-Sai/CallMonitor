package com.ramzisai.callmonitor.presentation.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramzisai.callmonitor.R
import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import com.ramzisai.callmonitor.presentation.util.DateUtil


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
        ServerControlCard(
            address = address,
            isServerRunning = isServerRunning,
            isWifiEnabled = isWifiEnabled,
            onOpenWifiSettingsClicked = onOpenWifiSettingsClicked
        ) {
            isServerRunning = !isServerRunning
            onServerButtonClicked(isServerRunning)
        }

        CallLog(callLog = callLog)
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

@Composable
fun ServerControlCard(
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

@Composable
fun CallLog(
    modifier: Modifier = Modifier,
    callLog: List<CallLogDomainModel>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.label_call_log),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = callLog,
            ) { entry ->
                CallLogItem(entry = entry)
            }
        }
    }
}

@Composable
fun CallLogItem(
    modifier: Modifier = Modifier,
    entry: CallLogDomainModel
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(entry.id.toString())
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(R.drawable.ic_call_36),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Spacer(Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = entry.name.takeUnless { it.isNullOrEmpty() } ?: stringResource(R.string.label_unknown),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = entry.number ?: stringResource(R.string.label_unknown),
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                CallLogItemDetail(
                    text = DateUtil.formatDuration(entry.duration),
                    resource = R.drawable.ic_duration_16
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CallLogItemPreview() {
    CallLogItem(
        entry = CallLogDomainModel(
            number = "695436313",
            name = "Ramzi Sai",
            timestamp = 1734557921434,
            duration = 260,
            isOngoing = false
        )
    )
}

@Composable
fun CallLogItemDetail(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes resource: Int,
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.width(4.dp))
        Image(
            painter = painterResource(resource),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
    }
}
