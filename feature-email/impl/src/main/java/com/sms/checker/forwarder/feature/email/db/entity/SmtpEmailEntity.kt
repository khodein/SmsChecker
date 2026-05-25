package com.sms.checker.forwarder.feature.email.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "smtp_email_table")
class SmtpEmailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "host")
    val host: String,
    @ColumnInfo(name = "port")
    val port: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "password")
    val password: String,
    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean,
    @ColumnInfo(name = "from_email")
    val fromEmail: String,
    @ColumnInfo(name = "from_name")
    val fromName: String,
    @ColumnInfo(name = "ssl_enabled")
    val sslEnabled: Boolean,
    @ColumnInfo(name = "start_tls_enabled")
    val startTlsEnabled: Boolean,
    @ColumnInfo(name = "forwarding_sources")
    val forwardingSources: String,
)