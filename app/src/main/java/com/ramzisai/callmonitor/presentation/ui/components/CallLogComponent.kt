package com.ramzisai.callmonitor.presentation.ui.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramzisai.callmonitor.R
import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import com.ramzisai.callmonitor.presentation.util.DateUtil

@Composable
fun CallLogComponent(
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

@Preview(showBackground = true)
@Composable
fun CallLogComponentPreview() {
    CallLogComponent(
        callLog = listOf(
            CallLogDomainModel(
                number = "695436313",
                name = "Ramzi Sai",
                timestamp = 1734557921434,
                duration = 260,
                isOngoing = false
            ),
            CallLogDomainModel(
                number = "555213874",
                name = "John Doe",
                timestamp = 1234557921434,
                duration = 30,
                isOngoing = false
            ),
            CallLogDomainModel(
                number = "555653973",
                name = "Jane Doe",
                timestamp = 1674557921434,
                duration = 154,
                isOngoing = false
            )
        )
    )
}
