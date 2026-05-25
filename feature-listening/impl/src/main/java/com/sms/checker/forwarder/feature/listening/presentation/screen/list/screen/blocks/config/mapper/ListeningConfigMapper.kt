package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.mapper

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningConfigMapper(
    private val resProvider: ResProvider,
) {
    fun mapConfigState(
        action: ListeningConfigAction,
        smtpEmailList: List<SmtpEmailModel> = emptyList(),
    ): ListeningConfigState {
        return if (smtpEmailList.isEmpty()) {
            mapEmptyState(action)
        } else {
            mapConfigItemsState(
                smtpEmailList = smtpEmailList,
                action = action,
            )
        }
    }

    private fun mapEmptyState(
        action: ListeningConfigAction,
    ): ListeningConfigState {
        return ListeningConfigState.EmptyConfig(
            action = action,
            title = resProvider.getString(R.string.feature_listening_empty_configs),
            actionText = resProvider.getString(R.string.feature_listening_add_config),
        )
    }

    private fun mapConfigItemsState(
        smtpEmailList: List<SmtpEmailModel>,
        action: ListeningConfigAction,
    ): ListeningConfigState.ItemsConfig {
        return ListeningConfigState.ItemsConfig(
            title = resProvider.getString(R.string.feature_listening_config_apply),
            action = action,
            items = mapConfigItems(smtpEmailList)
        )
    }

    private fun mapConfigItems(
        smtpEmailList: List<SmtpEmailModel>,
    ): List<ListeningConfigState.ConfigItemState> {
        return buildList {
            addAll(getSmtpEmailConfigItems(smtpEmailList))
        }
    }

    private fun getSmtpEmailConfigItems(
        smtpEmailList: List<SmtpEmailModel>,
    ): List<ListeningConfigState.ConfigItemState> {
        return smtpEmailList.map {
            ListeningConfigState.ConfigItemState(
                id = it.id.toString(),
                name = it.name,
                type = ListeningConfigState.ConfigType.SMTP
            )
        }
    }
}