package com.example.notificationblocker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Upsert
    suspend fun upsert(group: Group);

    @Query("SELECT * FROM groups ORDER BY name ASC")
    fun getAll(): Flow<List<Group>>

    @Query("SELECT * from groups WHERE id = :id")
    fun get(id: Int): Flow<Group>

    @Delete
    suspend fun delete(group: Group)
}