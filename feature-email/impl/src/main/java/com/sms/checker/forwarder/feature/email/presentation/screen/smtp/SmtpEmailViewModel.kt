package com.sms.checker.forwarder.feature.email.presentation.screen.smtp

import androidx.lifecycle.viewModelScope
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailFromModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailRecipientModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailServerModel
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailStatus
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailUserModel
import com.sms.checker.forwarder.feature.email.domain.usecase.DeleteSmtpConfigByIdUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpConfigByIdUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.SaveSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.domain.usecase.UpdateSmtpConfigUseCase
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.bottombar.SmtpBottomBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.from.SmtpEmailFromBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.name.SmtpEmailNameBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.recipient.SmtpEmailRecipientBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.server.SmtpEmailServerBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.SmtpEmailTestBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.test.state.SmtpEmailTestState
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.blocks.topbar.SmtpTopBarBlock
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.mapper.SmtpEmailMapper
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailAction
import com.sms.checker.forwarder.feature.email.presentation.screen.smtp.state.SmtpEmailState
import com.sms.checker.forwarder.framework.BaseViewModel
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.framework.router.Router
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SmtpEmailViewModel(
    private val smtpId: Long?,
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
    private val deleteSmtpConfigByIdUseCase: DeleteSmtpConfigByIdUseCase,
    private val mapper: SmtpEmailMapper,
    private val router: Router,
) : BaseViewModel<SmtpEmailState, SmtpEmailAction>(),
    SmtpBottomBarBlock.Provider,
    SmtpEmailTestBlock.Provider,
    SmtpTopBarBlock.Provider {

    private var model: SmtpEmailModel? = null
    private var status: Status = Status.IDLE

    private var loadById: Job? = null
    private var deleteById: Job? = null

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

    override fun startBlocks() {
        super.startBlocks()
        loadById()
    }

    private fun loadById() {
        val id = smtpId ?: return
        smtpBottomBarBlock.setIsUpdate(true)
        loadById?.cancel()
        loadById = viewModelScope.launch {
            status = Status.LOADING
            updateViewState()
            runCatching {
                getSmtpConfigByIdUseCase.invoke(id)
            }.onSuccess { model ->
                smtpEmailNameBlock.onChangeValue(model.name)
                smtpTopBarBlock.onChangeValue(model.status)
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
                status = Status.SUCCESS
                updateViewState()
            }.onFailure {
                smtpBottomBarBlock.setIsUpdate(false)
                status = Status.ERROR
                updateViewState()
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
            add(smtpTopBarBlock, this@SmtpEmailViewModel)
            add(smtpBottomBarBlock, this@SmtpEmailViewModel)
            add(smtpEmailTestBlock, this@SmtpEmailViewModel)
            add(smtpEmailNameBlock)
            add(smtpEmailServerBlock)
            add(smtpEmailFromBlock)
            add(smtpEmailRecipientBlock)
        }
    }

    override fun getRequired(): Map<SmtpBottomBarBlock.Required, Boolean> {
        return mapOf(
            SmtpBottomBarBlock.Required.Name to smtpEmailNameBlock.isRequiredState,
            SmtpBottomBarBlock.Required.Server to smtpEmailServerBlock.isRequiredState,
            SmtpBottomBarBlock.Required.From to smtpEmailFromBlock.isRequiredState,
            SmtpBottomBarBlock.Required.Recipient to smtpEmailRecipientBlock.isRequiredState,
            SmtpBottomBarBlock.Required.Test to smtpEmailTestBlock.isRequiredState
        )
    }

    override fun onRequired() {
        val model = SmtpEmailModel(
            id = model?.id,
            status = smtpTopBarBlock.status ?: SmtpEmailStatus.Disable,
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
        status = Status.LOADING
        updateViewState()
        viewModelScope.launch {
            runCatching {
                saveSmtpConfigUseCase.invoke(model)
            }.onSuccess {
                updateSaveSuccess()
            }.onFailure {
                updateSaveError()
            }
        }
    }

    override fun onUpdate() {
        val model = this@SmtpEmailViewModel.model ?: return
        status = Status.LOADING
        updateViewState()
        viewModelScope.launch {
            runCatching {
                updateSmtpConfigUseCase.invoke(model)
            }.onSuccess { _ ->
                updateSaveSuccess()
            }.onFailure {
                updateSaveError()
            }
        }
    }

    override fun onChangeStatus() {
        val model = this@SmtpEmailViewModel.model ?: return
        this@SmtpEmailViewModel.model?.id ?: return
        val status = smtpTopBarBlock.status ?: return
        this@SmtpEmailViewModel.model = model.copy(status = status)
        onUpdate()
    }

    override fun onClickDelete() {
        val id = smtpId ?: return
        deleteById?.cancel()
        deleteById = viewModelScope.launch {
            runCatching {
                deleteSmtpConfigByIdUseCase.invoke(id)
            }.onSuccess {
                updateDeleteSuccess()
            }.onFailure {
                updateDeleteError()
            }
        }
    }

    override fun onReload() {
        smtpBottomBarBlock.onClickAdd()
    }

    private fun updateSaveSuccess() {
        onEvent(mapper.mapSuccessEvent())
        router.goBack()
    }

    private fun updateSaveError() {
        onEvent(mapper.mapErrorEvent())
    }

    private fun updateDeleteSuccess() {
        onEvent(mapper.mapDeleteSuccess())
        router.goBack()
    }

    private fun updateDeleteError() {
        onEvent(mapper.mapDeleteError())
    }
}