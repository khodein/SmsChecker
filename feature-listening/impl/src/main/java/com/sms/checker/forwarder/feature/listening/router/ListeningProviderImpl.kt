package com.sms.checker.forwarder.feature.listening.router

import androidx.compose.material3.ExperimentalMaterial3Api
import com.sms.checker.forwarder.feature.listening.presentation.dialog.ListeningWarningKey
import com.sms.checker.forwarder.feature.listening.presentation.dialog.ListeningWarningRoute
import com.sms.checker.forwarder.feature.listening.presentation.route.ListeningListKey
import com.sms.checker.forwarder.feature.listening.presentation.route.ListeningListRoute
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.ListeningListViewModel
import com.sms.checker.forwarder.framework.BottomSheetSceneStrategy
import com.sms.checker.forwarder.framework.router.EntryProviderInstaller
import com.sms.checker.forwarder.framework.router.Router
import org.koin.compose.viewmodel.koinViewModel

internal class ListeningProviderImpl : Router.Provider {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun invoke(): EntryProviderInstaller = {
        entry<ListeningListKey> {
            val viewModel = koinViewModel<ListeningListViewModel>()
            ListeningListRoute(viewModel = viewModel)
        }
        entry<ListeningWarningKey>(
            metadata = BottomSheetSceneStrategy.bottomSheet()
        ) { key ->
            ListeningWarningRoute(
                title = key.title,
                description = key.description,
            )
        }
    }
}