package com.sms.checker.forwarder.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sms.checker.forwarder.feature.sms.db.SmsDao
import com.sms.checker.forwarder.feature.sms.db.entity.SmsEntity

@Database(
    entities = [
        SmsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class SmsCheckerDatabase : RoomDatabase() {

    abstract fun smsDao(): SmsDao
}