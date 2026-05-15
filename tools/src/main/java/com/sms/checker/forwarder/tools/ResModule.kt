package com.sms.checker.forwarder.tools

import com.sms.checker.forwarder.tools.res.ResProvider
import com.sms.checker.forwarder.tools.res.ResProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ResModule {
    fun get() = module {
        single<ResProvider> { ResProviderImpl(androidContext()) }
    }
}