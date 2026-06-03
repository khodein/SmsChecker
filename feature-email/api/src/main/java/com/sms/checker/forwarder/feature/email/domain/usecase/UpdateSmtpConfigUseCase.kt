package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

interface UpdateSmtpConfigUseCase {
    suspend operator fun invoke(model: SmtpEmailModel): Long
}