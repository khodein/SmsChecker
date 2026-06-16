package com.sms.checker.forwarder

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.memory.MemoryCache
import coil3.request.crossfade
import com.sms.checker.forwarder.db.SmsCheckerDatabaseModule
import com.sms.checker.forwarder.feature.email.EmailModule
import com.sms.checker.forwarder.feature.listening.ListeningModule
import com.sms.checker.forwarder.feature.settings.SettingsModule
import com.sms.checker.forwarder.feature.sms.SmsModule
import com.sms.checker.forwarder.framework.tools.ResModule
import com.sms.checker.forwarder.main.MainModule
import com.sms.checker.forwarder.router.RouterModule
import org.koin.core.module.Module

object AppModule {

    fun get(): List<Module> {
        return listOf(
            RouterModule.get(),
            SmsCheckerDatabaseModule.get(),
            MainModule.get(),
            ResModule.get(),
            ListeningModule.get(),
            SmsModule.get(),
            EmailModule.get(),
            SettingsModule.get()
        )
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
}
