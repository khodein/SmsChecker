package com.sms.checker.forwarder.feature.listening.domain.usecase

import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository

internal class StartListeningUseCase(
    private val repository: ListeningRepository,
) {
    operator fun invoke() = repository.startListening()
}