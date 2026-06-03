package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal class UpdateSmtpConfigUseCaseImpl(
    private val repository: EmailRepository,
) : UpdateSmtpConfigUseCase {
    override suspend fun invoke(model: SmtpEmailModel): Long {
        return repository.updateSmtpConfig(model)
    }
}