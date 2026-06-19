package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import kotlinx.coroutines.flow.Flow

internal class ObserveSmtpLimitUseCaseImpl(
    private val repository: EmailRepository,
) : ObserveSmtpLimitUseCase {
    override fun invoke(): Flow<Boolean> {
        return repository.isSmtpLimitFlow
    }
}