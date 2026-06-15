package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.mapper

import com.sms.checker.forwarder.feature.listening.R
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class ListeningMapper(
    private val resProvider: ResProvider
) {
    fun map(
        isListening: Boolean,
        needPermissionState: ListeningState.NeedPermissionState?,
    ): ListeningState {
        return ListeningState(
            title = resProvider.getString(R.string.feature_listening_switch_title),
            description = resProvider.getString(R.string.feature_listening_switch_description),
            isListening = isListening,
            needPermissionState = needPermissionState,
        )
    }
}