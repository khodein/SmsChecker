package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

interface GetEnabledSmtpConfigUseCase {
    suspend operator fun invoke(): List<SmtpEmailModel>
}
