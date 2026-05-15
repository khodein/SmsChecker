package com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.BaseUiState
import com.sms.checker.forwarder.framework.Status

@Immutable
internal data class DevListState(
    override val status: Status,
    val items: List<DevListItemState>
) : BaseUiState()