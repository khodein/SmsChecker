package com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar

import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.mapper.SmtpBottomBarMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.state.SmtpBottomBarState
import com.sms.checker.forwarder.framework.block.Block

internal class SmtpBottomBarBlock(
    private val smtpBottomBarMapper: SmtpBottomBarMapper,
) : Block<SmtpBottomBarState, SmtpBottomBarAction, SmtpBottomBarBlock.Provider>() {

    private var isUpdate: Boolean = false

    fun setIsUpdate(isUpdate: Boolean) {
        this.isUpdate = isUpdate
        updateBlockState()
    }

    override val action = SmtpBottomBarAction(
        onClickAdd = ::onClickAdd
    )

    override fun getInitialUiState(): SmtpBottomBarState {
        return buildState()
    }

    override fun updateBlockState() {
        setState { buildState() }
    }

    fun onClickAdd() {
        val requiredMap = blockProvider?.getRequired() ?: return
        val requiredWithoutTest = requiredMap.filter { it.key != Required.Test }
        val isRequired = requiredWithoutTest.any { it.value }
        val isTestRequired = requiredMap[Required.Test] ?: false
        if (!isRequired) {
            onEvent(smtpBottomBarMapper.getErrorEvent())
            return
        }

        if (isTestRequired) {
            if (isUpdate) {
                blockProvider?.onUpdate()
            } else {
                blockProvider?.onSave()
            }
            return
        }

        blockProvider?.onRequired()
    }

    private fun buildState() = smtpBottomBarMapper.map(isUpdate)

    interface Provider {
        fun getRequired(): Map<Required, Boolean>
        fun onRequired()
        fun onSave()
        fun onUpdate()
    }

    enum class Required {
        Name,
        Server,
        From,
        Recipient,
        Test,
    }
}