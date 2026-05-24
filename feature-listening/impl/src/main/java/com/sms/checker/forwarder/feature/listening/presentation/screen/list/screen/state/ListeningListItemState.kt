package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.state

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ListeningListItemState {
    val id: String
    val contentType: String

}