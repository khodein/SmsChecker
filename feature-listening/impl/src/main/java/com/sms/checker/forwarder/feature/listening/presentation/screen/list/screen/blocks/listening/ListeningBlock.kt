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
    private val getListeningUseCase: GetListeningUseCase,
): BaseBlock<ListeningState, ListeningAction, ListeningBlock.Provider>() {

    var isListeningState: Boolean = false
        private set(value) {
            blockProvider?.onUpdateListening(value)
            field = value
        }

    private var isGrantedPermissionState: Boolean = false
    private var needPermissionState: ListeningState.NeedPermissionState? = ListeningState.NeedPermissionState

    override val action = ListeningAction(
        onClickListening = ::onClickListening,
        onPermissionListeningResult = ::onPermissionListeningResult
    )

    override fun onUiStart() {
        super.onUiStart()
        isListeningState = getListeningUseCase.invoke()
        updateBlockState()
    }

    override fun getInitialUiState(): ListeningState {
        return buildState()
    }

    override fun updateBlockState() {
        setState { buildState() }
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
        blockProvider?.onUpdateListening(isListeningState)
    }

    private fun onListening() {
        if (isListeningState) {
            startListeningUseCase.invoke()
        } else {
            stopListeningUseCase.invoke()
        }
    }

    private fun buildState(): ListeningState {
        return listeningMapper.map(
            isListening = isListeningState,
            needPermissionState = needPermissionState,
        )
    }

    private fun onPermissionListeningResult(isGranted: Boolean) {
        needPermissionState = null
        isGrantedPermissionState = isGranted
        updateBlockState()
    }

    interface Provider {
        fun onUpdateListening(isListening: Boolean)
    }
}