package com.ramzisai.callmonitor.presentation.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramzisai.callmonitor.R
import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.presentation.util.DateUtil


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    callLog: List<CallLogEntry>,
    address: String,
    onStartServerClicked: () -> Unit,
) {
    var isServerRunning by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ServerControlCard(address = address, isServerRunning = isServerRunning) {
            onStartServerClicked()
            isServerRunning = !isServerRunning
        }

        CallLog(callLog = callLog)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        callLog = listOf(
            CallLogEntry(
                timestamp = 12345,
                duration = 12345L,
                number = "123-456-789",
                name = "John Doe",
                timesQueried = 1,
                isOngoing = false
            )
        ),
        address = "192.10.0.1",
        onStartServerClicked = {},
    )
}

@Composable
fun ServerControlCard(
    modifier: Modifier = Modifier,
    address: String,
    isServerRunning: Boolean,
    onStartServerClicked: () -> Unit,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
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
                    Toast.makeText(context, "TODO Starting server", Toast.LENGTH_SHORT).show()
                    onStartServerClicked()
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.button_start_server))
            }
        }
    }
}

@Composable
fun CallLog(
    modifier: Modifier = Modifier,
    callLog: List<CallLogEntry>
) {
    Column(
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
    entry: CallLogEntry
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = entry.name.takeUnless { it.isNullOrEmpty() } ?: stringResource(R.string.label_unknown),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = entry.number ?: stringResource(R.string.label_unknown),
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = DateUtil.formatPretty(entry.timestamp),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${entry.duration}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "${entry.timesQueried}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}