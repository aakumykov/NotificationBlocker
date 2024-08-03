package com.example.notificationblocker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Group::class], version = 1)
abstract class NotificationBlockerDatabase : RoomDatabase() {
    abstract val groupDao: GroupDao;

    companion object {
        @Volatile
        private var Instance: NotificationBlockerDatabase? = null

        fun getDatabase(context: Context): NotificationBlockerDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    NotificationBlockerDatabase::class.java,
                    "notification_blocker"
                ).fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }
    }
}