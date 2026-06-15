package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.mapper

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailStatus
import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigEvent
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningConfigMapper(
    private val resProvider: ResProvider,
) {
    fun mapEmptyConfigState(): ListeningConfigState {
        return ListeningConfigState.EmptyConfig(
            title = resProvider.getString(R.string.feature_listening_empty_configs),
            actionText = resProvider.getString(R.string.feature_listening_add_config),
        )
    }

    fun mapItemsConfigState(
        smtpList: List<SmtpEmailModel>
    ): ListeningConfigState {
        return ListeningConfigState.ItemsConfig(
            title = resProvider.getString(R.string.feature_listening_config_apply),
            items = buildList {
                addAll(mapSmtpItemsConfigState(smtpList))
            }
        )
    }

    private fun mapSmtpItemsConfigState(
        smtpList: List<SmtpEmailModel>
    ): List<ListeningConfigState.ItemsConfig.Item> {
        return smtpList.mapNotNull { model ->
            val id = model.id ?: return@mapNotNull null
            ListeningConfigState.ItemsConfig.Item(
                id = id,
                name = model.name,
                type = ListeningConfigState.ConfigType.SMTP,
                isStatus = model.status == SmtpEmailStatus.Enable
            )
        }
    }

    fun mapSnackBarEventInfo(): ListeningConfigEvent.SnackBarEvent {
        return ListeningConfigEvent.SnackBarEvent(
            value = resProvider.getString(R.string.feature_listening_add_disabled_hint),
            status = ListeningConfigEvent.Status.Info
        )
    }

    fun mapSnackBarEventStatusError(): ListeningConfigEvent.SnackBarEvent {
        return ListeningConfigEvent.SnackBarEvent(
            value = resProvider.getString(R.string.feature_listening_error_update_state),
            status = ListeningConfigEvent.Status.Error
        )
    }

    fun mapSnackBarEventStatusSuccess(): ListeningConfigEvent.SnackBarEvent {
        return ListeningConfigEvent.SnackBarEvent(
            value = resProvider.getString(R.string.feature_listening_status_updated_success),
            status = ListeningConfigEvent.Status.Success
        )
    }
}
