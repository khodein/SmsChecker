package com.sms.checker.forwarder.feature.email.router

import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.email.presentation.route.smtp.SmtpEmailKey
import com.sms.checker.forwarder.feature.email.presentation.route.smtp.SmtpEmailRoute
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.SmtpEmailViewModel
import com.sms.checker.forwarder.framework.router.EntryProviderInstaller
import com.sms.checker.forwarder.framework.router.Router
import org.koin.compose.viewmodel.koinViewModel

internal class EmailProviderImpl : Router.Provider {
    override fun invoke(): EntryProviderInstaller = {
        entry<SmtpEmailKey> { key ->
            val viewModel = koinViewModel<SmtpEmailViewModel>().also {
                it.setSmtpId(key.id)
            }
            SmtpEmailRoute(
                modifier = Modifier,
                viewModel = viewModel
            )
        }
    }
}