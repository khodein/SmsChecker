package com.sms.checker.forwarder.feature.listening.router

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.route.ListeningListKey
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.route.ListeningListRoute
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.ListeningListViewModel
import com.sms.checker.forwarder.framework.OnLifecycle
import com.sms.checker.forwarder.framework.router.EntryProviderInstaller
import com.sms.checker.forwarder.framework.router.NavTransition
import com.sms.checker.forwarder.framework.router.Router
import com.sms.checker.forwarder.framework.router.navTransitionMetadata
import org.koin.compose.viewmodel.koinViewModel

internal class ListeningProviderImpl : Router.Provider {
    override fun invoke(): EntryProviderInstaller = {
        entry<ListeningListKey>() {
            val viewModel = koinViewModel<ListeningListViewModel>()
            ListeningListRoute(viewModel = viewModel)
        }
    }
}