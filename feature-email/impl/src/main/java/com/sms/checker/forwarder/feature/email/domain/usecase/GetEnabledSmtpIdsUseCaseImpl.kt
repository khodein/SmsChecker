package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository

internal class GetEnabledSmtpIdsUseCaseImpl(
    private val repository: EmailRepository,
) : GetEnabledSmtpIdsUseCase {
    override suspend fun invoke(): List<Long> {
        return repository.getEnabledSmtpIds()
    }
}