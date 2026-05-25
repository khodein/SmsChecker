package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ListeningConfigState {
    val title: String
    val action: ListeningConfigAction

    @Immutable
    data class EmptyConfig(
        override val title: String,
        override val action: ListeningConfigAction,
        val actionText: String,
    ) : ListeningConfigState

    @Immutable
    data class ItemsConfig(
        override val title: String,
        override val action: ListeningConfigAction,
        val items: List<ConfigItemState>
    ) : ListeningConfigState

    @Immutable
    data class ConfigItemState(
        val id: String,
        val name: String,
        val type: ConfigType,
    )

    @Immutable
    enum class ConfigType {
        SMTP
    }
}