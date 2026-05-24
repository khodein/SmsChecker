package com.sms.checker.forwarder.feature.sms.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sms.checker.forwarder.feature.sms.db.entity.SmsEntity

@Dao
interface SmsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<SmsEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllReturnIds(entities: List<SmsEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: SmsEntity): Long

    @Query("SELECT * FROM sms_table WHERE id = :id")
    suspend fun getById(id: Long): SmsEntity?

    @Query("SELECT * FROM sms_table WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Long>): List<SmsEntity>

    @Query("SELECT * FROM sms_table ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getPage(limit: Int, offset: Int): List<SmsEntity>
}