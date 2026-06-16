package com.sms.checker.forwarder.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sms.checker.forwarder.feature.email.db.SmtpEmailDao
import com.sms.checker.forwarder.feature.email.db.entity.SmtpEmailEntity
import com.sms.checker.forwarder.feature.sms.db.SmsDao
import com.sms.checker.forwarder.feature.sms.db.SmsForwardDao
import com.sms.checker.forwarder.feature.sms.db.entity.SmsEntity
import com.sms.checker.forwarder.feature.sms.db.entity.SmsForwardEntity

@Database(
    entities = [
        SmsEntity::class,
        SmsForwardEntity::class,
        SmtpEmailEntity::class,
    ],
    version = 1,
    exportSchema = false
)
internal abstract class SmsCheckerDatabase : RoomDatabase() {
    abstract fun smsDao(): SmsDao
    abstract fun smsForwardDao(): SmsForwardDao
    abstract fun smtpEmailDao(): SmtpEmailDao
}