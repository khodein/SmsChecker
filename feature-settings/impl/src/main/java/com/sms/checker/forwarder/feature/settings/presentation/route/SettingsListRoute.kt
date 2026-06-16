package com.sms.checker.forwarder.feature.settings.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.sms.checker.forwarder.feature.settings.presentation.screen.list.SettingsListScreen
import com.sms.checker.forwarder.feature.settings.presentation.screen.list.SettingsListViewModel

@Composable
internal fun SettingsListRoute(
    viewModel: SettingsListViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action

    SettingsListScreen(
        state = state,
        action = action,
    )
}
