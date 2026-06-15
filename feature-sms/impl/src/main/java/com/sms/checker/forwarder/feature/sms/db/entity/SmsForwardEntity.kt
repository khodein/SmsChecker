package com.sms.checker.forwarder.feature.sms.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = SmsForwardEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = SmsEntity::class,
            parentColumns = ["id"],
            childColumns = ["sms_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("sms_id")],
)
data class SmsForwardEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "sms_id")
    val smsId: Long,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "config_id")
    val configId: Long,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "error")
    val error: String? = null,
    @ColumnInfo(name = "attempted_at")
    val attemptedAt: Long,
) {
    companion object {
        const val TABLE_NAME = "sms_forward_table"
    }
}