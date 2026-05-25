package com.sms.checker.forwarder.router

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import com.sms.checker.forwarder.framework.router.Router

class RouterImpl : Router {
    private val backStack: SnapshotStateList<NavKey> = mutableStateListOf()

    override fun getBackStack(): List<NavKey> = backStack

    override fun goTo(key: NavKey) {
        backStack.add(key)
    }

    override fun goBack() {
        backStack.removeLastOrNull()
    }
}