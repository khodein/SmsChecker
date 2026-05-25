package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarState
import com.sms.checker.forwarder.framework.BaseUiState
import com.sms.checker.forwarder.framework.Status

@Immutable
internal data class SmtpEmailState(
    override val status: Status,
    val smtpBottomBarState: SmtpBottomBarState,
    val smtpTopBarState: SmtpTopBarState,
) : BaseUiState()