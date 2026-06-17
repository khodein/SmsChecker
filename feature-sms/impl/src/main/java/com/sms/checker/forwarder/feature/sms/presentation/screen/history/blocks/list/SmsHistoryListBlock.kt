package com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list

import com.sms.checker.forwarder.feature.sms.domain.model.SmsWithForwardsModel
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.GetSmsPageWithForwardsUseCase
import com.sms.checker.forwarder.feature.sms.domain.usecase.with_forward.ObserveLastSmsWithForwardsUseCase
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.mapper.SmsHistoryListMapper
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.state.SmsHistoryListAction
import com.sms.checker.forwarder.feature.sms.presentation.screen.history.blocks.list.state.SmsHistoryListState
import com.sms.checker.forwarder.framework.PageState
import com.sms.checker.forwarder.framework.Status
import com.sms.checker.forwarder.framework.block.Block
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class SmsHistoryListBlock(
    private val smsHistoryListMapper: SmsHistoryListMapper,
    private val getSmsPageWithForwardsUseCase: GetSmsPageWithForwardsUseCase,
    private val observeLastSmsWithForwardsUseCase: ObserveLastSmsWithForwardsUseCase
) : Block<SmsHistoryListState, SmsHistoryListAction, Unit>() {

    private var page: Int = 0
    private var status: Status = Status.IDLE
    private var isEndReached: Boolean = true
    private var loadJob: Job? = null

    private var list: List<SmsWithForwardsModel> = emptyList()
    override val action: SmsHistoryListAction = SmsHistoryListAction(
        onClickItem = ::onClickItem,
        onNextLoad = ::load,
        onClickReload = ::load
    )

    override fun getInitialUiState(): SmsHistoryListState {
        return buildState()
    }

    override fun updateBlockState() {
        setState { buildState() }
    }

    private fun buildState(): SmsHistoryListState {
        return SmsHistoryListState(
            pageState = getPageState(),
            error = smsHistoryListMapper.mapError()
        )
    }

    private fun getPageState(): PageState<SmsHistoryListState.ItemState> {
        return PageState(
            items = smsHistoryListMapper.mapList(list = list),
            page = page,
            status = status,
            isEndReached = isEndReached
        )
    }

    override fun startBlock() {
        super.startBlock()
        load()
        startObservable()
    }

    private fun startObservable() {
        blockScope?.launch {
            observeLastSmsWithForwardsUseCase.invoke(1).collectLatest {
                load(0)
            }
        }
    }

    private fun load(page: Int = this.page) {
        this.page = page
        if (page == 0) list = emptyList()
        loadJob?.cancel()
        loadJob = blockScope?.launch {
            updateLoading()
            runCatching {
                getSmsPageWithForwardsUseCase.invoke(
                    limit = LIMIT,
                    offset = LIMIT * page
                )
            }
                .onSuccess(::updateSuccess)
                .onFailure(::updateError)
        }
    }

    private fun updateSuccess(list: List<SmsWithForwardsModel>) {
        if (list.isEmpty()) {
            status = Status.IDLE
            isEndReached = true
        } else {
            status = Status.SUCCESS
            isEndReached = false
        }

        this@SmsHistoryListBlock.list += list
        updateBlockState()
    }

    private fun updateError(error: Throwable) {
        updateError()
    }

    private fun updateLoading() {
        status = Status.LOADING
        isEndReached = true
        setState {
            copy(
                pageState = pageState.copy(
                    status = this@SmsHistoryListBlock.status,
                    isEndReached = this@SmsHistoryListBlock.isEndReached
                )
            )
        }
    }

    private fun updateError() {
        status = Status.ERROR
        isEndReached = true
        setState {
            copy(
                pageState = pageState.copy(
                    status = this@SmsHistoryListBlock.status,
                    isEndReached = this@SmsHistoryListBlock.isEndReached
                )
            )
        }
    }

    private fun onClickItem(id: Long) {

    }

    private companion object {
        const val LIMIT = 10
    }
}