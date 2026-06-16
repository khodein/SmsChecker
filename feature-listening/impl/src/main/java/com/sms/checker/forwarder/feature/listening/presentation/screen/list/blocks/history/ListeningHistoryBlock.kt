package com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history

import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.mapper.ListeningHistoryMapper
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state.ListeningHistoryAction
import com.sms.checker.forwarder.feature.listening.presentation.screen.list.blocks.history.state.ListeningHistoryState
import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.GetLastSmsWithForwardsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.ObserveLastSmsWithForwardsUseCase
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.framework.block.Block
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ListeningHistoryBlock(
    private val getLastSmsWithForwardsUseCase: GetLastSmsWithForwardsUseCase,
    private val observeLastSmsWithForwardsUseCase: ObserveLastSmsWithForwardsUseCase,
    private val listeningHistoryMapper: ListeningHistoryMapper,
) : Block<ListeningHistoryState, ListeningHistoryAction, Unit>() {

    private var loadJob: Job? = null
    private var list: List<SmsWithForwardsModel> = emptyList()
    private var status: Status = Status.IDLE

    override val action = ListeningHistoryAction(
        onClickItem = ::onClickItem,
        onClickAll = ::onClickAll,
        onClickReload = ::onClickReload
    )

    override fun getInitialUiState(): ListeningHistoryState {
        return buildState()
    }

    override fun updateBlockState() {
        setState { buildState() }
    }

    override fun startBlock() {
        super.startBlock()
        startObservable()
        load()
    }

    private fun startObservable() {
        blockScope?.launch {
            observeLastSmsWithForwardsUseCase.invoke().collect {
                load()
            }
        }
    }

    private fun buildState(): ListeningHistoryState {
        return ListeningHistoryState(
            status = status,
            items = listeningHistoryMapper.mapItems(list),
            button = listeningHistoryMapper.mapButton(),
            empty = listeningHistoryMapper.mapEmpty(),
            error = listeningHistoryMapper.mapError()
        )
    }

    private fun load() {
        loadJob?.cancel()
        loadJob = blockScope?.launch {
            runCatching {
                getLastSmsWithForwardsUseCase.invoke()
            }
                .onSuccess(::updateSuccess)
                .onFailure(::updateError)
        }
    }

    private fun updateSuccess(list: List<SmsWithForwardsModel>) {
        status = Status.SUCCESS
        this.list = list
        updateBlockState()
    }

    private fun updateError(error: Throwable) {
        status = Status.ERROR
        list = emptyList()
        updateBlockState()
    }

    private fun onClickReload() {
        load()
    }

    private fun onClickItem(id: Long) {

    }

    private fun onClickAll() {

    }
}