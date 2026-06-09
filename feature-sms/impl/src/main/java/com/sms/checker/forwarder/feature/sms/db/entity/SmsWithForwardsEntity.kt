package com.sms.checker.forwarder.feature.sms.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SmsWithForwardsEntity(
    @Embedded
    val sms: SmsEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sms_id",
    )
    val forwards: List<SmsForwardEntity>,
)