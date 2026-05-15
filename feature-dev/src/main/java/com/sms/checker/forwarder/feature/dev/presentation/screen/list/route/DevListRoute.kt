package com.sms.checker.forwarder.feature.dev.presentation.screen.list.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.DevListAction
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen.mapper.DevListMapper
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.screen.DevListScreen
import com.sms.checker.forwarder.feature.dev.presentation.screen.list.screen.DevListViewModel

@Composable
internal fun DevListRoute(
    viewModel: DevListViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action
    DevListScreen(
        state = state,
        action = action,
    )
}