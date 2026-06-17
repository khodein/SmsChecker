package com.sms.checker.forwarder.framework

import androidx.compose.runtime.Stable

@Stable
data class PageState<T>(
    val items: List<T>,
    val page: Int = 0,
    val status: Status = Status.IDLE,
    val isEndReached: Boolean = false,
)