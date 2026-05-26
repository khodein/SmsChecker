package com.sms.checker.forwarder.feature.email.presentation.screen.smtp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.widget.SmtpBottomBarWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.widget.SmtpEmailHostWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.widget.SmtpEmailNameWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.widget.SmtpTopBarWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.widget.SmtpEmailUserWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailItemState
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
            modifier = Modifier.padding(it),
            items = state.items
        )
    }
}

@Composable
private fun SmtpBottomBarContent(
    modifier: Modifier = Modifier,
    items: List<SmtpEmailItemState>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = SmsCheckerTheme.padding.verticalExtraSmall()
    ) {
        items(
            items = items,
            key = { it.id },
            contentType = { it.contentType },
        ) { item ->
            when (item) {
                is SmtpEmailItemState.NameState -> {
                    SmtpEmailNameWidget(
                        modifier = Modifier.fillMaxWidth(),
                        state = item.state
                    )
                }

                is SmtpEmailItemState.HostState -> {
                    SmtpEmailHostWidget(
                        modifier = Modifier.fillMaxWidth(),
                        state = item.state
                    )
                }

                is SmtpEmailItemState.UserState -> {
                    SmtpEmailUserWidget(
                        modifier = Modifier.fillMaxWidth(),
                        state = item.state
                    )
                }
            }
        }
    }
}

