package com.sms.checker.forwarder.feature.settings.router

import com.sms.checker.forwarder.feature.settings.presentation.route.SettingsListKey
import com.sms.checker.forwarder.feature.settings.presentation.route.SettingsListRoute
import com.sms.checker.forwarder.feature.settings.presentation.screen.list.SettingsListViewModel
import com.sms.checker.forwarder.framework.router.EntryProviderInstaller
import com.sms.checker.forwarder.framework.router.NavTransition
import com.sms.checker.forwarder.framework.router.Router
import com.sms.checker.forwarder.framework.router.navTransitionMetadata
import org.koin.compose.viewmodel.koinViewModel

internal class SettingsProviderImpl : Router.Provider {
    override fun invoke(): EntryProviderInstaller = {
        entry<SettingsListKey>(
            metadata = navTransitionMetadata(NavTransition.SLIDE_HORIZONTAL)
        ) {
            val viewModel = koinViewModel<SettingsListViewModel>()
            SettingsListRoute(viewModel = viewModel)
        }
    }
}
