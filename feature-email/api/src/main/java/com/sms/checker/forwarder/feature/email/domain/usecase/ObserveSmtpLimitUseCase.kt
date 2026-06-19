package com.sms.checker.forwarder.feature.email.domain.usecase

import kotlinx.coroutines.flow.Flow

interface ObserveSmtpLimitUseCase {
    operator fun invoke(): Flow<Boolean>
}