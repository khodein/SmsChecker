package com.sms.checker.forwarder.router

import com.sms.checker.forwarder.framework.router.Router
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

data object RouterModule {

    fun get() = module {
        singleOf(::RouterImpl) bind Router::class
    }
}