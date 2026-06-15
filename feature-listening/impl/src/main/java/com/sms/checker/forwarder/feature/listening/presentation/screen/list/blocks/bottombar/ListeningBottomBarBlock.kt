package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar

import com.sms.checker.forwarder.feature.email.router.EmailRouter
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.mapper.ListeningBottomBarMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.state.ListeningBottomBarAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.bottombar.state.ListeningBottomBarState
import com.sms.checker.forwarder.framework.block.Block

internal class ListeningBottomBarBlock(
    private val listeningBottomBarMapper: ListeningBottomBarMapper,
    private val navEmailRouter: EmailRouter,
) : Block<ListeningBottomBarState, ListeningBottomBarAction, Unit>() {

    private var isListening: Boolean = true
    private var configSizes: List<Boolean> = listOf()

    override val action = ListeningBottomBarAction(
        onClickForward = ::onClickForward
    )

    override fun getInitialUiState(): ListeningBottomBarState {
        return buildState()
    }

    private fun onClickForward() {
        if (isListening) {
            onEvent(listeningBottomBarMapper.mapSnackBarEventInfo())
        } else {
            navEmailRouter.gotoSmtpEmail()
        }
    }

    override fun updateBlockState() {
        setState { buildState() }
    }

    override fun onUiStart() {
        updateBlockState()
    }

    private fun buildState(): ListeningBottomBarState {
        val isConfig = if (configSizes.isEmpty()) {
            true
        } else {
            configSizes.all { it }
        }
        return listeningBottomBarMapper.map(
            isVisible = !isListening && isConfig
        )
    }

    fun updateListening(isListening: Boolean) {
        this.isListening = isListening
        updateBlockState()
    }

    fun updateConfigSizes(list: List<Boolean>) {
        this.configSizes = list
        updateBlockState()
    }
}