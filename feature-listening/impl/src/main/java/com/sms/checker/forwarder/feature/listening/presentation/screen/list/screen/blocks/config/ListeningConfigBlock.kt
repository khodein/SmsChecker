package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailStatus
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.UpdateSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.router.EmailRouter
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.mapper.ListeningConfigMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.framework.block.BaseBlock
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ListeningConfigBlock(
    private val listeningConfigMapper: ListeningConfigMapper,
    private val navEmailRouter: EmailRouter,
    private val getSmtpConfigUseCase: GetSmtpConfigUseCase,
    private val updateSmtpConfigUseCase: UpdateSmtpConfigUseCase,
) : BaseBlock<ListeningConfigState, ListeningConfigAction, ListeningConfigBlock.Provider>() {

    private var loadSmtpJob: Job? = null
    private var smtpList: List<SmtpEmailModel> = emptyList()

    private val configSizes: List<Boolean>
        get() {
            return listOf(
                smtpList.size <= 3
            )
        }

    override val action = ListeningConfigAction(
        onClickEmpty = ::onClickAddNew,
        onClickConfig = ::onClickConfig,
        onChangeConfigStatus = ::onChangeConfigStatus
    )

    override fun getInitialUiState(): ListeningConfigState {
        return ListeningConfigState.None
    }

    override fun updateBlockState() {
        setState { buildState() }
    }

    private fun buildState(): ListeningConfigState {
        return if (smtpList.isEmpty()) {
            listeningConfigMapper.mapEmptyConfigState()
        } else {
            listeningConfigMapper.mapItemsConfigState(smtpList)
        }
    }

    override fun onUiStart() {
        super.onUiStart()
        loadSmtp()
    }

    private fun onClickAddNew() {
        onCheckListening {
            navEmailRouter.gotoSmtpEmail()
        }
    }

    private fun onChangeConfigStatus(
        id: Long,
        type: ListeningConfigState.ConfigType,
        isStatus: Boolean,
    ) {
        onCheckListening {
            when (type) {
                ListeningConfigState.ConfigType.SMTP -> {
                    updateSmtp(
                        isStatus = isStatus,
                        id = id,
                    )
                }
            }
        }
    }

    private fun onClickConfig(
        id: Long,
        type: ListeningConfigState.ConfigType
    ) {
        onCheckListening {
            when (type) {
                ListeningConfigState.ConfigType.SMTP -> {
                    navEmailRouter.gotoSmtpEmail(id)
                }
            }
        }
    }

    private fun onCheckListening(
        doNotListening: () -> Unit
    ) {
        if (blockProvider?.isListening() == true) {
            onEvent(listeningConfigMapper.mapSnackBarEventInfo())
        } else {
            doNotListening.invoke()
        }
    }

    private fun loadSmtp() {
        loadSmtpJob?.cancel()
        loadSmtpJob = blockScope?.launch {
            runCatching {
                getSmtpConfigUseCase.invoke()
            }.onSuccess { list ->
                smtpList = list
                blockProvider?.onConfigSizes(configSizes)
                updateBlockState()
            }.onFailure {
                smtpList = emptyList()
                updateBlockState()
            }
        }
    }

    private fun updateSmtp(
        id: Long,
        isStatus: Boolean,
    ) {
        val index = smtpList.indexOfFirst { it.id == id }
        if (index == -1) return updateErrorStatus()
        val status = if (isStatus) {
            SmtpEmailStatus.Enable
        } else {
            SmtpEmailStatus.Disable
        }
        val model = smtpList[index].copy(status = status)
        blockScope?.launch {
            runCatching {
                updateSmtpConfigUseCase.invoke(model)
            }.onSuccess {
                smtpList = smtpList.toMutableList().apply {
                    set(index, model)
                }
                updateSuccessStatus()
            }.onFailure {
                updateErrorStatus()
            }
        }
    }

    private fun updateErrorStatus() {
        onEvent(listeningConfigMapper.mapSnackBarEventStatusError())
        updateBlockState()
    }

    private fun updateSuccessStatus() {
        onEvent(listeningConfigMapper.mapSnackBarEventStatusSuccess())
        updateBlockState()
    }

    interface Provider {
        fun isListening(): Boolean
        fun onConfigSizes(list: List<Boolean>)
    }
}
