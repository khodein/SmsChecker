package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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

private const val LISTENING_HISTORY_LOADING_KEY = "LISTENING_HISTORY_LOADING_KEY"
private const val LISTENING_HISTORY_STATUS_CONTENT_TYPE = "LISTENING_HISTORY_LOADING_CONTENT_TYPE"

private const val LISTENING_HISTORY_ITEM_CONTENT_TYPE = "LISTENING_HISTORY_ITEM_CONTENT_TYPE"

private const val LISTENING_HISTORY_EMPTY_KEY = "LISTENING_HISTORY_EMPTY_KEY"
private const val LISTENING_HISTORY_EMPTY_CONTENT_TYPE = "LISTENING_HISTORY_EMPTY_CONTENT_TYPE"

private const val LISTENING_HISTORY_ALL_KEY = "LISTENING_HISTORY_ALL_KEY"
private const val LISTENING_HISTORY_ALL_CONTENT_TYPE = "LISTENING_HISTORY_ALL_CONTENT_TYPE"

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
        Status.ERROR -> {
            loadingItem(
                state = state,
                action = action
            )
        }

        Status.SUCCESS -> {
            if (state.items.isEmpty()) {
                emptyItem(state.empty)
            } else {
                items(
                    items = state.items,
                    key = { it.id },
                    contentType = { LISTENING_HISTORY_ITEM_CONTENT_TYPE }
                ) { item ->
                    ListeningHistoryWidget(
                        item = item,
                        onClickItem = action.onClickItem,
                    )
                }

                if (state.items.isNotEmpty()) {
                    allItem(
                        all = state.button,
                        onClickAll = action.onClickAll
                    )
                }
            }
        }

        Status.IDLE, Status.LOADING -> Unit
    }
}

private fun LazyListScope.loadingItem(
    state: ListeningHistoryState,
    action: ListeningHistoryAction,
) {
    item(
        key = LISTENING_HISTORY_LOADING_KEY,
        contentType = LISTENING_HISTORY_STATUS_CONTENT_TYPE
    ) {
        ListeningHistoryErrorWidget(
            error = state.error,
            onClickReload = action.onClickReload
        )
    }
}

private fun LazyListScope.emptyItem(
    empty: ListeningHistoryState.Empty
) {
    item(
        key = LISTENING_HISTORY_EMPTY_KEY,
        contentType = LISTENING_HISTORY_EMPTY_CONTENT_TYPE
    ) {
        ListeningHistoryEmptyWidget(empty = empty)
    }
}

private fun LazyListScope.allItem(
    all: String,
    onClickAll: () -> Unit
) {
    item(
        key = LISTENING_HISTORY_ALL_KEY,
        contentType = LISTENING_HISTORY_ALL_CONTENT_TYPE
    ) {
        ListeningHistoryAllWidget(
            all = all,
            onClickAll = onClickAll,
        )
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
private fun ListeningHistoryErrorWidget(
    modifier: Modifier = Modifier,
    error: ListeningHistoryState.Error,
    onClickReload: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.all()
            )
            .padding(SmsCheckerTheme.padding.medium()),
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

@Composable
internal fun ListeningHistoryWidget(
    modifier: Modifier = Modifier,
    item: ListeningHistoryState.Item,
    onClickItem: (id: Long) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.all()
            )
            .clickable { onClickItem.invoke(item.id) }
            .padding(SmsCheckerTheme.padding.medium())
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
    }
}

@Composable
private fun ListeningHistoryAllWidget(
    all: String,
    onClickAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.all()
            )
            .clickable(onClick = onClickAll)
            .padding(SmsCheckerTheme.padding.medium())
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            style = SmsCheckerTheme.typography.bodySmall,
            color = SmsCheckerTheme.color.onSurfaceVariant,
            textAlign = TextAlign.Start,
            text = all
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