package com.sms.checker.forwarder.feature.email.presentation.screen.smtp

import androidx.lifecycle.SavedStateHandle
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.SmtpBottomBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.host.SmtpEmailHostBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.SmtpEmailNameBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.SmtpTopBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.user.SmtpEmailUserBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.mapper.SmtpEmailMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailItemState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailState
import com.sms.checker.forwarder.framework.BaseViewModel
import com.sms.checker.forwarder.framework.Status

internal class SmtpEmailViewModel(
    private val smtpEmailMapper: SmtpEmailMapper,
    private val smtpBottomBarBlock: SmtpBottomBarBlock,
    private val smtpTopBarBlock: SmtpTopBarBlock,
    private val smtpEmailNameBlock: SmtpEmailNameBlock,
    private val smtpEmailHostBlock: SmtpEmailHostBlock,
    private val smtpEmailUserBlock: SmtpEmailUserBlock,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<SmtpEmailState>(savedStateHandle) {

    init {
        attach()
    }

    override fun getInitialUiState(): SmtpEmailState {
        return SmtpEmailState(
            status = Status.SUCCESS,
            smtpBottomBarState = smtpBottomBarBlock.blockState.value,
            smtpTopBarState = smtpTopBarBlock.blockState.value,
            items = getItems()
        )
    }

    override fun updateViewState() {
        setState {
            copy(
                smtpBottomBarState = smtpBottomBarBlock.blockState.value,
                smtpTopBarState = smtpTopBarBlock.blockState.value,
                items = getItems(),
            )
        }
    }

    private fun getItems(): List<SmtpEmailItemState> {
        return smtpEmailMapper.mapSmtpEmailItems(
            smtpEmailNameState = smtpEmailNameBlock.blockState.value,
            smtpEmailHostState = smtpEmailHostBlock.blockState.value,
            smtpEmailUserState = smtpEmailUserBlock.blockState.value
        )
    }

    override fun attach() {
        registerBlocks {
            add(smtpTopBarBlock)
            add(smtpBottomBarBlock)
            add(smtpEmailNameBlock)
            add(smtpEmailHostBlock)
            add(smtpEmailUserBlock)
        }
    }
}