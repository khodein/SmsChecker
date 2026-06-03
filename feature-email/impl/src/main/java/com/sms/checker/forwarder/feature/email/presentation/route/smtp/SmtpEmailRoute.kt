package com.sms.checker.forwarder.feature.email.presentation.route.smtp

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.SmtpEmailScreen
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.SmtpEmailViewModel
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.widget.onEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.widget.onEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.onEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailEvent
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.AppSnackBarVisuals
import com.sms.checker.forwarder.framework.uikit.SnackBarPosition
import com.sms.checker.forwarder.framework.uikit.SnackBarType
import com.sms.checker.forwarder.framework.uikit.showAppSnackBar
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun SmtpEmailRoute(
    modifier: Modifier = Modifier,
    viewModel: SmtpEmailViewModel,
) {
    val state by viewModel.viewState.collectAsState()
    val action = viewModel.action
    val snackbarHostState = SmsCheckerTheme.snackBarHostState
    val lazyListState = rememberLazyListState()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SmtpEmailEvent -> event.onEvent(snackbarHostState)
                is SmtpBottomBarEvent -> event.onEvent(snackbarHostState)
                is SmtpEmailTestEvent -> event.onEvent(
                    snackbarHostState = snackbarHostState,
                    lazyListState = lazyListState,
                )
            }
        }
    }
    SmtpEmailScreen(
        modifier = modifier,
        state = state,
        lazyListState = lazyListState,
        action = action,
    )
}