package com.sms.checker.forwarder.feature.sms.router

import com.sms.checker.forwarder.feature.sms.presentation.route.history.SmsHistoryKey
import com.sms.checker.forwarder.feature.sms.presentation.route.history.SmsHistoryRoute
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.SmsHistoryViewModel
import com.sms.checker.forwarder.framework.router.EntryProviderInstaller
import com.sms.checker.forwarder.framework.router.NavTransition
import com.sms.checker.forwarder.framework.router.Router
import com.sms.checker.forwarder.framework.router.navTransitionMetadata
import org.koin.compose.viewmodel.koinViewModel

internal class SmsProviderImpl : Router.Provider {
    override fun invoke(): EntryProviderInstaller = {
        entry<SmsHistoryKey>(
            metadata = navTransitionMetadata(NavTransition.SLIDE_HORIZONTAL)
        ) {
            val viewModel = koinViewModel<SmsHistoryViewModel>()
            SmsHistoryRoute(viewModel = viewModel)
        }
    }
}
