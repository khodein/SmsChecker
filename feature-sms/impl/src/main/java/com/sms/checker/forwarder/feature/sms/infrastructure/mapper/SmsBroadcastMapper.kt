package com.sms.checker.forwarder.feature.sms.infrastructure.mapper

import android.telephony.SmsMessage
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel
import com.sms.checker.forwarder.framework.tools.time.LocalDateTimeFormatter

internal class SmsBroadcastMapper {

    // SMS longer than 160 characters arrives as multiple parts in one broadcast intent
    fun toModel(messages: List<SmsMessage>): SmsModel {
        val first = messages.first()
        return SmsModel(
            sender = first.originatingAddress.orEmpty(),
            body = messages.joinToString("") { it.messageBody.orEmpty() },
            dateFormatter = LocalDateTimeFormatter.of(first.timestampMillis),
        )
    }
}