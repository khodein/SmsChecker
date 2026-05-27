package com.sms.checker.forwarder.feature.email.domain.model

data class SmtpEmailModel(
    val id: Long? = null,
    val name: String,
    val host: String,
    val port: String,
    val username: String,
    val password: String,
    val fromEmail: String,
    val fromName: String,
    val sslEnabled: Boolean,
    val startTlsEnabled: Boolean,
)
