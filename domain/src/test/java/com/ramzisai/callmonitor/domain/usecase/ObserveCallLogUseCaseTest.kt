package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogDomainModel
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ObserveCallLogUseCaseTest {

    val mockRepository: CallLogRepository = mockk()

    private lateinit var observeCallLogUseCase: ObserveCallLogUseCase

    @Before
    fun setUp() {
        observeCallLogUseCase = ObserveCallLogUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(mockRepository)
    }

    @Test
    fun `when use case is called it should observe and return all values emitted`() = runTest {
        // given
        coEvery { mockRepository.getCallLog() } returns flow {
            emit(CALL_LOG_ENTRIES_1)
            delay(FLOW_DELAY_MS)
            emit(CALL_LOG_ENTRIES_2)
        }

        // when
        val callLog = mutableListOf<List<CallLogDomainModel>>()
        observeCallLogUseCase(Unit).toList(callLog)

        // then
        assert(callLog.containsAll(listOf(CALL_LOG_ENTRIES_1, CALL_LOG_ENTRIES_2))) { "use case should return ongoing call" }
        coVerify { mockRepository.getCallLog() }
    }

    companion object {
        const val FLOW_DELAY_MS = 200L
        val CALL_LOG_ENTRIES_1 = listOf(
            CallLogDomainModel(
                id = 1,
                timestamp = 456,
                duration = 10,
                number = "123456789",
                name = "test 1",
                timesQueried = 0,
                isOngoing = true,
            ),
            CallLogDomainModel(
                id = 2,
                timestamp = 123,
                duration = 20,
                number = "987654321",
                name = "test 2",
                timesQueried = 1,
                isOngoing = false,
            )
        )
        val CALL_LOG_ENTRIES_2 = listOf(
            CallLogDomainModel(
                id = 3,
                timestamp = 789,
                duration = 30,
                number = "123456789",
                name = "test 3",
                timesQueried = 2,
                isOngoing = false,
            ),
            CallLogDomainModel(
                id = 4,
                timestamp = 678,
                duration = 40,
                number = "987654321",
                name = "test 4",
                timesQueried = 3,
                isOngoing = false,
            )
        )
    }
}