package com.ramzisai.callmonitor

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import com.ramzisai.callmonitor.presentation.ui.screens.MainScreen
import com.ramzisai.callmonitor.presentation.ui.theme.CallMonitorTheme
import com.ramzisai.callmonitor.presentation.util.DateUtil
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mainScreenCallLogTest() {
        // given
        val mockCallLog = getMockCallLog()

        // when
        composeTestRule.setContent {
            CallMonitorTheme {
                MainScreen(
                    callLog = mockCallLog,
                    address = MOCK_ADDRESS,
                    isWifiEnabled = true,
                    onOpenWifiSettingsClicked = {},
                    onServerButtonClicked = {},
                )
            }
        }

        // then
        with(composeTestRule) {
            onNodeWithTag("1").assertIsDisplayed()
            onNodeWithTag("2").assertIsDisplayed()
            onNodeWithTag("3").assertIsDisplayed()
            onNodeWithTag("4").assertIsDisplayed()
            mockCallLog.forEach { entry ->
                onNodeWithText(entry.name ?: "Unknown").assertIsDisplayed()
                onNodeWithText(entry.number ?: "Unknown").assertIsDisplayed()
                onNodeWithText(DateUtil.formatDuration(entry.duration)).assertIsDisplayed()
            }
        }
    }

    @Test
    fun mainScreenServerCardTest() {
        // given
        var isServerRunning = false

        // when
        composeTestRule.setContent {
            CallMonitorTheme {
                MainScreen(
                    callLog = emptyList(),
                    address = MOCK_ADDRESS,
                    isWifiEnabled = true,
                    onOpenWifiSettingsClicked = {},
                    onServerButtonClicked = { isrunning ->
                        isServerRunning = isrunning
                    }
                )
            }
        }

        // then
        with(composeTestRule) {
            onNodeWithText(MOCK_ADDRESS).assertIsDisplayed()
            onNodeWithText("Start server").assertIsDisplayed()
            onNodeWithText("Server stopped").assertIsDisplayed()

            onNodeWithText("Start server").performClick()

            onNodeWithText("Stop server").assertIsDisplayed()
            onNodeWithText("Server running").assertIsDisplayed()
        }
        assert(isServerRunning) { "onServerButtonClicked callback was not called" }
    }

    private fun getMockCallLog() = listOf(
        CallLogDomainModel(
            id = 1,
            timestamp = 100,
            duration = 30,
            number = "695436313",
            name = "Ramzi Sai",
            timesQueried = 0,
            isOngoing = false
        ),
        CallLogDomainModel(
            id = 2,
            timestamp = 90,
            duration = 120,
            number = "555230632",
            name = null,
            timesQueried = 0,
            isOngoing = false
        ),
        CallLogDomainModel(
            id = 3,
            timestamp = 80,
            duration = 0,
            number = "854900455",
            name = "John Doe",
            timesQueried = 0,
            isOngoing = false
        ),
        CallLogDomainModel(
            id = 4,
            timestamp = 70,
            duration = 345,
            number = "565800900",
            name = "Jane Doe",
            timesQueried = 0,
            isOngoing = false
        )
    )

    companion object {
        const val MOCK_ADDRESS = "http://192.168.0.10:12345"
    }
}