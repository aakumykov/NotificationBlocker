package com.example.notificationblocker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MyNotificationListenerService : NotificationListenerService() {

    private val channelId = "MyNotificationListenerService"

    override fun onCreate() {
        super.onCreate()
        Log.d("NB", "MyNotificationListenerService.onCreate")
        // Add code to block notifications here
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Your code here to block notifications
        Log.d("NB", "MyNotificationListenerService.onStartCommand")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("NB", "MyNotificationListenerService.onBind")
        return super.onBind(intent)
    }

    override fun onDestroy() {
        Log.d("NB", "MyNotificationListenerService.onDestroy")
        return super.onDestroy()
        // Add code to unblock notifications here
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Block notifications
        val text = sbn.notification?.extras?.getString("android.text");
        Log.d("NB", "MyNotificationListenerService.onNotificationPosted - text: $text")
        super.onNotificationPosted(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Optionally handle notification removal
    }

    override fun onListenerConnected() {
        Log.d("NB", "MyNotificationListenerService.onListenerConnected")
    }
}
