package com.example.notificationblocker

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationBlockerListenerService : NotificationListenerService() {

    override fun onCreate() {
        super.onCreate()
        Log.d("NB", "NotificationBlockerService.onCreate")
        // Add code to block notifications here
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Your code here to block notifications
        Log.d("NB", "NotificationBlockerService.onStartCommand")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("NB", "NotificationBlockerService.onBind")
        return super.onBind(intent)
    }

    override fun onDestroy() {
        Log.d("NB", "NotificationBlockerService.onDestroy")
        return super.onDestroy()
        // Add code to unblock notifications here
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Block notifications
        val text = sbn.notification?.extras?.getString("android.text");
        Log.d("NB", "NotificationBlockerService.onNotificationPosted - text: $text")
        super.onNotificationPosted(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Optionally handle notification removal
    }

    override fun onListenerConnected() {
        Log.d("NB", "NotificationBlockerService.onListenerConnected")
    }
}
