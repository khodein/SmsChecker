package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ListeningConfigState {
    val title: String

    data object None : ListeningConfigState {
        override val title: String = ""
    }

    @Immutable
    data class EmptyConfig(
        override val title: String,
        val actionText: String,
    ) : ListeningConfigState

    @Immutable
    data class ItemsConfig(
        override val title: String,
        val items: List<Item>,
    ) : ListeningConfigState {

        @Immutable
        data class Item(
            val id: Long,
            val name: String,
            val type: ConfigType,
            val isStatus: Boolean,
        )
    }

    @Immutable
    enum class ConfigType {
        SMTP
    }
}