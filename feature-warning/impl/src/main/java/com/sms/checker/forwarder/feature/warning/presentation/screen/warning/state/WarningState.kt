package com.sms.checker.forwarder.feature.warning.presentation.screen.warning.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.framework.UiState

@Immutable
internal data class WarningState(
    override val status: Status = Status.SUCCESS,
    val title: String,
    val description: String,
) : UiState(status = status)
