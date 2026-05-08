package com.sms.checker.forwarder.router

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

typealias EntryProviderInstaller = EntryProviderScope<NavKey>.() -> Unit


val routerModule = module {
    singleOf(::RouterImpl) bind Router::class
}

interface Router {
    fun getBackStack(): List<NavKey>
    fun goTo(key: NavKey)
    fun goBack()

    interface Provider {
        operator fun invoke(): EntryProviderInstaller
    }
}


private class RouterImpl : Router {
    private val backStack: SnapshotStateList<NavKey> = mutableStateListOf()

    init {

    }

    override fun getBackStack(): List<NavKey> {
        return backStack
    }

    override fun goTo(key: NavKey) {
        backStack.add(key)
    }

    override fun goBack() {
        backStack.removeLastOrNull()
    }
}