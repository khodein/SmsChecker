package com.sms.checker.forwarder.feature.email.delegate

interface SmtpEmailDelegate {
    suspend fun getSmtpConfigList(): List<Long>
}