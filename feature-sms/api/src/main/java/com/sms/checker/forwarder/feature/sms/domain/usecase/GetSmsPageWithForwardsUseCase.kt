package com.sms.checker.forwarder.feature.sms.domain.usecase

import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel

interface GetSmsPageWithForwardsUseCase {
    suspend operator fun invoke(limit: Int, offset: Int): List<SmsWithForwardsModel>
}