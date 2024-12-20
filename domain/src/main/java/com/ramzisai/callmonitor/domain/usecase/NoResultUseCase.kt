package com.ramzisai.callmonitor.domain.usecase

interface NoResultUseCase<in P> {
    suspend operator fun invoke(parameter: P)
}
