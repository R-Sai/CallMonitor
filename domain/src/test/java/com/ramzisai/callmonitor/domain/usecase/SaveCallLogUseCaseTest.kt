package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.model.CallLogEntry
import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SaveCallLogUseCaseTest {

    val mockRepository: CallLogRepository = mockk()

    private lateinit var saveCallLogUseCase: SaveCallLogUseCase

    @Before
    fun setUp() {
        saveCallLogUseCase = SaveCallLogUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(mockRepository)
    }

    @Test
    fun `when use case is called then repository should put call log with param values and return ID`() = runTest {
        // given
        val param = SaveCallLogUseCase.Params(
            name = NAME,
            number = NUMBER,
            timestamp = TIMESTAMP
        )
        coEvery { mockRepository.putCallLog(any()) } returns flowOf(ID)

        // when
        val id = saveCallLogUseCase(param).single()

        // then
        assert(id == ID) { "returned ID does not match expected ID" }
        coVerify {
            mockRepository.putCallLog(
                eq(
                    CallLogEntry(
                        name = NAME,
                        number = NUMBER,
                        timestamp = TIMESTAMP,
                        isOngoing = true
                    )
                )
            )
        }
    }

    companion object {
        const val ID = 123L
        const val NAME = "name"
        const val NUMBER = "number"
        const val TIMESTAMP = 123456L
    }
}
