package com.sms.checker.forwarder.db

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE smtp_email_table ADD COLUMN forwarding_sources TEXT NOT NULL DEFAULT ''")
    }
}

object SmsCheckerDatabaseModule {
    fun get() = module {
        single {
            Room.databaseBuilder(
                context = androidContext(),
                klass = SmsCheckerDatabase::class.java,
                name = "sms_checker_forwarder_db"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        single { get<SmsCheckerDatabase>().smsDao() }
        single { get<SmsCheckerDatabase>().smtpEmailDao() }
    }
}