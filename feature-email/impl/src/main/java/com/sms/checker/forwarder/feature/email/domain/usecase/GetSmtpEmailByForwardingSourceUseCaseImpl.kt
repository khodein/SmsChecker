package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.model.ForwardingSource
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal class GetSmtpEmailByForwardingSourceUseCaseImpl(
    private val repository: EmailRepository,
) : GetSmtpEmailByForwardingSourceUseCase {

    override suspend fun invoke(source: ForwardingSource): List<SmtpEmailModel> {
        return repository.getByForwardingSource(source)
    }
}