package com.sms.checker.forwarder.feature.settings.presentation.screen.list

import com.sms.checker.forwarder.feature.settings.R
import com.sms.checker.forwarder.feature.settings.presentation.screen.list.state.SettingsListState
import com.sms.checker.forwarder.framework.tools.res.ResProvider

internal class SettingsListMapper(
    private val resProvider: ResProvider,
) {
    fun map(): SettingsListState {
        return SettingsListState(
            title = resProvider.getString(R.string.feature_settings_list_title),
            emptyTitle = resProvider.getString(R.string.feature_settings_list_empty_title),
            emptyDescription = resProvider.getString(R.string.feature_settings_list_empty_description)
        )
    }
}
