package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
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
class GetOngoingCallUseCaseTest {

    val mockRepository: CallLogRepository = mockk()

    private lateinit var getOngoingCallUseCase: GetOngoingCallUseCase

    @Before
    fun setUp() {
        getOngoingCallUseCase = GetOngoingCallUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(mockRepository)
    }

    @Test
    fun `when use case is called and call is ongoing then return ongoing call entry`() = runTest {
        // given
        coEvery { mockRepository.getCallLog() } returns flowOf(listOf(ONGOING_CALL_ENTRY, NOT_ONGOING_CALL_ENTRY))

        // when
        val callLog = getOngoingCallUseCase(Unit).firstOrNull()

        // then
        assert(callLog == ONGOING_CALL_ENTRY) { "use case should return ongoing call" }
        coVerify { mockRepository.getCallLog() }
    }

    @Test
    fun `when use case is called and call is not ongoing then return nothing`() = runTest {
        // given
        coEvery { mockRepository.getCallLog() } returns flowOf(listOf(NOT_ONGOING_CALL_ENTRY, NOT_ONGOING_CALL_ENTRY))

        // when
        val callLog = getOngoingCallUseCase(Unit).single()

        // then
        assert(callLog == null) { "use case should not return ongoing call" }
        coVerify { mockRepository.getCallLog() }
    }

    companion object {
        val ONGOING_CALL_ENTRY = CallLogEntry(
            id = 1,
            timestamp = 111,
            duration = 10,
            number = "123456789",
            name = "test 1",
            timesQueried = 0,
            isOngoing = true,
        )
        val NOT_ONGOING_CALL_ENTRY = CallLogEntry(
            id = 2,
            timestamp = 222,
            duration = 20,
            number = "987654321",
            name = "test 2",
            timesQueried = 1,
            isOngoing = false,
        )
    }
}