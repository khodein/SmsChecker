package com.sms.checker.forwarder.feature.email.infrastructure

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

interface SmtpEmailDelegate {
    suspend fun getSmtpConfigList(): List<Long>
}