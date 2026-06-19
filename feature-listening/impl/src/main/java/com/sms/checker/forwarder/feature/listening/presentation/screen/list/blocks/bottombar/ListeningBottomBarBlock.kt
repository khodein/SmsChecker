package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar

import com.sms.checker.forwarder.feature.email.router.EmailRouter
import com.sms.checker.forwarder.feature.listening.domain.usecase.ObserveListeningUseCase
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.mapper.ListeningBottomBarMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.state.ListeningBottomBarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.state.ListeningBottomBarState
import com.sms.checker.forwarder.framework.block.Block
import kotlinx.coroutines.launch

internal class ListeningBottomBarBlock(
    private val listeningBottomBarMapper: ListeningBottomBarMapper,
    private val observeListeningUseCase: ObserveListeningUseCase,
    private val navEmailRouter: EmailRouter,
) : Block<ListeningBottomBarState, ListeningBottomBarAction, Unit>() {
    private var isListening: Boolean = false

    override val action = ListeningBottomBarAction(
        onClickForward = ::onClickForward
    )

    override fun getInitialUiState(): ListeningBottomBarState {
        return ListeningBottomBarState(
            isVisible = false,
            caption = listeningBottomBarMapper.mapBottomBarCaption()
        )
    }

    override fun startBlock() {
        blockScope?.launch {
            observeListeningUseCase.invoke().collect { isListening ->
                this@ListeningBottomBarBlock.isListening = isListening
                updateBlockState()
            }
        }
    }

    override fun updateBlockState() {
        updateState {
            it.copy(
                isVisible = !isListening
            )
        }
    }

    override fun onUiStart() {
        updateBlockState()
    }

    private fun onClickForward() {
        if (isListening) {
            onEvent(listeningBottomBarMapper.mapSnackBarEventInfo())
        } else {
            navEmailRouter.gotoSmtpEmail()
        }
    }
}