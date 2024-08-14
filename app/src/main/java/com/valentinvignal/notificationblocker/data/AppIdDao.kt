package com.valentinvignal.notificationblocker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppIdDao {

    @Insert
    suspend fun insert(appId: AppId)

    @Delete
    suspend fun delete(appId: AppId)

    @Query("SELECT * FROM applications")
    fun getAll(): Flow<List<AppId>>
}