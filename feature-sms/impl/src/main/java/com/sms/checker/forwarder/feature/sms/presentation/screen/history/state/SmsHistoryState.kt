package com.sms.checker.forwarder.feature.sms.presentation.screen.history.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.state.SmsHistoryListState
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.state.SmsHistoryTopBarState
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.state.SmsHistoryWarningState
import com.sms.checker.forwarder.framework.UiState

@Immutable
internal data class SmsHistoryState(
    val listState: SmsHistoryListState,
    val topBarState: SmsHistoryTopBarState,
    val warningState: SmsHistoryWarningState,
) : UiState()
