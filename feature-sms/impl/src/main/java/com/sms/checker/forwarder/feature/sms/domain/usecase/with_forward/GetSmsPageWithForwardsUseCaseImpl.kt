package com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward

import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel

internal class GetSmsPageWithForwardsUseCaseImpl(
    private val repository: SmsRepository,
) : GetSmsPageWithForwardsUseCase {
    override suspend fun invoke(limit: Int, offset: Int): List<SmsWithForwardsModel> {
        return repository.getPageWithForwards(limit, offset)
    }
}