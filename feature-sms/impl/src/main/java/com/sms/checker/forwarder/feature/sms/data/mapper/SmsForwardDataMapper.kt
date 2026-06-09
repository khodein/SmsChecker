package com.sms.checker.forwarder.feature.sms.data.mapper

import com.sms.checker.forwarder.feature.sms.db.entity.SmsForwardEntity
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardModel
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardStatus
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardType

internal class SmsForwardDataMapper {

    fun toModel(entity: SmsForwardEntity): SmsForwardModel = SmsForwardModel(
        id = entity.id,
        smsId = entity.smsId,
        type = SmsForwardType.valueOf(entity.type),
        configId = entity.configId,
        status = SmsForwardStatus.valueOf(entity.status),
        error = entity.error,
        attemptedAt = entity.attemptedAt,
    )

    fun toEntity(model: SmsForwardModel): SmsForwardEntity = SmsForwardEntity(
        id = model.id ?: 0,
        smsId = model.smsId,
        type = model.type.name,
        configId = model.configId,
        status = model.status.name,
        error = model.error,
        attemptedAt = model.attemptedAt,
    )
}