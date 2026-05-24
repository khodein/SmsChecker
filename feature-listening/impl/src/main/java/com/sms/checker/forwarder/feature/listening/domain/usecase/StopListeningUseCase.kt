package com.sms.checker.forwarder.feature.listening.domain.usecase

import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository

internal class StopListeningUseCase(
    private val repository: ListeningRepository,
) {
    operator fun invoke() = repository.stopListening()
}