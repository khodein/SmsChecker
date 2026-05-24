package com.sms.checker.forwarder.feature.sms.domain

import com.sms.checker.forwarder.feature.sms.domain.model.SmsModel

interface SmsRepository {
    suspend fun setSms(model: SmsModel): Long
}