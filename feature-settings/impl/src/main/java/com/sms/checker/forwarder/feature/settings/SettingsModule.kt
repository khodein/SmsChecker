package com.sms.checker.forwarder.feature.settings

import com.sms.checker.forwarder.feature.settings.presentation.screen.list.SettingsListMapper
import com.sms.checker.forwarder.feature.settings.presentation.screen.list.SettingsListViewModel
import com.sms.checker.forwarder.feature.settings.router.SettingsProviderImpl
import com.sms.checker.forwarder.feature.settings.router.SettingsRouter
import com.sms.checker.forwarder.feature.settings.router.SettingsRouterImpl
import com.sms.checker.forwarder.framework.router.Router
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

object SettingsModule {

    fun get() = module {
        // presentation
        viewModelOf(::SettingsListViewModel)
        singleOf(::SettingsListMapper)

        // navigation
        singleOf(::SettingsRouterImpl) bind SettingsRouter::class
        singleOf(::SettingsProviderImpl) bind Router.Provider::class
    }
}
