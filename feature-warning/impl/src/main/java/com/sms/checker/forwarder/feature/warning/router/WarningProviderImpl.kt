package com.sms.checker.forwarder.feature.warning.router

import androidx.compose.material3.ExperimentalMaterial3Api
import com.sms.checker.forwarder.feature.warning.presentation.route.warning.WarningKey
import com.sms.checker.forwarder.feature.warning.presentation.route.warning.WarningRoute
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.WarningViewModel
import com.sms.checker.forwarder.framework.BottomSheetSceneStrategy
import com.sms.checker.forwarder.framework.router.EntryProviderInstaller
import com.sms.checker.forwarder.framework.router.Router
import org.koin.compose.viewmodel.koinViewModel

internal class WarningProviderImpl : Router.Provider {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun invoke(): EntryProviderInstaller = {
        entry<WarningKey>(
            metadata = BottomSheetSceneStrategy.bottomSheet(),
        ) {
            val viewModel = koinViewModel<WarningViewModel>()
            WarningRoute(viewModel = viewModel)
        }
    }
}
