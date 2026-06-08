package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state.ListeningListState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state.ListeningToolbarState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.widget.ListeningBottomBarWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.widget.ListeningConfigWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.widget.ListeningToolbarWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.widget.ListeningWidget
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state.ListeningListAction
import com.sms.checker.forwarder.framework.Status
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
        verticalArrangement = Arrangement.spacedBy(SmsCheckerTheme.padding.extraSmall())
    ) {
        item(
            key = "config",
            contentType = "config"
        ) {
            ListeningConfigWidget(
                state = state.listeningConfigState,
                action = action.configAction
            )
        }
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

