package com.sms.checker.forwarder.main

import com.sms.checker.forwarder.main.presentation.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object MainModule {
    fun get() = module {
        viewModelOf(::MainViewModel)
    }
}