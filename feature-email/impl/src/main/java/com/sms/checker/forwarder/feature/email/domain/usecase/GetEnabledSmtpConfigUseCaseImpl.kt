package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal class GetEnabledSmtpConfigUseCaseImpl(
    private val repository: EmailRepository,
) : GetEnabledSmtpConfigUseCase {
    override suspend fun invoke(): List<SmtpEmailModel> {
        return repository.getEnabledSmtpList()
    }
}
