package com.sms.checker.forwarder.feature.email.presentation.screen.smtp

import androidx.lifecycle.SavedStateHandle
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.SmtpBottomBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.SmtpTopBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailState
import com.sms.checker.forwarder.framework.BaseViewModel
import com.sms.checker.forwarder.framework.Status

internal class SmtpEmailViewModel(
    private val smtpBottomBarBlock: SmtpBottomBarBlock,
    private val smtpTopBarBlock: SmtpTopBarBlock,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<SmtpEmailState>(savedStateHandle) {

    init {
        attach()
    }

    override fun getInitialUiState(): SmtpEmailState {
        return SmtpEmailState(
            status = Status.SUCCESS,
            smtpBottomBarState = smtpBottomBarBlock.blockState.value,
            smtpTopBarState = smtpTopBarBlock.blockState.value
        )
    }

    override fun updateViewState() {
        setState {
            copy(
                smtpBottomBarState = smtpBottomBarBlock.blockState.value,
                smtpTopBarState = smtpTopBarBlock.blockState.value
            )
        }
    }

    override fun attach() {
        registerBlocks {
            add(smtpBottomBarBlock)
        }
    }
}