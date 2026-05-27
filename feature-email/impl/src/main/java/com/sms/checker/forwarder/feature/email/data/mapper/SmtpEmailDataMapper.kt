package com.sms.checker.forwarder.feature.email.data.mapper

import com.sms.checker.forwarder.feature.email.db.entity.SmtpEmailEntity
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel

internal class SmtpEmailDataMapper {

    fun toModel(entity: SmtpEmailEntity): SmtpEmailModel = SmtpEmailModel(
        id = entity.id,
        name = entity.name,
        host = entity.host,
        port = entity.port,
        username = entity.username,
        password = entity.password,
        fromEmail = entity.fromEmail,
        fromName = entity.fromName,
        sslEnabled = entity.sslEnabled,
        startTlsEnabled = entity.startTlsEnabled,
    )

    fun toEntity(model: SmtpEmailModel): SmtpEmailEntity = SmtpEmailEntity(
        id = model.id ?: 0L,
        name = model.name,
        host = model.host,
        port = model.port,
        username = model.username,
        password = model.password,
        fromEmail = model.fromEmail,
        fromName = model.fromName,
        sslEnabled = model.sslEnabled,
        startTlsEnabled = model.startTlsEnabled,
    )
}
