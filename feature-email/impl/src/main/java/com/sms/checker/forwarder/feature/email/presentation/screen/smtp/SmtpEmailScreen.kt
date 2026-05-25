package com.sms.checker.forwarder.feature.email.presentation.screen.smtp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.widget.SmtpBottomBarWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.widget.SmtpTopBarWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.DefaultButtonWidget
import com.sms.checker.forwarder.framework.uikit.TopAppBarWidget

@Composable
internal fun SmtpEmailScreen(
    modifier: Modifier = Modifier,
    state: SmtpEmailState,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SmtpTopBarWidget(
                modifier = Modifier,
                state = state.smtpTopBarState,
            )
        },
        bottomBar = {
            SmtpBottomBarWidget(
                modifier = Modifier,
                state = state.smtpBottomBarState,
            )
        }
    ) {
        SmtpBottomBarContent(
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
private fun SmtpBottomBarContent(
    modifier: Modifier = Modifier,
) {

}

