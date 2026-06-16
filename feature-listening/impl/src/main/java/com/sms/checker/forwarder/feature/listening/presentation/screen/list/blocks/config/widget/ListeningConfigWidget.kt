package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigEvent
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.AppSnackBarVisuals
import com.sms.checker.forwarder.framework.uikit.DefaultButtonWidget
import com.sms.checker.forwarder.framework.uikit.SnackBarPosition
import com.sms.checker.forwarder.framework.uikit.SnackBarType
import com.sms.checker.forwarder.framework.uikit.SwitchWidget
import com.sms.checker.forwarder.framework.uikit.showAppSnackBar

private const val LISTENING_CONFIG_KEY = "ListeningConfigKey"
private const val LISTENING_CONFIG_CONTENT_TYPE = "ListeningConfigContentType"
private const val LISTENING_CONFIG_APPEARANCE_ANIMATION_DURATION = 300

internal suspend fun ListeningConfigEvent.onEvent(
    snackbarHostState: SnackbarHostState,
) {
    when (this) {
        is ListeningConfigEvent.SnackBarEvent -> {
            val visuals = AppSnackBarVisuals(
                position = SnackBarPosition.Top,
                message = value,
                type = when (status) {
                    ListeningConfigEvent.Status.Info -> SnackBarType.Info
                    ListeningConfigEvent.Status.Error -> SnackBarType.Error
                    ListeningConfigEvent.Status.Success -> SnackBarType.Success
                }
            )
            snackbarHostState.showAppSnackBar(visuals)
        }
    }
}

internal fun LazyListScope.item(
    state: ListeningConfigState,
    action: ListeningConfigAction,
) {
    item(
        key = LISTENING_CONFIG_KEY,
        contentType = LISTENING_CONFIG_CONTENT_TYPE
    ) {
        var isVisible by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            isVisible = true
        }

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(
                animationSpec = tween(LISTENING_CONFIG_APPEARANCE_ANIMATION_DURATION)
            ) + expandVertically(
                animationSpec = tween(LISTENING_CONFIG_APPEARANCE_ANIMATION_DURATION)
            )
        ) {
            ListeningConfigWidget(
                modifier = Modifier,
                state = state,
                action = action,
            )
        }
    }
}

@Composable
internal fun ListeningConfigWidget(
    modifier: Modifier = Modifier,
    state: ListeningConfigState,
    action: ListeningConfigAction,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.all()
            )
            .padding(SmsCheckerTheme.padding.allNormal())
    ) {
        if (state.items.isEmpty()) {
            ListeningConfigEmptyWidget(
                title = state.title,
                caption = state.actionText,
                onClickEmpty = action.onClickEmpty
            )
        } else {
            ListeningConfigListWidget(
                title = state.title,
                items = state.items,
                action = action,
            )
        }
    }
}

@Composable
private fun ColumnScope.ListeningConfigListWidget(
    title: String,
    items: List<ListeningConfigState.Item>,
    action: ListeningConfigAction,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SmsCheckerTheme.padding.small())
            .align(Alignment.Start),
        textAlign = TextAlign.Start,
        text = title,
        style = SmsCheckerTheme.typography.titleMedium,
        color = SmsCheckerTheme.color.onSurface
    )
    items.forEachIndexed { index, item ->
        ListeningConfigItemWidget(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    action.onClickConfig.invoke(
                        item.id,
                        item.type
                    )
                }
                .padding(SmsCheckerTheme.padding.horizontalSmall()),
            item = item,
            onChangeConfig = { isStatus ->
                action.onChangeConfigStatus(
                    item.id,
                    item.type,
                    isStatus
                )
            }
        )
        if (index != items.lastIndex) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = SmsCheckerTheme.color.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ListeningConfigItemWidget(
    modifier: Modifier,
    item: ListeningConfigState.Item,
    onChangeConfig: (isStatus: Boolean) -> Unit
) {
    Row(
        modifier = modifier.padding(paddingValues = SmsCheckerTheme.padding.verticalExtraSmall())
    ) {
        Text(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = item.name,
            style = SmsCheckerTheme.typography.bodyMedium,
            color = SmsCheckerTheme.color.onSurface
        )
        SwitchWidget(
            modifier = Modifier.align(Alignment.CenterVertically),
            isValue = item.isStatus,
            onChangeValue = onChangeConfig
        )
    }
}

@Composable
private fun ColumnScope.ListeningConfigEmptyWidget(
    title: String,
    caption: String,
    onClickEmpty: () -> Unit
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SmsCheckerTheme.padding.small())
            .align(Alignment.CenterHorizontally),
        text = title,
        textAlign = TextAlign.Center,
        style = SmsCheckerTheme.typography.bodyMedium,
        color = SmsCheckerTheme.color.onSurfaceVariant,
    )
    ListeningConfigAddNewButtonWidget(
        modifier = Modifier
            .wrapContentSize()
            .align(Alignment.CenterHorizontally),
        caption = caption,
        onClickAdd = onClickEmpty
    )
}

@Composable
private fun ListeningConfigAddNewButtonWidget(
    modifier: Modifier,
    caption: String,
    onClickAdd: () -> Unit
) {
    DefaultButtonWidget(
        modifier = modifier,
        caption = caption,
        onClick = onClickAdd,
        content = {
            Text(
                text = caption,
                style = SmsCheckerTheme.typography.labelMedium,
                color = SmsCheckerTheme.color.onPrimary,
            )
        }
    )
}
