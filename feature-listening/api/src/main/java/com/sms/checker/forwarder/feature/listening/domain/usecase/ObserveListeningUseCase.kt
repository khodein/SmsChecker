package com.sms.checker.forwarder.feature.listening.domain.usecase

import kotlinx.coroutines.flow.Flow

interface ObserveListeningUseCase {
    operator fun invoke(): Flow<Boolean>
}