package com.sms.checker.forwarder.feature.email.data.mapper

import com.sms.checker.forwarder.feature.email.db.entity.SmtpEmailEntity
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailFromModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailRecipientModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailServerModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailUserModel
import java.util.Properties
import javax.mail.Session

internal class SmtpEmailDataMapper {

    fun toModel(entity: SmtpEmailEntity): SmtpEmailModel = SmtpEmailModel(
        id = entity.id,
        name = entity.name,
        server = SmtpEmailServerModel(
            host = entity.host,
            port = entity.port,
        ),
        user = SmtpEmailUserModel(
            name = entity.username,
            password = entity.password,
        ),
        from = SmtpEmailFromModel(
            email = entity.senderEmail,
            name = entity.senderName,
        ),
        recipient = SmtpEmailRecipientModel(
            email = entity.fromEmail,
            subject = entity.fromName,
        ),
    )

    fun toEntity(model: SmtpEmailModel): SmtpEmailEntity = SmtpEmailEntity(
        id = model.id ?: 0L,
        name = model.name,
        host = model.server.host,
        port = model.server.port,
        username = model.user.name,
        password = model.user.password,
        senderEmail = model.from.email,
        senderName = model.from.name,
        fromEmail = model.recipient.email,
        fromName = model.recipient.subject,
    )
}
