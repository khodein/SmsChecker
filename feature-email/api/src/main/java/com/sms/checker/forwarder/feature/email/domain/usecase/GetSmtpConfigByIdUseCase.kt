package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

interface GetSmtpConfigByIdUseCase {
    suspend operator fun invoke(id: Long): SmtpEmailModel
}