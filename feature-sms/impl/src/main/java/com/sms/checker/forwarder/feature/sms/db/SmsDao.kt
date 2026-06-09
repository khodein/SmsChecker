package com.sms.checker.forwarder.feature.sms.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sms.checker.forwarder.feature.sms.db.entity.SmsEntity
import com.sms.checker.forwarder.feature.sms.db.entity.SmsWithForwardsEntity

@Dao
interface SmsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(entities: List<SmsEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: SmsEntity): Long

    @Update
    suspend fun update(entity: SmsEntity): Int

    @Query("SELECT * FROM sms_table WHERE id = :id")
    suspend fun getById(id: Long): SmsEntity?

    @Transaction
    @Query("SELECT * FROM sms_table WHERE id IN (:ids)")
    suspend fun getByIdsWithForwards(ids: List<Long>): List<SmsWithForwardsEntity>

    @Transaction
    @Query("SELECT * FROM sms_table ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getPageWithForwards(limit: Int, offset: Int): List<SmsWithForwardsEntity>
}