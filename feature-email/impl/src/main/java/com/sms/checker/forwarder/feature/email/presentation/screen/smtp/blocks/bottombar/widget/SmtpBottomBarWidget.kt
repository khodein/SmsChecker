package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.DefaultButtonWidget

@Composable
internal fun SmtpBottomBarWidget(
    modifier: Modifier = Modifier,
    state: SmtpBottomBarState,
) {
    DefaultButtonWidget(
        onClick = state.action.onClickAdd,
        caption = state.caption,
        isEnabled = state.isEnabled,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.top()
            )
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    )
}