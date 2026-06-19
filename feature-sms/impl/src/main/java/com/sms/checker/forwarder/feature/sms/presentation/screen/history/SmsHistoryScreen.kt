package com.sms.checker.forwarder.feature.sms.presentation.screen.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.state.SmsHistoryListState
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.widget.SmsHistoryWidget
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.topbar.widget.SmsHistoryTopBarWidget
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.state.SmsHistoryWarningAction
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.warning.state.SmsHistoryWarningState
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.state.SmsHistoryAction
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.state.SmsHistoryState
import com.sms.checker.forwarder.feature.warning.widget.WarningUi
import com.sms.checker.forwarder.framework.PageList
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme
import com.sms.checker.forwarder.framework.uikit.DefaultButtonWidget
import com.sms.checker.forwarder.framework.uikit.LoadingWidget
import org.koin.compose.koinInject

private const val SMS_HISTORY_CONTENT_TYPE = "SMS_HISTORY_CONTENT_TYPE"

private const val SMS_HISTORY_LOADING_CONTENT_TYPE = "SMS_HISTORY_LOADING_CONTENT_TYPE"
private const val SMS_HISTORY_LOADING_KEY = "SMS_HISTORY_LOADING_KEY"

private const val SMS_HISTORY_ERROR_CONTENT_TYPE = "SMS_HISTORY_ERROR_CONTENT_TYPE"
private const val SMS_HISTORY_ERROR_KEY = "SMS_HISTORY_ERROR_KEY"

@Composable
internal fun SmsHistoryScreen(
    state: SmsHistoryState,
    action: SmsHistoryAction,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmsHistoryTopBarWidget(
                state = state.topBarState,
                action = action.topBarAction
            )
        }
    ) { innerPadding ->
        PageList(
            state = state.listState.pageState,
            contentPadding = innerPadding.plus(SmsCheckerTheme.padding.verticalExtraSmall()),
            verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.extraSmall()),
            contentType = { SMS_HISTORY_CONTENT_TYPE },
            contentKey = { item -> item.id.toString() },
            onNextAction = action.listAction.onNextLoad,
            errorContent = { page ->
                error(
                    error = state.listState.error,
                    onClickReload = {
                        action.listAction.onClickReload.invoke(page)
                    }
                )
            },
            loadContent = {
                loading()
            },
            itemContent = { state ->
                SmsHistoryWidget(
                    modifier = Modifier,
                    state = state,
                    onClick = action.listAction.onClickItem
                )
            },
        )
    }
}

@Composable
private fun WarningBanner(
    state: SmsHistoryWarningState,
    action: SmsHistoryWarningAction,
) {
    if (!state.isVisible) return
    val warningUi: WarningUi = koinInject()
    warningUi.WarningNotificationContent(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.bottom()
            )
            .shadow(
                elevation = 6.dp,
                shape = SmsCheckerTheme.corner.bottom()
            )
            .padding(SmsCheckerTheme.padding.horizontalNormal())
            .padding(bottom = SmsCheckerTheme.padding.normal()),
        title = state.title,
        description = state.description,
        onClick = action.onClick,
    )
}

private fun LazyListScope.loading() {
    item(
        key = SMS_HISTORY_LOADING_KEY,
        contentType = SMS_HISTORY_LOADING_CONTENT_TYPE
    ) {
        SmsHistoryLoadingWidget()
    }
}

@Composable
private fun SmsHistoryLoadingWidget(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(SmsCheckerTheme.padding.verticalSmall()),
        contentAlignment = Alignment.Center
    ) {
        LoadingWidget(
            modifier = Modifier.size(48.dp)
        )
    }
}

private fun LazyListScope.error(
    error: SmsHistoryListState.Error,
    onClickReload: () -> Unit
) {
    item(
        key = SMS_HISTORY_ERROR_KEY,
        contentType = SMS_HISTORY_ERROR_CONTENT_TYPE
    ) {
        SmsHistoryErrorWidget(
            modifier = Modifier,
            error = error,
            onClickReload = onClickReload
        )
    }
}

@Composable
private fun SmsHistoryErrorWidget(
    modifier: Modifier,
    error: SmsHistoryListState.Error,
    onClickReload: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(SmsCheckerTheme.padding.verticalSmall()),
    ) {
        DefaultButtonWidget(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SmsCheckerTheme.padding.horizontalLarge()),
            caption = error.text,
            onClick = onClickReload
        )
    }
}
