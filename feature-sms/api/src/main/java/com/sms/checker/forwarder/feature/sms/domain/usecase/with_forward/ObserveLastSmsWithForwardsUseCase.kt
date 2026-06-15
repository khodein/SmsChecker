package com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward

import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel
import kotlinx.coroutines.flow.Flow

interface ObserveLastSmsWithForwardsUseCase {
    operator fun invoke(): Flow<List<SmsWithForwardsModel>>
}
