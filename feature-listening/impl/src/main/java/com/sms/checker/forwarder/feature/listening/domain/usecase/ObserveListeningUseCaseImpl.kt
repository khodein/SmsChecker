package com.sms.checker.forwarder.feature.listening.domain.usecase

import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository
import kotlinx.coroutines.flow.Flow

internal class ObserveListeningUseCaseImpl(
    private val repository: ListeningRepository,
) : ObserveListeningUseCase {
    override fun invoke(): Flow<Boolean> {
        return repository.observeListening()
    }
}