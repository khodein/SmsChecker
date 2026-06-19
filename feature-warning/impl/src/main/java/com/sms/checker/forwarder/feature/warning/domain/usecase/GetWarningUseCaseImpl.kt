package com.sms.checker.forwarder.feature.warning.domain.usecase

import com.sms.checker.forwarder.feature.warning.domain.WarningRepository
import com.sms.checker.forwarder.feature.warning.domain.model.WarningModel

internal class GetWarningUseCaseImpl(
    private val repository: WarningRepository,
) : GetWarningUseCase {
    override fun invoke(): WarningModel = repository.getWarning()
}
