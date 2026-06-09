package com.sms.checker.forwarder.feature.sms.domain.model

data class SmsForwardModel(
    val id: Long? = null,
    val smsId: Long,
    val type: SmsForwardType,
    val configId: Long,
    val status: SmsForwardStatus,
    val error: String? = null,
    val attemptedAt: Long,
)