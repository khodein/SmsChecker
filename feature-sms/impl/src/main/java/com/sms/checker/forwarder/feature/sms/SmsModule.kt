package com.sms.checker.forwarder.feature.sms

import com.sms.checker.forwarder.feature.sms.data.SmsRepositoryImpl
import com.sms.checker.forwarder.feature.sms.data.mapper.SmsDataMapper
import com.sms.checker.forwarder.feature.sms.data.mapper.SmsForwardDataMapper
import com.sms.checker.forwarder.feature.sms.delegate.SmsBroadcastDelegate
import com.sms.checker.forwarder.feature.sms.delegate.SmsBroadcastDelegateImpl
import com.sms.checker.forwarder.feature.sms.delegate.mapper.SmsBroadcastMapper
import com.sms.checker.forwarder.feature.sms.domain.SmsRepository
import com.sms.checker.forwarder.feature.sms.domain.usecase.GetSmsByIdUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.GetSmsByIdUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.SetSmsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.SetSmsUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.UpdateSmsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.UpdateSmsUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.GetLastSmsWithForwardsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.GetLastSmsWithForwardsUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.GetSmsPageWithForwardsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.GetSmsPageWithForwardsUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.ObserveLastSmsWithForwardsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.ObserveLastSmsWithForwardsUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.SetSmsForwardUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.SetSmsForwardUseCaseImpl
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.UpdateSmsForwardUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.UpdateSmsForwardUseCaseImpl
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.SmsHistoryViewModel
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.SmsHistoryListBlock
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.mapper.SmsHistoryListMapper
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.SmsHistoryTopBarBlock
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.mapper.SmsHistoryTopBarMapper
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.mapper.SmsHistoryMapper
import com.sms.checker.forwarder.feature.sms.router.SmsProviderImpl
import com.sms.checker.forwarder.feature.sms.router.SmsRouter
import com.sms.checker.forwarder.feature.sms.router.SmsRouterImpl
import com.sms.checker.forwarder.framework.router.Router
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

object SmsModule {

    fun get() = module {
        // presentation
        viewModelOf(::SmsHistoryViewModel)
        singleOf(::SmsHistoryMapper)

        //blocks
        factoryOf(::SmsHistoryListBlock)
        factoryOf(::SmsHistoryTopBarBlock)

        //mapper
        singleOf(::SmsHistoryListMapper)
        singleOf(::SmsHistoryTopBarMapper)

        // domain
        singleOf(::SetSmsUseCaseImpl) bind SetSmsUseCase::class
        singleOf(::UpdateSmsUseCaseImpl) bind UpdateSmsUseCase::class
        singleOf(::GetSmsByIdUseCaseImpl) bind GetSmsByIdUseCase::class
        singleOf(::GetSmsPageWithForwardsUseCaseImpl) bind GetSmsPageWithForwardsUseCase::class
        singleOf(::GetLastSmsWithForwardsUseCaseImpl) bind GetLastSmsWithForwardsUseCase::class
        singleOf(::ObserveLastSmsWithForwardsUseCaseImpl) bind ObserveLastSmsWithForwardsUseCase::class
        singleOf(::SetSmsForwardUseCaseImpl) bind SetSmsForwardUseCase::class
        singleOf(::UpdateSmsForwardUseCaseImpl) bind UpdateSmsForwardUseCase::class

        // data
        singleOf(::SmsRepositoryImpl) bind SmsRepository::class
        singleOf(::SmsDataMapper)
        singleOf(::SmsForwardDataMapper)

        // delegate
        factoryOf(::SmsBroadcastDelegateImpl) bind SmsBroadcastDelegate::class
        singleOf(::SmsBroadcastMapper)

        // navigation
        singleOf(::SmsRouterImpl) bind SmsRouter::class
        singleOf(::SmsProviderImpl) bind Router.Provider::class
    }
}
