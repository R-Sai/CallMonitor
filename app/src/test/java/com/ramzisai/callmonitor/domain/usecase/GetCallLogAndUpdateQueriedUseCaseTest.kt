package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetCallLogAndUpdateQueriedUseCaseTest {

    val mockRepository: CallLogRepository = mockk()

    private lateinit var getCallLogAndUpdateQueriedUseCase: GetCallLogAndUpdateQueriedUseCase

    @Before
    fun setUp() {
        getCallLogAndUpdateQueriedUseCase = GetCallLogAndUpdateQueriedUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(mockRepository)
    }

    @Test
    fun `when use case is called and callLog is empty then return empty flow and update nothing`() = runTest {
        // given
        coEvery { mockRepository.getCallLog() } returns emptyFlow()

        // when
        val callLog = getCallLogAndUpdateQueriedUseCase(Unit).firstOrNull()

        // then
        assert(callLog == null) { "use case should not return any values" }
        coVerify { mockRepository.getCallLog() }
        coVerify(exactly = 0) { mockRepository.updateCallLogs(any()) }
    }

    @Test
    fun `when use case is called and callLog is not empty then return callLog flow and increment timesQueried`() = runTest {
        // given
        coEvery { mockRepository.getCallLog() } returns flowOf(CALL_LOG_ENTRIES)
        coEvery { mockRepository.updateCallLogs(any()) } returns Unit

        // when
        val callLog = getCallLogAndUpdateQueriedUseCase(Unit).single()

        // then
        assert(callLog == CALL_LOG_ENTRIES) { "use case does not return expected call entries" }
        coVerify { mockRepository.getCallLog() }
        val updatedCallLog = CALL_LOG_ENTRIES.map {
            it.copy(timesQueried = it.timesQueried + 1)
        }
        coVerify { mockRepository.updateCallLogs(updatedCallLog) }
    }

    companion object {
        val CALL_LOG_ENTRIES = listOf(
            CallLogEntry(
                id = 1,
                timestamp = 111,
                duration = 10,
                number = "123456789",
                name = "test 1",
                timesQueried = 0,
                isOngoing = true,
            ),
            CallLogEntry(
                id = 2,
                timestamp = 222,
                duration = 20,
                number = "987654321",
                name = "test 2",
                timesQueried = 1,
                isOngoing = false,
            )
        )
    }
}