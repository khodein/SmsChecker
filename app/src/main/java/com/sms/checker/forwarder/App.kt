package com.sms.checker.forwarder

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.sms.checker.forwarder.feature.dev.DevModule
import com.sms.checker.forwarder.tools.ResModule
import com.sms.checker.forwarder.main.MainViewModel
import com.sms.checker.forwarder.router.Router
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
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
                ResModule.get(),
                DevModule.get()
            )
        }

        private val rootModule = module {
            viewModelOf(::MainViewModel)
            singleOf(::RouterImpl) bind Router::class
        }

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

        private class RouterImpl : Router {
            private val backStack: SnapshotStateList<NavKey> = mutableStateListOf()

            override fun getBackStack(): List<NavKey> = backStack

            override fun goTo(key: NavKey) {
                backStack.add(key)
            }

            override fun goBack() {
                backStack.removeLastOrNull()
            }
        }
    }
}