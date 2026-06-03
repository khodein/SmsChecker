package com.sms.checker.forwarder.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sms.checker.forwarder.feature.email.db.SmtpEmailDao
import com.sms.checker.forwarder.feature.email.db.entity.SmtpEmailEntity
import com.sms.checker.forwarder.feature.sms.db.SmsDao
import com.sms.checker.forwarder.feature.sms.db.entity.SmsEntity

@Database(
    entities = [
        SmsEntity::class,
        SmtpEmailEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class SmsCheckerDatabase : RoomDatabase() {
    abstract fun smsDao(): SmsDao
    abstract fun smtpEmailDao(): SmtpEmailDao
}