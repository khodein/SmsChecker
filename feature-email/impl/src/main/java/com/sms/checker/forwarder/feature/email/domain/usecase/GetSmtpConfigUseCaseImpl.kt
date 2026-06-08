package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal class GetSmtpConfigUseCaseImpl(
    private val repository: EmailRepository,
) : GetSmtpConfigUseCase {
    override suspend fun invoke(): List<SmtpEmailModel> {
        return repository.getSmtpList()
    }
}