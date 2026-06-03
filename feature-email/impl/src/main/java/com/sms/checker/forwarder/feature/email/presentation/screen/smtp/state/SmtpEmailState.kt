package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.state.SmtpEmailFromState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state.SmtpEmailServerState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarState
import com.sms.checker.forwarder.framework.UiState
import com.sms.checker.forwarder.framework.Status

@Immutable
internal data class SmtpEmailState(
    override val status: Status,
    val smtpBottomBarState: SmtpBottomBarState,
    val smtpTopBarState: SmtpTopBarState,
    val nameState: SmtpEmailNameState,
    val serverState: SmtpEmailServerState,
    val fromState: SmtpEmailFromState,
    val recipientState: SmtpEmailRecipientState,
    val testState: SmtpEmailTestState?,
) : UiState()