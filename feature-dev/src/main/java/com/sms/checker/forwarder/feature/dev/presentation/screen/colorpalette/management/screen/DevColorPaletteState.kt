package com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.management.screen

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.BaseUiState
import com.sms.checker.forwarder.framework.Status

@Immutable
internal data class DevColorPaletteState(
    override val status: Status,
) : BaseUiState()