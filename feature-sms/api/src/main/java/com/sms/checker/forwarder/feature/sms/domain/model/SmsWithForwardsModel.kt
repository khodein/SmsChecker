package com.sms.checker.forwarder.feature.sms.domain.model

data class SmsWithForwardsModel(
    val sms: SmsModel,
    val forwards: List<SmsForwardModel> = emptyList(),
) {
    val aggregateStatus: SmsForwardStatus
        get() = when {
            forwards.isEmpty() -> SmsForwardStatus.PENDING
            forwards.any { it.status == SmsForwardStatus.ERROR } -> SmsForwardStatus.ERROR
            forwards.all { it.status == SmsForwardStatus.SUCCESS } -> SmsForwardStatus.SUCCESS
            else -> SmsForwardStatus.PENDING
        }
}