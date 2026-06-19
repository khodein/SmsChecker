package com.sms.checker.forwarder.feature.warning.presentation.screen.warning

import com.sms.checker.forwarder.feature.warning.domain.usecase.GetWarningUseCase
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.mapper.WarningScreenMapper
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.state.WarningAction
import com.sms.checker.forwarder.feature.warning.presentation.screen.warning.state.WarningState
import com.sms.checker.forwarder.framework.BaseViewModel

internal class WarningViewModel(
    private val getWarningUseCase: GetWarningUseCase,
    private val mapper: WarningScreenMapper,
) : BaseViewModel<WarningState, WarningAction>() {

    override val action: WarningAction = WarningAction

    init {
        attach()
    }

    override fun attach() {
        registerBlocks { }
    }

    override fun getInitialUiState(): WarningState {
        return mapper.map(getWarningUseCase.invoke())
    }

    override fun updateViewState() {
        setState { mapper.map(getWarningUseCase.invoke()) }
    }
}