package com.sms.checker.forwarder

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application(), SingletonImageLoader.Factory {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(AppModule.get())
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return AppModule.newImageLoader(context)
    }
}