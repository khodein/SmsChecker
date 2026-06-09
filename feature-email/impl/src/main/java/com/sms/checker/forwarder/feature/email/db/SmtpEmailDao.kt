package com.sms.checker.forwarder.feature.email.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sms.checker.forwarder.feature.email.db.entity.SmtpEmailEntity

@Dao
interface SmtpEmailDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SmtpEmailEntity): Long

    @Update
    suspend fun update(entity: SmtpEmailEntity): Int

    @Query("SELECT * FROM smtp_email_table WHERE id = :id")
    suspend fun getById(id: Long): SmtpEmailEntity?

    @Query("SELECT * FROM smtp_email_table")
    suspend fun getAll(): List<SmtpEmailEntity>

    @Query("SELECT * FROM smtp_email_table WHERE status = 'Enable'")
    suspend fun getAllEnabled(): List<SmtpEmailEntity>

    @Query("SELECT id FROM smtp_email_table WHERE status = 'Enable'")
    suspend fun getAllEnabledIds(): List<Long>

    @Query("DELETE FROM smtp_email_table WHERE id = :id")
    suspend fun deleteById(id: Long)
}
