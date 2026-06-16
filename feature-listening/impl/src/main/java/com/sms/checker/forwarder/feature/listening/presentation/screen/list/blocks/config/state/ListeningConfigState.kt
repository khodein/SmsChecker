package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state

import androidx.compose.runtime.Immutable

@Immutable
data class ListeningConfigState(
    val title: String,
    val actionText: String,
    val items: List<Item>,
) {

    @Immutable
    data class Item(
        val id: Long,
        val name: String,
        val type: ConfigType,
        val isStatus: Boolean,
    )

    @Immutable
    enum class ConfigType {
        SMTP
    }
}