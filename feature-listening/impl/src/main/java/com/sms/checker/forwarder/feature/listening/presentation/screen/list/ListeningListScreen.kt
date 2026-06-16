package com.sms.checker.forwarder.feature.listening.presentation.screen.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.widget.ListeningBottomBarWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.widget.item
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.widget.items
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.widget.ListeningWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.widget.notificationItem
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.toolbar.state.ListeningToolbarState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.toolbar.widget.ListeningToolbarWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.state.ListeningListAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.state.ListeningListState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

internal const val LISTENING_ANIMATION_DURATION = 1000
internal fun listeningFadeSpec() = tween<Float>(LISTENING_ANIMATION_DURATION)
internal fun listeningPlacementSpec() = tween<IntOffset>(LISTENING_ANIMATION_DURATION)

@Composable
internal fun ListeningListScreen(
    state: ListeningListState,
    action: ListeningListAction,
    lazyListState: LazyListState,
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
            modifier = Modifier,
            state = state,
            lazyListState = lazyListState,
            action = action,
            paddingValues = it
        )
    }
}

@Composable
private fun ListeningListContent(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState,
    state: ListeningListState,
    action: ListeningListAction,
    paddingValues: PaddingValues,
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier,
        contentPadding = paddingValues.plus(SmsCheckerTheme.padding.verticalExtraSmall()),
        verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.extraSmall())
    ) {
        notificationItem(
            notificationState = state.listeningState.notificationState,
            onClickWarning = action.listeningAction.onClickWarning
        )
        item(
            state = state.listeningConfigState,
            action = action.configAction
        )
        items(
            state = state.listeningHistoryState,
            action = action.historyAction
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
            .shadow(6.dp)
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

