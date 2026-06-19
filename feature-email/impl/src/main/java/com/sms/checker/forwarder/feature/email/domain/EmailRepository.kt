package com.sms.checker.forwarder.feature.email.domain

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import kotlinx.coroutines.flow.Flow

internal interface EmailRepository {

    val isSmtpLimitFlow: Flow<Boolean>

    suspend fun sendSmtpMessage(
        message: String,
        model: SmtpEmailModel
    )

    suspend fun getSmtpConfigById(id: Long): SmtpEmailModel

    suspend fun setSmtpConfig(model: SmtpEmailModel): Long

    suspend fun updateSmtpConfig(model: SmtpEmailModel): Long

    suspend fun getSmtpList(): List<SmtpEmailModel>

    suspend fun getEnabledSmtpList(): List<SmtpEmailModel>

    suspend fun getEnabledSmtpIds(): List<Long>

    suspend fun deleteSmtpConfigById(id: Long)
}
