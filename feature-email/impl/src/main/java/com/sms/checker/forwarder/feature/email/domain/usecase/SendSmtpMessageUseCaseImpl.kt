package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.EmailRepository
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal class SendSmtpMessageUseCaseImpl(
    private val emailRepository: EmailRepository,
) : SendSmtpMessageUseCase {

    override suspend fun invoke(
        message: String,
        model: SmtpEmailModel
    ) {
        emailRepository.sendSmtpMessage(
            message = message,
            model = model,
        )
    }

    override suspend fun invoke(
        message: String,
        smtpId: Long
    ) {
        val model = emailRepository.getSmtpConfigById(smtpId)
        emailRepository.sendSmtpMessage(
            message = message,
            model = model,
        )
    }
}