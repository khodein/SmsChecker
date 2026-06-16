package com.sms.checker.forwarder.feature.settings.presentation.screen.list.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.UiState

@Immutable
internal data class SettingsListState(
    val title: String,
    val emptyTitle: String,
    val emptyDescription: String,
) : UiState()
