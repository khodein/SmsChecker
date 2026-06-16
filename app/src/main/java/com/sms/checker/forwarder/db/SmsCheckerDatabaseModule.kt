package com.sms.checker.forwarder.db

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal object SmsCheckerDatabaseModule {
    fun get() = module {
        single {
            Room.databaseBuilder(
                context = androidContext(),
                klass = SmsCheckerDatabase::class.java,
                name = "sms_checker_forwarder_db"
            )
                .fallbackToDestructiveMigration(false)
                .build()
        }
        single<RoomDatabase> { get<SmsCheckerDatabase>() }

        single { get<SmsCheckerDatabase>().smsDao() }
        single { get<SmsCheckerDatabase>().smsForwardDao() }
        single { get<SmsCheckerDatabase>().smtpEmailDao() }
    }
}
