package com.sms.checker.forwarder.feature.sms.presentation.route.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.SmsHistoryScreen
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.SmsHistoryViewModel

@Composable
internal fun SmsHistoryRoute(
    viewModel: SmsHistoryViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action

    SmsHistoryScreen(
        state = state,
        action = action,
    )
}
