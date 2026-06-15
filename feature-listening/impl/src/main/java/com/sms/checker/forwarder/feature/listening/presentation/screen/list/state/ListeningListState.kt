package com.sms.checker.forwarder.feature.listening.presentation.screen.list.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.state.ListeningBottomBarState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state.ListeningHistoryState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.toolbar.state.ListeningToolbarState
import com.sms.checker.forwarder.framework.UiState

@Immutable
internal data class ListeningListState(
    val listeningToolbarState: ListeningToolbarState,
    val listeningState: ListeningState,
    val listeningBottomBarState: ListeningBottomBarState,
    val listeningConfigState: ListeningConfigState,
    val listeningHistoryState: ListeningHistoryState,
) : UiState()
