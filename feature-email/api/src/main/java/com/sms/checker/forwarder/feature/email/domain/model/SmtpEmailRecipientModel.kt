package com.sms.checker.forwarder.feature.email.domain.model

data class SmtpEmailRecipientModel(
    val email: String,
    val subject: String,
)