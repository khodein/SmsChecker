package com.sms.checker.forwarder.feature.listening.domain.usecase

import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository

internal class GetListeningUseCase(
    private val repository: ListeningRepository,
) {
    operator fun invoke() = repository.isListening()
}