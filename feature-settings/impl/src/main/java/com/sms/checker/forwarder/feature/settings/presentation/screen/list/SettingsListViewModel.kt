package com.sms.checker.forwarder.feature.settings.presentation.screen.list

import com.sms.checker.forwarder.feature.settings.presentation.screen.list.state.SettingsListAction
import com.sms.checker.forwarder.feature.settings.presentation.screen.list.state.SettingsListState
import com.sms.checker.forwarder.framework.BaseViewModel
import com.sms.checker.forwarder.framework.router.Router

internal class SettingsListViewModel(
    private val mapper: SettingsListMapper,
    private val router: Router,
) : BaseViewModel<SettingsListState, SettingsListAction>() {

    override val action: SettingsListAction = SettingsListAction(
        onClickBack = router::goBack
    )

    init {
        attach()
    }

    override fun attach() = Unit

    override fun updateViewState() = Unit

    override fun getInitialUiState(): SettingsListState {
        return mapper.map()
    }
}
