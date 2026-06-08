package com.sms.checker.forwarder.feature.email.domain

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal interface EmailRepository {

    suspend fun sendSmtpMessage(
        message: String,
        model: SmtpEmailModel
    )

    suspend fun getSmtpConfigById(id: Long): SmtpEmailModel

    suspend fun setSmtpConfig(model: SmtpEmailModel): Long

    suspend fun updateSmtpConfig(model: SmtpEmailModel): Long

    suspend fun getSmtpList(): List<SmtpEmailModel>
}
