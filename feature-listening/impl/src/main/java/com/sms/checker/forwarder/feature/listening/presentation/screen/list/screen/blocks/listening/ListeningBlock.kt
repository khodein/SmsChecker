package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening

import com.sms.checker.forwarder.feature.listening.domain.usecase.GetListeningUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.StartListeningUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.StopListeningUseCase
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.mapper.ListeningMapper
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class ListeningBlock(
    private val listeningMapper: ListeningMapper,
    private val startListeningUseCase: StartListeningUseCase,
    private val stopListeningUseCase: StopListeningUseCase,
    getListeningUseCase: GetListeningUseCase,
): BaseBlock<ListeningState, Unit>() {

    private var isListeningState: Boolean = getListeningUseCase.invoke()

    private var isGrantedPermissionState: Boolean = false
    private var needPermissionState: ListeningState.NeedPermissionState? = ListeningState.NeedPermissionState

    private val action = ListeningAction(
        onClickListening = ::onClickListening,
        onPermissionListeningResult = ::onPermissionListeningResult
    )

    override fun getInitialUiState(): ListeningState {
        return listeningMapper.map(
            isListening = isListeningState,
            needPermissionState = needPermissionState,
            action = action,
        )
    }

    override fun start() {

    }

    override fun updateBlockState() {
        setState {
            copy(
                isListening = isListeningState,
                needPermissionState = needPermissionState
            )
        }
    }

    private fun onClickListening(isListening: Boolean) {
        if (isGrantedPermissionState) {
            needPermissionState = null
            isListeningState = isListening
            onListening()
        } else {
            needPermissionState = ListeningState.NeedPermissionState
            isListeningState = false
        }

        updateBlockState()
    }

    private fun onListening() {
        if (isListeningState) {
            startListeningUseCase.invoke()
        } else {
            stopListeningUseCase.invoke()
        }
    }

    private fun onPermissionListeningResult(isGranted: Boolean) {
        needPermissionState = null
        isGrantedPermissionState = isGranted
        updateBlockState()
    }
}