package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState

@Immutable
internal sealed interface ListeningListItemState {
    val id: String
    val contentType: String

    @Immutable
    data class ConfigItemState(
        override val id: String,
        override val contentType: String,
        val listeningConfigState: ListeningConfigState,
    ): ListeningListItemState

}