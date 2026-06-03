package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.AppSnackBarVisuals
import com.sms.checker.forwarder.framework.uikit.DefaultButtonWidget
import com.sms.checker.forwarder.framework.uikit.LoadingWidget
import com.sms.checker.forwarder.framework.uikit.SnackBarPosition
import com.sms.checker.forwarder.framework.uikit.SnackBarType
import com.sms.checker.forwarder.framework.uikit.showAppSnackBar

internal suspend fun SmtpEmailTestEvent.onEvent(
    lazyListState: LazyListState,
    snackbarHostState: SnackbarHostState
) {
    when (this) {
        is SmtpEmailTestEvent.Notification -> {
            val visuals = AppSnackBarVisuals(
                message = message,
                position = SnackBarPosition.Top,
                type = if (isSuccess) {
                    SnackBarType.Success
                } else {
                    SnackBarType.Error
                }
            )
            snackbarHostState.showAppSnackBar(visuals)
        }

        is SmtpEmailTestEvent.Scroll -> {
            lazyListState.animateScrollToItem(lazyListState.layoutInfo.totalItemsCount - 1)
        }
    }
}

@Composable
internal fun SmtpEmailTestWidget(
    modifier: Modifier = Modifier,
    state: SmtpEmailTestState,
    action: SmtpEmailTestAction,
) {
    Column(
        modifier = modifier
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.all()
            )
            .padding(all = SmsCheckerTheme.padding.medium()),
        verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.small())
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.small())
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.extraSmall())
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.title,
                    style = SmsCheckerTheme.typography.titleLarge,
                    color = SmsCheckerTheme.color.onSurface
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = state.description,
                    style = SmsCheckerTheme.typography.bodyMedium,
                    color = SmsCheckerTheme.color.onSurfaceVariant
                )
            }
            when (state.status) {
                SmtpEmailTestState.TestStatus.Loading -> {
                    LoadingWidget(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(48.dp)
                    )
                }

                SmtpEmailTestState.TestStatus.Succuss -> {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(48.dp),
                        imageVector = Icons.Default.Done,
                        tint = SmsCheckerTheme.color.primary,
                        contentDescription = "Icon Success"
                    )
                }

                SmtpEmailTestState.TestStatus.Error -> {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(48.dp),
                        imageVector = Icons.Default.Close,
                        tint = SmsCheckerTheme.color.error,
                        contentDescription = "Icon Error"
                    )
                }

                else -> Unit
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = state.notice,
            style = SmsCheckerTheme.typography.bodySmall,
            color = SmsCheckerTheme.color.error
        )
        if (state.status == SmtpEmailTestState.TestStatus.Error || state.status == SmtpEmailTestState.TestStatus.Succuss) {
            DefaultButtonWidget(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                caption = state.buttonText,
                onClick = action.onClickReload
            )
        }
    }
}