package com.sms.checker.forwarder.feature.listening

import com.sms.checker.forwarder.feature.listening.data.ListeningRepositoryImpl
import com.sms.checker.forwarder.feature.listening.delegate.management.notification.ListeningNotificationDelegate
import com.sms.checker.forwarder.feature.listening.delegate.management.sending.ListeningSendingDelegate
import com.sms.checker.forwarder.feature.listening.delegate.management.sending.facade.ListeningSendingFacade
import com.sms.checker.forwarder.feature.listening.delegate.management.sending.facade.ListeningSmtpFacade
import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository
import com.sms.checker.forwarder.feature.listening.domain.usecase.GetListeningUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.ObserveListeningUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.ObserveListeningUseCaseImpl
import com.sms.checker.forwarder.feature.listening.domain.usecase.StartListeningUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.StopListeningUseCase
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.ListeningListViewModel
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.ListeningBottomBarBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.mapper.ListeningBottomBarMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.ListeningConfigBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.mapper.ListeningConfigMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.ListeningHistoryBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.mapper.ListeningHistoryMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.ListeningBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.mapper.ListeningMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.toolbar.ListeningToolbarBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.toolbar.mapper.ListeningToolbarMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.mapper.ListeningListMapper
import com.sms.checker.forwarder.feature.listening.router.ListeningProviderImpl
import com.sms.checker.forwarder.feature.listening.router.ListeningRouter
import com.sms.checker.forwarder.feature.listening.router.ListeningRouterImpl
import com.sms.checker.forwarder.framework.router.Router
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

object ListeningModule {

    fun get() = module {
        // presentation
        viewModelOf(::ListeningListViewModel)
        singleOf(::ListeningListMapper)
        singleOf(::ListeningMapper)
        singleOf(::ListeningBottomBarMapper)
        singleOf(::ListeningToolbarMapper)
        singleOf(::ListeningConfigMapper)
        singleOf(::ListeningHistoryMapper)

        //block
        factoryOf(::ListeningBlock)
        factoryOf(::ListeningToolbarBlock)
        factoryOf(::ListeningBottomBarBlock)
        factoryOf(::ListeningConfigBlock)
        factoryOf(::ListeningHistoryBlock)

        // domain
        factoryOf(::StartListeningUseCase)
        factoryOf(::StopListeningUseCase)
        factoryOf(::GetListeningUseCase)
        singleOf(::ObserveListeningUseCaseImpl) bind ObserveListeningUseCase::class

        // data
        singleOf(::ListeningRepositoryImpl) bind ListeningRepository::class

        // delegate
        factoryOf(::ListeningNotificationDelegate)
        factoryOf(::ListeningSendingDelegate)
        factoryOf(::ListeningSmtpFacade) bind ListeningSendingFacade::class

        // navigation
        singleOf(::ListeningRouterImpl) bind ListeningRouter::class
        singleOf(::ListeningProviderImpl) bind Router.Provider::class
    }
}
