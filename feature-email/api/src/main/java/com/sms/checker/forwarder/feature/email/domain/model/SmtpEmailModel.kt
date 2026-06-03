package com.sms.checker.forwarder.feature.email.domain.model

data class SmtpEmailModel(
    val id: Long? = null,
    val name: String,
    val server: SmtpEmailServerModel,
    val user: SmtpEmailUserModel,
    val from: SmtpEmailFromModel,
    val recipient: SmtpEmailRecipientModel,
)
