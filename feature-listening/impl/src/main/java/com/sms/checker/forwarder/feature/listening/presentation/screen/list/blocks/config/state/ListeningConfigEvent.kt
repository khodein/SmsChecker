package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.UiEvent

@Immutable
internal sealed interface ListeningConfigEvent : UiEvent {

    @Immutable
    data class SnackBarEvent(
        val value: String,
        val status: Status
    ) : ListeningConfigEvent

    @Immutable
    enum class Status {
        Info,
        Error,
        Success
    }
}