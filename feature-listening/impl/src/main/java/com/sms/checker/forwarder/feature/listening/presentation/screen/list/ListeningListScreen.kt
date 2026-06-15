package com.sms.checker.forwarder.feature.listening.presentation.screen.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.widget.ListeningBottomBarWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.widget.item
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.widget.item
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.widget.ListeningWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.toolbar.state.ListeningToolbarState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.toolbar.widget.ListeningToolbarWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.state.ListeningListAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.state.ListeningListState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@Composable
internal fun ListeningListScreen(
    state: ListeningListState,
    action: ListeningListAction,
) {
    Scaffold(
        topBar = {
            ListeningListAppBarWidget(
                modifier = Modifier.fillMaxWidth(),
                listeningState = state.listeningState,
                toolbarState = state.listeningToolbarState,
                action = action,
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = state.listeningBottomBarState.isVisible,
                enter = slideInVertically(animationSpec = tween(300)) { fullHeight -> fullHeight },
                exit = slideOutVertically(animationSpec = tween(300)) { fullHeight -> fullHeight },
            ) {
                ListeningBottomBarWidget(
                    modifier = Modifier.fillMaxWidth(),
                    state = state.listeningBottomBarState,
                    action = action.bottomBarAction,
                )
            }
        }
    ) {
        ListeningListContent(
            modifier = Modifier.padding(it),
            state = state,
            action = action,
        )
    }
}

@Composable
private fun ListeningListContent(
    modifier: Modifier = Modifier,
    state: ListeningListState,
    action: ListeningListAction,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = SmsCheckerTheme.padding.verticalExtraSmall(),
    ) {
        item(
            state = state.listeningConfigState,
            action = action.configAction
        )
        space(key = "Space1")
        item(
            state = state.listeningHistoryState,
            action = action.historyAction
        )
        space(key = "Space2")
    }
}

private fun LazyListScope.space(
    key: String
) {
    item(
        key = key,
        contentType = "SpaceBetweenContentType"
    ) {
        Spacer(
            modifier = Modifier
                .height(4.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
internal fun ListeningListAppBarWidget(
    modifier: Modifier = Modifier,
    listeningState: ListeningState,
    toolbarState: ListeningToolbarState,
    action: ListeningListAction,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SmsCheckerTheme.color.surface,
                shape = SmsCheckerTheme.corner.bottom()
            ),
    ) {
        ListeningToolbarWidget(
            modifier = Modifier.fillMaxWidth(),
            state = toolbarState,
            action = action.toolbarAction,
        )
        ListeningWidget(
            modifier = Modifier.fillMaxWidth(),
            state = listeningState,
            action = action.listeningAction,
        )
    }
}

