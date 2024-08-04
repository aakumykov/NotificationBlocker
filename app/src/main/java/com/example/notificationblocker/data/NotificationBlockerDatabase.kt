package com.example.notificationblocker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Group::class, GroupToApp::class], version = 2, exportSchema = false)
abstract class NotificationBlockerDatabase : RoomDatabase() {
    abstract val groupDao: GroupDao;
    abstract val groupToAppDao: GroupToAppDao;

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