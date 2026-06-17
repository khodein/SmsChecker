package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.state.SmsHistoryTopBarAction
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.state.SmsHistoryTopBarState
import com.sms.checker.forwarder.framework.uikit.TopAppBarWidget

@Composable
internal fun SmsHistoryTopBarWidget(
    modifier: Modifier = Modifier,
    state: SmsHistoryTopBarState,
    action: SmsHistoryTopBarAction
) {
    TopAppBarWidget(
        modifier = modifier.fillMaxWidth(),
        title = state.title,
        onClickBackPressed = action.onClickBack,
    )
}