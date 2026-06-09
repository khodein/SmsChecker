package com.sms.checker.forwarder.feature.listening.infrastructure.management.sending.facade

import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardStatus
import com.sms.checker.forwarder.feature.sms.domain.model.SmsForwardType
import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

interface ListeningSendingFacade {

    fun onCreate(provider: Provider)
    fun onDestroy()

    suspend fun send(model: SmsModel)

    interface Provider {
        suspend fun onResult(params: ResultParameters)
    }

    data class ResultParameters(
        val smsId: Long,
        val configId: Long,
        val type: SmsForwardType,
        val status: SmsForwardStatus,
        val error: String?,
    )
}