package com.sms.checker.forwarder.feature.sms.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sms_table")
data class SmsEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "sender")
    val sender: String,
    @ColumnInfo(name = "body")
    val body: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "forwarded_to")
    val forwardedTo: String = "",
    @ColumnInfo(name = "forward_status")
    val forwardStatus: String,
)