package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.DefaultButtonWidget

@Composable
internal fun ListeningBottomBarWidget(
    modifier: Modifier = Modifier,
    state: ListeningBottomBarState,
    action: ListeningBottomBarAction,
) {
    DefaultButtonWidget(
        onClick = action.onClickForward,
        caption = state.caption,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.top()
            )
            .navigationBarsPadding()
            .padding(paddingValues = SmsCheckerTheme.padding.allMedium()),
    )
}