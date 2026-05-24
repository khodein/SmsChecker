package com.sms.checker.forwarder.feature.sms.domain.model

import com.sms.checker.forwarder.framework.tools.time.LocalDateTimeFormatter

data class SmsModel(
    val id: Long? = null,
    val sender: String,
    val body: String,
    val dateFormatter: LocalDateTimeFormatter,
    val forwardedTo: List<SmsForwardType> = emptyList(),
    val forwardStatus: SmsForwardStatus = SmsForwardStatus.PENDING,
)