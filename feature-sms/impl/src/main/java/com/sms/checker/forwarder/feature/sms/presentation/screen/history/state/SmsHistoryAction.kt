package com.sms.checker.forwarder.feature.sms.presentation.screen.history.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.state.SmsHistoryListAction
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.state.SmsHistoryTopBarAction
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.state.SmsHistoryWarningAction

@Immutable
internal data class SmsHistoryAction(
    val listAction: SmsHistoryListAction,
    val topBarAction: SmsHistoryTopBarAction,
    val warningAction: SmsHistoryWarningAction,
)
