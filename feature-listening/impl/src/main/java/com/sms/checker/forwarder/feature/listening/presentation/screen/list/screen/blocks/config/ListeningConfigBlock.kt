package com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config

import com.sms.checker.forwarder.feature.email.domain.model.ForwardingSource
import com.sms.checker.forwarder.feature.email.domain.model.SmtpEmailModel
import com.sms.checker.forwarder.feature.email.domain.usecase.GetSmtpEmailByForwardingSourceUseCase
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.mapper.ListeningConfigMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.screen.blocks.config.state.ListeningConfigState
import com.sms.checker.forwarder.framework.block.BaseBlock
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

internal class ListeningConfigBlock(
    private val listeningConfigMapper: ListeningConfigMapper,
    private val getSmtpEmailByForwardingSourceUseCase: GetSmtpEmailByForwardingSourceUseCase,
) : BaseBlock<ListeningConfigState, Unit>() {

    private val action = ListeningConfigAction(
        onClickEmpty = ::onClickEmpty,
        onClickItem = ::onClickItem,
    )

    private var smtpEmailList: List<SmtpEmailModel>? = null

    override fun getInitialUiState(): ListeningConfigState {
        return listeningConfigMapper.mapConfigState(action = action)
    }

    override fun start() {
        load()
    }

    fun reload() {
        load()
    }

    override fun updateBlockState() {
        setState {
            listeningConfigMapper.mapConfigState(
                action = action,
                smtpEmailList = smtpEmailList.orEmpty()
            )
        }
    }

    private fun onClickEmpty() {

    }

    private fun onClickItem(id: String) {

    }

    private fun load() {
        blockScope?.launch {
            val smsConfigAsync = async {
                fetchSmsConfig()
            }

            listOf(
                smsConfigAsync,
            ).awaitAll()
        }
    }

    private suspend fun fetchSmsConfig() {
        runCatching {
            getSmtpEmailByForwardingSourceUseCase.invoke(ForwardingSource.SMS)
        }.onSuccess { list ->
            smtpEmailList = list
            updateBlockState()
        }.onFailure {
            smtpEmailList = null
            updateBlockState()
        }
    }
}