package com.example.notificationblocker.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Group::class], version = 1)
abstract class NotificationBlockerDatabase: RoomDatabase(){
    abstract val groupDao: GroupDao;
}