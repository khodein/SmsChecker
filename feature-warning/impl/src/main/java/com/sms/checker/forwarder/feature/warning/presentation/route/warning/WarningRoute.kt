package com.sms.checker.forwarder.feature.warning.presentation.route.warning

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.WarningScreen
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.WarningViewModel

@Composable
internal fun WarningRoute(
    viewModel: WarningViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action

    WarningScreen(
        state = state,
        action = action,
    )
}
