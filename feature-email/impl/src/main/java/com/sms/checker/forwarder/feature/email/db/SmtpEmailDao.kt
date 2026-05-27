package com.sms.checker.forwarder.feature.email.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sms.checker.forwarder.feature.email.db.entity.SmtpEmailEntity

@Dao
interface SmtpEmailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SmtpEmailEntity): Long

    @Query("SELECT * FROM smtp_email_table WHERE id = :id")
    suspend fun getById(id: Long): SmtpEmailEntity?

    @Query("SELECT * FROM smtp_email_table")
    suspend fun getAll(): List<SmtpEmailEntity>

    @Query("DELETE FROM smtp_email_table WHERE id = :id")
    suspend fun deleteById(id: Long)
}
