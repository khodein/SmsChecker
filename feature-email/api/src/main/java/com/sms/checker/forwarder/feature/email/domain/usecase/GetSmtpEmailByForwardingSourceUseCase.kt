package com.sms.checker.forwarder.feature.email.domain.usecase

import com.sms.checker.forwarder.feature.email.domain.model.ForwardingSource
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

interface GetSmtpEmailByForwardingSourceUseCase {

    suspend operator fun invoke(source: ForwardingSource): List<SmtpEmailModel>
}