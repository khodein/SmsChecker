package com.sms.checker.forwarder

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.sms.checker.forwarder.router.routerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

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

    private object AppModule {

        fun get(): List<Module> {
            return listOf(
                rootModule,
                routerModule,
            )
        }

        private val rootModule = module {}

        fun newImageLoader(
            context: PlatformContext,
        ): ImageLoader {
            return ImageLoader.Builder(context).memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(
                        context = context,
                        percent = 0.25
                    )
                    .build()
            }
                .crossfade(true)
                .build()
        }
    }
}