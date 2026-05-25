package com.sms.checker.forwarder.feature.email.domain

import com.sms.checker.forwarder.feature.email.domain.model.ForwardingSource
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal interface EmailRepository {

    suspend fun toggleForwardingSource(id: Long, source: ForwardingSource)

    suspend fun getByForwardingSource(source: ForwardingSource): List<SmtpEmailModel>
}