package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

interface SendSmtpMessageUseCase {
    suspend operator fun invoke(
        message: String,
        model: SmtpEmailModel,
    )
    suspend operator fun invoke(
        message: String,
        smtpId: Long,
    )
}