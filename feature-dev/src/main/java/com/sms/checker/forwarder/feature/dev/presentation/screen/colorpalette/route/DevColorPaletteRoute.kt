package com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.screen.DevColorPaletteScreen
import com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.screen.DevColorPaletteViewModel

@Composable
internal fun DevColorPaletteRoute(
    viewModel: DevColorPaletteViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action
    DevColorPaletteScreen(
        state = state,
        action = action
    )
}