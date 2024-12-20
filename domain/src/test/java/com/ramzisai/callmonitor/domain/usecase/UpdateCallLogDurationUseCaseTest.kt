package com.ramzisai.callmonitor.domain.usecase

import com.ramzisai.callmonitor.domain.repository.CallLogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UpdateCallLogDurationUseCaseTest {

    val mockRepository: CallLogRepository = mockk()

    private lateinit var updateCallLogDurationUseCase: UpdateCallLogDurationUseCase

    @Before
    fun setUp() {
        updateCallLogDurationUseCase = UpdateCallLogDurationUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(mockRepository)
    }

    @Test
    fun `when use case is called then repository should be updated with parameter values`() = runTest {
        // given
        val params = UpdateCallLogDurationUseCase.Params(
            id = TEST_ID,
            duration = DURATION
        )
        coEvery { mockRepository.updateCallLogDuration(any(), any()) } returns Unit

        // when
        updateCallLogDurationUseCase(params)

        // then
        coVerify { mockRepository.updateCallLogDuration(TEST_ID, DURATION) }
    }

    companion object {
        const val TEST_ID = 12345L
        const val DURATION = 30L
    }
}
