package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state.ListeningHistoryAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state.ListeningHistoryEvent
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state.ListeningHistoryState
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.DefaultButtonWidget
import com.sms.checker.forwarder.framework.uikit.LoadingWidget

private const val LISTENING_HISTORY_LOADING_KEY = "LISTENING_HISTORY_LOADING_KEY"
private const val LISTENING_HISTORY_STATUS_CONTENT_TYPE = "LISTENING_HISTORY_LOADING_CONTENT_TYPE"

private const val LISTENING_HISTORY_ITEM_CONTENT_TYPE = "LISTENING_HISTORY_ITEM_CONTENT_TYPE"

private const val LISTENING_HISTORY_EMPTY_KEY = "LISTENING_HISTORY_EMPTY_KEY"
private const val LISTENING_HISTORY_EMPTY_CONTENT_TYPE = "LISTENING_HISTORY_EMPTY_CONTENT_TYPE"

internal fun ListeningHistoryEvent.onEvent(snackbarHostState: SnackbarHostState) {

}

internal fun LazyListScope.item(
    state: ListeningHistoryState,
    action: ListeningHistoryAction,
) {
    when (state.status) {
        Status.IDLE -> Unit
        else -> items(
            state = state,
            action = action,
        )
    }
}

private fun LazyListScope.items(
    state: ListeningHistoryState,
    action: ListeningHistoryAction,
) {
    when (state.status) {
        Status.LOADING, Status.ERROR -> {
            item(
                key = LISTENING_HISTORY_LOADING_KEY,
                contentType = LISTENING_HISTORY_STATUS_CONTENT_TYPE
            ) {
                ListeningHistoryLoadingErrorWidget(
                    status = state.status,
                    error = state.error,
                    onClickReload = action.onClickReload
                )
            }
        }

        Status.SUCCESS -> {

            if (state.items.isEmpty()) {
                item(
                    key = LISTENING_HISTORY_EMPTY_KEY,
                    contentType = LISTENING_HISTORY_EMPTY_CONTENT_TYPE
                ) {
                    ListeningHistoryEmptyWidget(empty = state.empty)
                }
            } else {
                itemsIndexed(
                    items = state.items,
                    key = { _, item -> item.id },
                    contentType = { _, _ -> LISTENING_HISTORY_ITEM_CONTENT_TYPE }
                ) { index, item ->
                    val lastIndex = state.items.lastIndex
                    val isLast = index == lastIndex
                    val modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = SmsCheckerTheme.color.surface,
                            shape = RoundedCornerShape(
                                topStart = if (index == 0) SmsCheckerTheme.corner.medium() else 0.dp,
                                topEnd = if (index == 0) SmsCheckerTheme.corner.medium() else 0.dp,
                                bottomStart = if (isLast) SmsCheckerTheme.corner.medium() else 0.dp,
                                bottomEnd = if (isLast) SmsCheckerTheme.corner.medium() else 0.dp,
                            )
                        )
                        .padding(SmsCheckerTheme.padding.allMedium())
                    ListeningHistoryWidget(
                        modifier = modifier,
                        item = item,
                        all = if (isLast) state.button else null,
                        onClickItem = action.onClickItem,
                        onClickAll = action.onClickAll,
                    )
                    if (index != lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = SmsCheckerTheme.color.surface)
                                .padding(SmsCheckerTheme.padding.horizontalNormal()),
                            thickness = 1.dp,
                            color = SmsCheckerTheme.color.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Status.IDLE -> Unit
    }
}

@Composable
private fun ListeningHistoryEmptyWidget(
    modifier: Modifier = Modifier,
    empty: ListeningHistoryState.Empty,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.all()
            )
            .padding(SmsCheckerTheme.padding.allMedium())
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmsCheckerTheme.padding.small())
                .align(Alignment.CenterHorizontally),
            text = empty.title,
            textAlign = TextAlign.Start,
            style = SmsCheckerTheme.typography.bodyLarge,
            color = SmsCheckerTheme.color.onSurface
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = empty.text,
            textAlign = TextAlign.Center,
            style = SmsCheckerTheme.typography.bodyMedium,
            color = SmsCheckerTheme.color.onSurfaceVariant,
        )
    }
}

@Composable
private fun ListeningHistoryLoadingErrorWidget(
    modifier: Modifier = Modifier,
    status: Status,
    error: ListeningHistoryState.Error,
    onClickReload: () -> Unit
) {
    if (status == Status.LOADING || status == Status.ERROR) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = SmsCheckerTheme.color.surface,
                    shape = SmsCheckerTheme.corner.all()
                )
                .padding(SmsCheckerTheme.padding.allLarge())
        ) {
            when (status) {
                Status.LOADING -> {
                    LoadingWidget(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }

                Status.ERROR -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.extraSmall())
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = error.text,
                            textAlign = TextAlign.Center,
                            style = SmsCheckerTheme.typography.bodyMedium,
                            color = SmsCheckerTheme.color.onSurfaceVariant,
                        )
                        DefaultButtonWidget(
                            modifier = Modifier.wrapContentWidth(),
                            caption = error.button,
                            onClick = onClickReload
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ListeningHistoryWidget(
    modifier: Modifier,
    item: ListeningHistoryState.Item,
    all: String?,
    onClickItem: (id: Long) -> Unit,
    onClickAll: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onClickItem.invoke(item.id) }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmsCheckerTheme.padding.medium())
                .align(Alignment.CenterHorizontally),
            text = item.date,
            textAlign = TextAlign.End,
            style = SmsCheckerTheme.typography.labelMedium,
            color = SmsCheckerTheme.color.onSurfaceVariant,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SmsCheckerTheme.padding.medium())
                .align(Alignment.CenterHorizontally),
            text = item.title,
            textAlign = TextAlign.Center,
            style = SmsCheckerTheme.typography.bodyLarge,
            color = SmsCheckerTheme.color.onSurface
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = item.description,
            textAlign = TextAlign.Start,
            style = SmsCheckerTheme.typography.bodyMedium,
            color = SmsCheckerTheme.color.onSurface,
        )
        all?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SmsCheckerTheme.padding.small())
                    .clickable {
                        onClickAll.invoke()
                    }
                    .padding(vertical = SmsCheckerTheme.padding.small())
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    style = SmsCheckerTheme.typography.bodySmall,
                    color = SmsCheckerTheme.color.onSurfaceVariant,
                    textAlign = TextAlign.Start,
                    text = it
                )
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterVertically),
                    tint = SmsCheckerTheme.color.onSurfaceVariant,
                    contentDescription = "Sms All Forward",
                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                )
            }
        }
    }
}