package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state

import androidx.compose.runtime.Immutable
import com.sms.checker.forwarder.framework.UiEvent

@Immutable
sealed interface ListeningEvent : UiEvent {
    data object ScrollTop : ListeningEvent
}