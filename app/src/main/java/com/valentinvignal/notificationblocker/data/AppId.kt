package com.valentinvignal.notificationblocker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class AppId (
    @PrimaryKey()
    val id: String,
)