package com.example.notificationblocker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "group_app",
    primaryKeys = ["group_id", "app_id"],
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
    )
data class GroupToApp (
    @ColumnInfo(name="group_id")
    val groupId: Int,
    @ColumnInfo(name="app_id")
    val appId: String

)