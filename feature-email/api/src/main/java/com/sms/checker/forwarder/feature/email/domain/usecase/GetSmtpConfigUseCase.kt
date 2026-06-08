package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

interface GetSmtpConfigUseCase {
    suspend operator fun invoke(): List<SmtpEmailModel>
}