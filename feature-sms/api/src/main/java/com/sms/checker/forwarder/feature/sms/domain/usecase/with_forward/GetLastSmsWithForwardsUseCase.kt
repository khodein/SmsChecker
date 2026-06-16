package com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward

import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel

interface GetLastSmsWithForwardsUseCase {
    suspend operator fun invoke(count: Int): List<SmsWithForwardsModel>
}
