package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.bottombar.state.ListeningBottomBarState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.toolbar.state.ListeningToolbarState
import com.sms.checker.forwarder.framework.BaseUiState
import com.sms.checker.forwarder.framework.Status

@Immutable
internal data class ListeningListState(
    override val status: Status,
    val listeningToolbarState: ListeningToolbarState,
    val listeningState: ListeningState,
    val listeningBottomBarState: ListeningBottomBarState,
    val items: List<ListeningListItemState> = emptyList(),
) : BaseUiState()
