package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.AppSnackBarVisuals
import com.sms.checker.forwarder.framework.uikit.DefaultButtonWidget
import com.sms.checker.forwarder.framework.uikit.SnackBarPosition
import com.sms.checker.forwarder.framework.uikit.SnackBarType
import com.sms.checker.forwarder.framework.uikit.showAppSnackBar

internal suspend fun SmtpBottomBarEvent.onEvent(
    snackbarHostState: SnackbarHostState,
) {
    val visuals = AppSnackBarVisuals(
        position = SnackBarPosition.Top,
        message = message,
        type = when (type) {
            SmtpBottomBarEvent.Type.Success -> SnackBarType.Success
            SmtpBottomBarEvent.Type.Error -> SnackBarType.Error
        }
    )
    snackbarHostState.showAppSnackBar(visuals)
}

@Composable
internal fun SmtpBottomBarWidget(
    modifier: Modifier = Modifier,
    state: SmtpBottomBarState,
    action: SmtpBottomBarAction,
) {
    DefaultButtonWidget(
        onClick = action.onClickAdd,
        caption = state.caption,
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