package com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward

import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel

internal class GetLastSmsWithForwardsUseCaseImpl(
    private val repository: SmsRepository,
) : GetLastSmsWithForwardsUseCase {
    override suspend fun invoke(): List<SmsWithForwardsModel> {
        return repository.getLastWithForwards()
    }
}
