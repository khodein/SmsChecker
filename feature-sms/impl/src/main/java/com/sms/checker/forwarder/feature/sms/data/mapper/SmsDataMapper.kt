package com.sms.checker.forwarder.feature.sms.data.mapper

import com.sms.checker.forwarder.feature.sms.db.entity.SmsEntity
import com.sms.checker.forwarder.feature.sms.db.entity.SmsWithForwardsEntity
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel
import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel
import com.sms.checker.forwarder.framework.tools.time.LocalDateTimeFormatter

internal class SmsDataMapper(
    private val smsForwardDataMapper: SmsForwardDataMapper,
) {

    fun toModel(entity: SmsEntity): SmsModel = SmsModel(
        id = entity.id,
        sender = entity.sender,
        body = entity.body,
        dateFormatter = LocalDateTimeFormatter.of(entity.timestamp),
    )

    fun toModel(entity: SmsWithForwardsEntity): SmsWithForwardsModel = SmsWithForwardsModel(
        sms = toModel(entity.sms),
        forwards = entity.forwards.map(smsForwardDataMapper::toModel),
    )

    fun toEntity(model: SmsModel): SmsEntity = SmsEntity(
        id = model.id ?: 0,
        sender = model.sender,
        body = model.body,
        timestamp = model.dateFormatter.toTimestamp(),
    )
}