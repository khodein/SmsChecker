package com.sms.checker.forwarder.db

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object SmsCheckerDatabaseModule {
    fun get() = module {
        single {
            Room.databaseBuilder(
                context = androidContext(),
                klass = SmsCheckerDatabase::class.java,
                name = "sms_checker_forwarder_db"
            )
                .build()
        }

        single { get<SmsCheckerDatabase>().smsDao() }
        single { get<SmsCheckerDatabase>().smtpEmailDao() }
    }
}
