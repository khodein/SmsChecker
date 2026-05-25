package com.sms.checker.forwarder.feature.email.presentation.route.smtp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.SmtpEmailScreen
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.SmtpEmailViewModel

@Composable
internal fun SmtpEmailRoute(
    modifier: Modifier = Modifier,
    viewModel: SmtpEmailViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    SmtpEmailScreen(
        modifier = modifier,
        state = state,
    )
}