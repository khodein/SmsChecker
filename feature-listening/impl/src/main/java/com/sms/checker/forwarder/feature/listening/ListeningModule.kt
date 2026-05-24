package com.sms.checker.forwarder.feature.listening

import com.sms.checker.forwarder.feature.listening.data.ListeningRepositoryImpl
import com.sms.checker.forwarder.feature.listening.domain.usecase.GetListeningObserveUseCase
import com.sms.checker.forwarder.feature.listening.domain.ListeningRepository
import com.sms.checker.forwarder.feature.listening.domain.usecase.GetListeningUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.StartListeningUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.StopListeningUseCase
import com.sms.checker.forwarder.feature.listening.infrastructure.management.ListeningNotificationDelegate
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.mapper.ListeningBottomBarMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.mapper.ListeningMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.mapper.ListeningToolbarMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.mapper.ListeningListMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.ListeningListViewModel
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.ListeningBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.ListeningBottomBarBlock
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.ListeningToolbarBlock
import com.sms.checker.forwarder.feature.listening.router.ListeningProviderImpl
import com.sms.checker.forwarder.feature.listening.router.ListeningRouter
import com.sms.checker.forwarder.feature.listening.router.ListeningRouterImpl
import com.sms.checker.forwarder.router.Router
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

        //block
        factoryOf(::ListeningBlock)
        factoryOf(::ListeningToolbarBlock)
        factoryOf(::ListeningBottomBarBlock)

        // domain
        factoryOf(::GetListeningObserveUseCase)
        factoryOf(::StartListeningUseCase)
        factoryOf(::StopListeningUseCase)
        factoryOf(::GetListeningUseCase)

        // data
        single<ListeningRepository> { ListeningRepositoryImpl(get()) }

        // infrastructure
        factoryOf(::ListeningNotificationDelegate)

        // navigation
        single<ListeningRouter> { ListeningRouterImpl(get()) }
        single { ListeningProviderImpl() } bind Router.Provider::class
    }
}
