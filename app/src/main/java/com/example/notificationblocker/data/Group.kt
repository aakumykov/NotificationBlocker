package com.example.notificationblocker.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "groups")
data class Group(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val active: Boolean = false,
)