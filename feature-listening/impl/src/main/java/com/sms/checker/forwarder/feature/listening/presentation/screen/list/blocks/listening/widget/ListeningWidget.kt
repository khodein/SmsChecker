package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningEvent
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.warning.widget.WarningUi
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.SwitchWidget
import org.koin.compose.koinInject

private const val LISTENING_NOTIFICATION_WARNING_KEY = "LISTENING_NOTIFICATION_WARNING_KEY"
private const val LISTENING_NOTIFICATION_WARNING_CONTENT_TYPE =
    "LISTENING_NOTIFICATION_WARNING_CONTENT_TYPE"

internal suspend fun ListeningEvent.onEvent(
    lazyListState: LazyListState,
    snackbarHostState: SnackbarHostState
) {
    when (this) {
        is ListeningEvent.ScrollTop -> lazyListState.animateScrollToItem(0)
    }
}

@Composable
internal fun ListeningWidget(
    modifier: Modifier = Modifier,
    state: ListeningState,
    action: ListeningAction,
) {
    Row(
        modifier = modifier
            .padding(paddingValues = SmsCheckerTheme.padding.horizontalMedium())
            .padding(bottom = SmsCheckerTheme.padding.medium()),
        horizontalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.medium())
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(24.dp),
            tint = if (state.isListening) {
                SmsCheckerTheme.color.primary
            } else {
                SmsCheckerTheme.color.onSurfaceVariant
            },
            imageVector = Icons.Default.Email,
            contentDescription = "Listening Switch Icon"
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.small())
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.title,
                style = SmsCheckerTheme.typography.bodyLarge,
                color = SmsCheckerTheme.color.onSurface
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.description,
                style = SmsCheckerTheme.typography.bodyMedium,
                color = SmsCheckerTheme.color.onSurfaceVariant
            )
        }
        SwitchWidget(
            modifier = Modifier.align(Alignment.CenterVertically),
            isValue = state.isListening,
            onChangeValue = action.onClickListening
        )
    }
}

internal fun LazyListScope.notificationItem(
    notificationState: ListeningState.NotificationState?,
    onClickWarning: () -> Unit,
) {
    notificationState?.let {
        item(
            key = LISTENING_NOTIFICATION_WARNING_KEY,
            contentType = LISTENING_NOTIFICATION_WARNING_CONTENT_TYPE,
        ) {
            val warningUi: WarningUi = koinInject()
            warningUi.WarningNotificationContent(
                title = it.title,
                description = it.description,
                onClick = onClickWarning,
            )
        }
    }
}
