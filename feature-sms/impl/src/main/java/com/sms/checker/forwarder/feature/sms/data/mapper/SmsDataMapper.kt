package com.sms.checker.forwarder.feature.sms.data.mapper

import com.sms.checker.forwarder.feature.sms.db.entity.SmsEntity
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardStatus
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardType
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel
import com.sms.checker.forwarder.framework.tools.time.LocalDateTimeFormatter

internal class SmsDataMapper {

    fun toModel(entity: SmsEntity): SmsModel = SmsModel(
        id = entity.id,
        sender = entity.sender,
        body = entity.body,
        dateFormatter = LocalDateTimeFormatter.of(entity.timestamp),
        forwardedTo = entity.forwardedTo
            .split(",")
            .filter { it.isNotBlank() }
            .map { SmsForwardType.valueOf(it) },
        forwardStatus = SmsForwardStatus.valueOf(entity.forwardStatus),
    )

    fun toEntity(model: SmsModel): SmsEntity = SmsEntity(
        id = model.id ?: 0,
        sender = model.sender,
        body = model.body,
        timestamp = model.dateFormatter.toTimestamp(),
        forwardedTo = model.forwardedTo.joinToString(",") { it.name },
        forwardStatus = model.forwardStatus.name,
    )
}