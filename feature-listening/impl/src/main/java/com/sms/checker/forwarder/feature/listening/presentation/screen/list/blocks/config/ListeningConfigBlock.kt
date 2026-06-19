package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config

import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailStatus
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.UpdateSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.router.EmailRouter
import com.sms.checker.forwarder.feature.listening.domain.usecase.GetListeningUseCase
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.mapper.ListeningConfigMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.framework.block.Block
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ListeningConfigBlock(
    private val listeningConfigMapper: ListeningConfigMapper,
    private val navEmailRouter: EmailRouter,
    private val getSmtpConfigUseCase: GetSmtpConfigUseCase,
    private val updateSmtpConfigUseCase: UpdateSmtpConfigUseCase,
    private val getListeningUseCase: GetListeningUseCase,
) : Block<ListeningConfigState, ListeningConfigAction, Unit>() {

    private var loadSmtpJob: Job? = null
    private var smtpList: List<SmtpEmailModel> = emptyList()

    override val action = ListeningConfigAction(
        onClickEmpty = ::onClickAddNew,
        onClickConfig = ::onClickConfig,
        onChangeConfigStatus = ::onChangeConfigStatus
    )

    override fun getInitialUiState(): ListeningConfigState {
        return buildState()
    }

    override fun updateBlockState() {
        setState { buildState() }
    }

    private fun buildState(): ListeningConfigState {
        return listeningConfigMapper.map(smtpList)
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
        if (getListeningUseCase.invoke()) {
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
}
