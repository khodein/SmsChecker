package com.sms.checker.forwarder.feature.dev

import com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.management.key.DevColorPaletteKey
import com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.route.DevColorPaletteRoute
import com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.screen.DevColorPaletteViewModel
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.key.DevListKey
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.mapper.DevListMapper
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.route.DevListRoute
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.screen.DevListViewModel
import com.sms.checker.forwarder.router.EntryProviderInstaller
import com.sms.checker.forwarder.router.NavTransition
import com.sms.checker.forwarder.router.Router
import com.sms.checker.forwarder.router.navTransitionMetadata
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

interface DevRouter {
    fun gotoDevList()
    fun gotoDevColorPalette()
}

object DevModule {

    fun get() = module {
        viewModelOf(::DevListViewModel)
        viewModelOf(::DevColorPaletteViewModel)
        singleOf(::DevListMapper)
        single<DevRouter> { DevRouterImpl(get()) }
        single { DevProviderImpl(get()) } bind Router.Provider::class
    }

    private class DevRouterImpl(private val router: Router) : DevRouter {
        override fun gotoDevList() {
            router.goTo(DevListKey)
        }

        override fun gotoDevColorPalette() {
            router.goTo(DevColorPaletteKey)
        }
    }

    private class DevProviderImpl(
        private val router: Router,
    ) : Router.Provider {
        override fun invoke(): EntryProviderInstaller = {
            entry<DevListKey> {
                val viewModel = koinViewModel<DevListViewModel>()
                DevListRoute(
                    viewModel = viewModel,
                )
            }
            entry<DevColorPaletteKey>(
                metadata = navTransitionMetadata(NavTransition.SLIDE_HORIZONTAL),
            ) {
                val viewModel = koinViewModel<DevColorPaletteViewModel>()
                DevColorPaletteRoute(
                    viewModel = viewModel,
                )
            }
        }
    }
}
