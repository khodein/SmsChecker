package com.sms.checker.forwarder.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TemporaryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SmsCheckerDatabase : RoomDatabase()
