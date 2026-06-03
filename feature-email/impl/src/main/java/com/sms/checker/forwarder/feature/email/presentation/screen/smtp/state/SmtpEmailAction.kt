package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.state.SmtpEmailNameAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.state.SmtpEmailFromAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.state.SmtpEmailRecipientAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.state.SmtpEmailServerAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.state.SmtpTopBarAction

@Immutable
internal data class SmtpEmailAction(
    val bottomBarAction: SmtpBottomBarAction,
    val topBarAction: SmtpTopBarAction,
    val nameAction: SmtpEmailNameAction,
    val fromAction: SmtpEmailFromAction,
    val recipientAction: SmtpEmailRecipientAction,
    val serverAction: SmtpEmailServerAction,
    val testAction: SmtpEmailTestAction,
)