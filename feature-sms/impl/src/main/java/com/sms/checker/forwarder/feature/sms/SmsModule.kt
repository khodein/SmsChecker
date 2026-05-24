package com.sms.checker.forwarder.feature.sms

import com.sms.checker.forwarder.feature.sms.data.SmsRepositoryImpl
import com.sms.checker.forwarder.feature.sms.data.mapper.SmsDataMapper
import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.infrastructure.SmsBroadcastDelegate
import com.sms.checker.forwarder.feature.sms.domain.usecase.SetSmsUseCase
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
        singleOf(::SmsBroadcastMapper)
        factoryOf(::SetSmsUseCase)
        factoryOf(::SmsBroadcastDelegateImpl) bind SmsBroadcastDelegate::class
    }
}