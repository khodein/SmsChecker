package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal class SaveSmtpConfigUseCaseImpl(
    private val repository: EmailRepository,
) : SaveSmtpConfigUseCase {
    override suspend fun invoke(model: SmtpEmailModel): Long {
        return repository.setSmtpConfig(model)
    }
}