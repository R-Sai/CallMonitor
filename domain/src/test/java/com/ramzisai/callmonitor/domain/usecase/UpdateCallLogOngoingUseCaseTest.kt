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
class UpdateCallLogOngoingUseCaseTest {

    val mockRepository: CallLogRepository = mockk()

    private lateinit var updateCallLogOngoingUseCase: UpdateCallLogOngoingUseCase

    @Before
    fun setUp() {
        updateCallLogOngoingUseCase = UpdateCallLogOngoingUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        confirmVerified(mockRepository)
    }

    @Test
    fun `when use case is called then repository should be updated with parameter values`() = runTest {
        // given
        val params = UpdateCallLogOngoingUseCase.Params(
            id = TEST_ID,
            isOngoing = true
        )
        coEvery { mockRepository.updateCallLogOngoing(any(), any()) } returns Unit

        // when
        updateCallLogOngoingUseCase(params)

        // then
        coVerify { mockRepository.updateCallLogOngoing(TEST_ID, true) }
    }

    companion object {
        const val TEST_ID = 12345L
    }
}