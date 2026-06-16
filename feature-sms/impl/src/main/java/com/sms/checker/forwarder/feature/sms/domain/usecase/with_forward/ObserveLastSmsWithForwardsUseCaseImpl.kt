package com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward

import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel
import kotlinx.coroutines.flow.Flow

internal class ObserveLastSmsWithForwardsUseCaseImpl(
    private val repository: SmsRepository,
) : ObserveLastSmsWithForwardsUseCase {
    override fun invoke(count: Int): Flow<List<SmsWithForwardsModel>> {
        return repository.observeLastWithForwards(count)
    }
}
