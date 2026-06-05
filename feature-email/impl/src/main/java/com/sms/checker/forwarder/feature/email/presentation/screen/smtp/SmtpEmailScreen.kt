package com.sms.checker.forwarder.feature.email.presentation.screen.smtp

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.widget.SmtpBottomBarWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.widget.SmtpEmailNameWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.widget.SmtpEmailFromWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.widget.SmtpEmailRecipientWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.widget.SmtpEmailServerWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.widget.SmtpEmailTestWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.widget.SmtpTopBarWidget
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailState
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.AppSnackBarVisuals
import com.sms.checker.forwarder.framework.uikit.SnackBarPosition
import com.sms.checker.forwarder.framework.uikit.SnackBarType
import com.sms.checker.forwarder.framework.uikit.showAppSnackBar

internal suspend fun SmtpEmailEvent.onEvent(snackbarHostState: SnackbarHostState) {
    val visuals = AppSnackBarVisuals(
        position = SnackBarPosition.Top,
        message = message,
        type = if (isSuccess) {
            SnackBarType.Success
        } else {
            SnackBarType.Error
        }
    )
    snackbarHostState.showAppSnackBar(visuals)
}

@Composable
internal fun SmtpEmailScreen(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    state: SmtpEmailState,
    action: SmtpEmailAction,
) {
    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            SmtpTopBarWidget(
                modifier = Modifier,
                state = state.smtpTopBarState,
                action = action.topBarAction,
            )
        },
        bottomBar = {
            SmtpBottomBarWidget(
                modifier = Modifier,
                state = state.smtpBottomBarState,
                action = action.bottomBarAction,
            )
        }
    ) { paddingValues ->
        SmtpEmailContent(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = state,
            lazyListState = lazyListState,
            action = action
        )
    }
}

@Composable
private fun SmtpEmailContent(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    state: SmtpEmailState,
    action: SmtpEmailAction,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.extraSmall()),
        contentPadding = SmsCheckerTheme.padding.allExtraSmall()
    ) {
        item(key = "name", contentType = "name") {
            SmtpEmailNameWidget(
                modifier = Modifier.fillMaxWidth(),
                state = state.nameState,
                action = action.nameAction
            )
        }
        item(key = "server", contentType = "server") {
            SmtpEmailServerWidget(
                modifier = Modifier.fillMaxWidth(),
                state = state.serverState,
                action = action.serverAction,
            )
        }
        item(key = "from", contentType = "from") {
            SmtpEmailFromWidget(
                modifier = Modifier.fillMaxWidth(),
                state = state.fromState,
                action = action.fromAction,
            )
        }
        item(key = "recipient", contentType = "recipient") {
            SmtpEmailRecipientWidget(
                modifier = Modifier.fillMaxWidth(),
                state = state.recipientState,
                action = action.recipientAction
            )
        }
        state.testState?.let { testState ->
            item(key = "test", contentType = "test") {
                SmtpEmailTestWidget(
                    modifier = Modifier.fillMaxWidth(),
                    state = testState,
                    action = action.testAction
                )
            }
        }
    }
}