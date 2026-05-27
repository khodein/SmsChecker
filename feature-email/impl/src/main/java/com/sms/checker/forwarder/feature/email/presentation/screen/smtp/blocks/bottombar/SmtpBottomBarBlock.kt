package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.mapper.SmtpBottomBarMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.framework.block.BaseBlock

internal class SmtpBottomBarBlock(
    private val smtpBottomBarMapper: SmtpBottomBarMapper,
) : BaseBlock<SmtpBottomBarState, SmtpBottomBarBlock.Provider>() {

    private val action = SmtpBottomBarAction(
        onClickAdd = ::onClickAdd
    )

    override fun getInitialUiState(): SmtpBottomBarState {
        return smtpBottomBarMapper.map(action = action)
    }

    override fun updateBlockState() {

    }

    private fun onClickAdd() {
        val requiredMap = blockProvider?.getRequired() ?: return
        val isRequired = requiredMap.any { it.value }
        if (isRequired) {

        }
    }

    interface Provider {
        fun getRequired(): Map<Required, Boolean>
    }

    enum class Required {
        Host,
        Recipient,
        Name,
        User,
    }
}