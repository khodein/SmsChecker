package com.sms.checker.forwarder.feature.email.router

import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.email.presentation.route.smtp.SmtpEmailKey
import com.sms.checker.forwarder.feature.email.presentation.route.smtp.SmtpEmailRoute
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.SmtpEmailViewModel
import com.sms.checker.forwarder.framework.router.EntryProviderInstaller
import com.sms.checker.forwarder.framework.router.NavTransition
import com.sms.checker.forwarder.framework.router.Router
import com.sms.checker.forwarder.framework.router.navTransitionMetadata
import io.ktor.http.parametersOf
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

internal class EmailProviderImpl : Router.Provider {
    override fun invoke(): EntryProviderInstaller = {
        entry<SmtpEmailKey>(
            metadata = navTransitionMetadata(NavTransition.SLIDE_HORIZONTAL)
        ) { key ->
            val viewModel = koinViewModel<SmtpEmailViewModel> {
                parametersOf(key.id)
            }
            SmtpEmailRoute(
                modifier = Modifier,
                viewModel = viewModel
            )
        }
    }
}