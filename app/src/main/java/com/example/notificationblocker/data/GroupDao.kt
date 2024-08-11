package com.example.notificationblocker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Insert
    suspend fun insert(group: Group): Long

    @Update
    suspend fun update(group: Group)

    @Query("SELECT * FROM groups ORDER BY name COLLATE NOCASE ASC")
    fun getAll(): Flow<List<Group>>

    @Query("SELECT * from groups WHERE id = :id")
    fun get(id: Int): Flow<Group>

    @Delete
    suspend fun delete(group: Group)
}