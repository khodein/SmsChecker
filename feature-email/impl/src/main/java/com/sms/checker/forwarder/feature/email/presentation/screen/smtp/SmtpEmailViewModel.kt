package com.sms.checker.forwarder.feature.email.presentation.screen.smtp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailFromModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailRecipientModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailServerModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailUserModel
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpConfigByIdUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.SaveSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.UpdateSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.SmtpBottomBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.SmtpEmailNameBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.SmtpEmailFromBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.SmtpEmailRecipientBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.SmtpEmailServerBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.SmtpEmailTestBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.SmtpTopBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.mapper.SmtpEmailMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailEvent
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailState
import com.sms.checker.forwarder.framework.BaseViewModel
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.framework.UiEvent
import com.sms.checker.forwarder.framework.router.Router
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SmtpEmailViewModel(
    private val smtpBottomBarBlock: SmtpBottomBarBlock,
    private val smtpTopBarBlock: SmtpTopBarBlock,
    private val smtpEmailNameBlock: SmtpEmailNameBlock,
    private val smtpEmailServerBlock: SmtpEmailServerBlock,
    private val smtpEmailFromBlock: SmtpEmailFromBlock,
    private val smtpEmailRecipientBlock: SmtpEmailRecipientBlock,
    private val smtpEmailTestBlock: SmtpEmailTestBlock,
    private val saveSmtpConfigUseCase: SaveSmtpConfigUseCase,
    private val updateSmtpConfigUseCase: UpdateSmtpConfigUseCase,
    private val getSmtpConfigByIdUseCase: GetSmtpConfigByIdUseCase,
    private val mapper: SmtpEmailMapper,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<SmtpEmailState, SmtpEmailAction>(savedStateHandle),
    SmtpBottomBarBlock.Provider,
    SmtpEmailTestBlock.Provider {

    private var model: SmtpEmailModel? = null
    private var status: Status = Status.IDLE

    private var loadById: Job? = null

    override val action: SmtpEmailAction = SmtpEmailAction(
        bottomBarAction = smtpBottomBarBlock.action,
        topBarAction = smtpTopBarBlock.action,
        nameAction = smtpEmailNameBlock.action,
        serverAction = smtpEmailServerBlock.action,
        testAction = smtpEmailTestBlock.action,
        fromAction = smtpEmailFromBlock.action,
        recipientAction = smtpEmailRecipientBlock.action,
    )

    init {
        attach()
    }

    fun setSmtpId(id: Long?) {
        if (id == null || model != null) return
        smtpBottomBarBlock.setIsUpdate(true)
        smtpTopBarBlock.setIsUpdate(true)
        loadById?.cancel()
        loadById = viewModelScope.launch {
            updateLoading()
            runCatching {
                getSmtpConfigByIdUseCase.invoke(id)
            }.onSuccess { model ->
                smtpEmailNameBlock.onChangeValue(model.name)
                smtpEmailServerBlock.run {
                    onChangeUserName(model.user.name)
                    onChangePassword(model.user.password)
                    onChangePort(model.server.port)
                    onChangeHost(model.server.host)
                }
                smtpEmailFromBlock.run {
                    onChangeEmail(model.from.email)
                    onChangeName(model.from.name)
                }
                smtpEmailRecipientBlock.run {
                    onChangeEmail(model.recipient.email)
                    onChangeSubject(model.recipient.subject)
                }
                this@SmtpEmailViewModel.model = model
                updateSuccess(id)
            }.onFailure {
                smtpBottomBarBlock.setIsUpdate(false)
                updateError(it)
            }
        }
    }

    override fun getInitialUiState(): SmtpEmailState = buildState()

    override fun updateViewState() {
        setState { buildState() }
    }

    private fun buildState() = SmtpEmailState(
        status = status,
        smtpBottomBarState = smtpBottomBarBlock.blockState.value,
        smtpTopBarState = smtpTopBarBlock.blockState.value,
        nameState = smtpEmailNameBlock.blockState.value,
        serverState = smtpEmailServerBlock.blockState.value,
        fromState = smtpEmailFromBlock.blockState.value,
        recipientState = smtpEmailRecipientBlock.blockState.value,
        testState = smtpEmailTestBlock.blockState.value.takeIf {
            it.status != SmtpEmailTestState.TestStatus.None
        },
    )

    override fun attach() {
        registerBlocks {
            add(smtpTopBarBlock)
            add(smtpBottomBarBlock, this@SmtpEmailViewModel)
            add(smtpEmailNameBlock)
            add(smtpEmailServerBlock)
            add(smtpEmailFromBlock)
            add(smtpEmailRecipientBlock)
            add(smtpEmailTestBlock, this@SmtpEmailViewModel)
        }
    }

    override fun getRequired(): Map<SmtpBottomBarBlock.Required, Boolean> {
        return mapOf(
            SmtpBottomBarBlock.Required.Name to smtpEmailNameBlock.isRequired(),
            SmtpBottomBarBlock.Required.Server to smtpEmailServerBlock.isRequired(),
            SmtpBottomBarBlock.Required.From to smtpEmailFromBlock.isRequired(),
            SmtpBottomBarBlock.Required.Recipient to smtpEmailRecipientBlock.isRequired(),
            SmtpBottomBarBlock.Required.Test to smtpEmailTestBlock.isRequired()
        )
    }

    override fun onRequired() {
        val model = SmtpEmailModel(
            name = smtpEmailNameBlock.blockState.value.value,
            server = SmtpEmailServerModel(
                host = smtpEmailServerBlock.blockState.value.hostState.value,
                port = smtpEmailServerBlock.blockState.value.portState.value
            ),
            user = SmtpEmailUserModel(
                name = smtpEmailServerBlock.blockState.value.userState.nameState.value,
                password = smtpEmailServerBlock.blockState.value.userState.passwordState.value,
            ),
            from = SmtpEmailFromModel(
                email = smtpEmailFromBlock.blockState.value.emailState.value,
                name = smtpEmailFromBlock.blockState.value.nameState.value,
            ),
            recipient = SmtpEmailRecipientModel(
                email = smtpEmailRecipientBlock.blockState.value.emailState.value,
                subject = smtpEmailRecipientBlock.blockState.value.subjectState.value,
            ),
        ).also {
            this@SmtpEmailViewModel.model = it
        }

        smtpEmailTestBlock.onTest(model)
    }

    override fun onSave() {
        val model = this@SmtpEmailViewModel.model ?: return
        viewModelScope.launch {
            runCatching {
                saveSmtpConfigUseCase.invoke(model)
            }
                .onSuccess {
                    onEvent(mapper.mapSuccessEvent())
                }
                .onFailure {
                    onEvent(mapper.mapErrorEvent())
                }
        }
    }

    override fun onReload() {
        action.bottomBarAction.onClickAdd.invoke()
    }

    override fun onUpdate() {
        val model = this@SmtpEmailViewModel.model ?: return
        updateLoading()
        viewModelScope.launch {
            runCatching {
                updateSmtpConfigUseCase.invoke(model)
            }
                .onSuccess { id ->
                    onEvent(mapper.mapSuccessEvent())
                }
                .onFailure {
                    onEvent(mapper.mapErrorEvent())
                }
        }
    }

    private fun updateLoading() {
        status = Status.LOADING
        updateViewState()
    }

    private fun updateSuccess(id: Long) {
        status = Status.SUCCESS
        updateViewState()
    }

    private fun updateError(throwable: Throwable) {
        status = Status.ERROR
        updateViewState()
    }
}