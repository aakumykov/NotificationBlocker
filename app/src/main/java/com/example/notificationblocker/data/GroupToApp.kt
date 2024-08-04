package com.example.notificationblocker.data

import androidx.room.Entity

@Entity(tableName = "group_app", primaryKeys = ["groupId", "appId"])
data class GroupToApp (
    val groupId: Int,
    val appId: String

)