package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal class GetSmtpConfigByIdUseCaseImpl(
    private val emailRepository: EmailRepository,
) : GetSmtpConfigByIdUseCase {
    override suspend fun invoke(id: Long): SmtpEmailModel {
        return emailRepository.getSmtpConfigById(id)
    }
}