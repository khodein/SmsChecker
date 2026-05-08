package com.sms.checker.forwarder.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temporary")
data class TemporaryEntity(
    @PrimaryKey
    val id: Long
)
