package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening

import com.sms.checker.forwarder.feature.listening.domain.usecase.GetListeningObserveUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.GetListeningUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.StartListeningUseCase
import com.sms.checker.forwarder.feature.listening.domain.usecase.StopListeningUseCase
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningState
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.mapper.ListeningMapper
import com.sms.checker.forwarder.framework.block.BaseBlock
import kotlinx.coroutines.launch

internal class ListeningBlock(
    private val listeningMapper: com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.mapper.ListeningMapper,
    private val getListeningObserveStateUseCase: GetListeningObserveUseCase,
    private val startListeningUseCase: StartListeningUseCase,
    private val stopListeningUseCase: StopListeningUseCase,
    getListeningUseCase: GetListeningUseCase,
): BaseBlock<com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.listening.state.ListeningState, Unit>() {

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
        blockScope?.launch {
            getListeningObserveStateUseCase.invoke().collect {
                isListeningState = it
                updateBlockState()
            }
        }
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