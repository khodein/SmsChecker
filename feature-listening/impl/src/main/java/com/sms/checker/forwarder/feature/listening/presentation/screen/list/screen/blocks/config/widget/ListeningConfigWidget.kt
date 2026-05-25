package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState.ConfigItemState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.DefaultButtonWidget

@Composable
internal fun ListeningConfigWidget(
    modifier: Modifier,
    state: ListeningConfigState,
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
        when (state) {
            is ListeningConfigState.EmptyConfig -> {
                ListeningConfigEmptyWidget(
                    title = state.title,
                    caption = state.actionText,
                    onClickEmpty = state.action.onClickEmpty
                )
            }

            is ListeningConfigState.ItemsConfig -> {
                ListeningConfigListWidget(
                    title = state.title,
                    items = state.items,
                    onClickItem = state.action.onClickItem
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.ListeningConfigListWidget(
    title: String,
    items: List<ConfigItemState>,
    onClickItem: (id: String) -> Unit
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
    items.forEach { item ->
        ListeningConfigItemWidget(
            item = item,
            onClickItem = onClickItem,
        )
    }
}

@Composable
private fun ColumnScope.ListeningConfigItemWidget(
    item: ConfigItemState,
    onClickItem: (id: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickItem.invoke(item.id) }
    ) {
        Icon(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterVertically),
            imageVector = getIcon(item.type),
            tint = SmsCheckerTheme.color.onSurfaceVariant,
            contentDescription = "ConfigItemIconWidget"
        )
    }
}

private fun getIcon(type: ListeningConfigState.ConfigType): ImageVector {
    return when (type) {
        ListeningConfigState.ConfigType.SMTP -> Icons.Default.Email
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
    DefaultButtonWidget(
        modifier = Modifier
            .wrapContentSize()
            .align(Alignment.CenterHorizontally),
        caption = caption,
        onClick = onClickEmpty,
        content = {
            Text(
                text = caption,
                style = SmsCheckerTheme.typography.labelMedium,
                color = SmsCheckerTheme.color.onPrimary,
            )
        }
    )
}