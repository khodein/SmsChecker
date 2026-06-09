package com.sms.checker.forwarder.feature.sms

import com.sms.checker.forwarder.feature.sms.data.SmsRepositoryImpl
import com.sms.checker.forwarder.feature.sms.data.mapper.SmsDataMapper
import com.sms.checker.forwarder.feature.sms.data.mapper.SmsForwardDataMapper
import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.infrastructure.SmsBroadcastDelegate
import com.sms.checker.forwarder.feature.sms.domain.usecase.GetSmsByIdUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.GetSmsByIdUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.GetSmsPageWithForwardsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.GetSmsPageWithForwardsUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.SetSmsForwardUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.SetSmsForwardUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.UpdateSmsForwardUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.UpdateSmsForwardUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.SetSmsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.SetSmsUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.UpdateSmsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.UpdateSmsUseCaseImpl
import com.sms.checker.forwarder.feature.sms.infrastructure.SmsBroadcastDelegateImpl
import com.sms.checker.forwarder.feature.sms.infrastructure.mapper.SmsBroadcastMapper
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object SmsModule {

    fun get() = module {
        singleOf(::SmsRepositoryImpl) bind SmsRepository::class
        singleOf(::SmsDataMapper)
        singleOf(::SmsForwardDataMapper)
        singleOf(::SmsBroadcastMapper)

        singleOf(::SetSmsUseCaseImpl) bind SetSmsUseCase::class
        singleOf(::UpdateSmsUseCaseImpl) bind UpdateSmsUseCase::class
        singleOf(::GetSmsByIdUseCaseImpl) bind GetSmsByIdUseCase::class
        singleOf(::GetSmsPageWithForwardsUseCaseImpl) bind GetSmsPageWithForwardsUseCase::class
        singleOf(::SetSmsForwardUseCaseImpl) bind SetSmsForwardUseCase::class
        singleOf(::UpdateSmsForwardUseCaseImpl) bind UpdateSmsForwardUseCase::class

        factoryOf(::SmsBroadcastDelegateImpl) bind SmsBroadcastDelegate::class
    }
}