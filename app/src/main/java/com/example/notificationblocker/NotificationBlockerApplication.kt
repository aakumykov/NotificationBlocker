package com.example.notificationblocker

import android.app.Application
import com.example.notificationblocker.data.NotificationBlockerDatabase

class NotificationBlockerApplication: Application() {
    lateinit var database: NotificationBlockerDatabase

    override fun onCreate() {
        super.onCreate()
        database = NotificationBlockerDatabase.getDatabase(this)
    }
}