package com.sms.checker.forwarder.feature.listening.domain.usecase

import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository
import kotlinx.coroutines.flow.Flow

internal class GetListeningObserveUseCase(
    private val repository: ListeningRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.observeListening()
}