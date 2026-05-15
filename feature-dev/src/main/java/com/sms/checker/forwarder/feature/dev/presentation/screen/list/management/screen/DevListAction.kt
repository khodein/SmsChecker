package com.sms.checker.forwarder.feature.dev.presentation.screen.list.management.screen

import androidx.compose.runtime.Immutable

@Immutable
data class DevListAction(
    val onClickDevListItem: (id: String) -> Unit
)