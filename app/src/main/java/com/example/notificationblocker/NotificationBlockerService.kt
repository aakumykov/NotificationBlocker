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

class NotificationBlockerService : NotificationListenerService() {

    private val channelId = "NotificationBlockerServiceChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, getNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        Log.d("com.example.notificationblocker.NotificationBlockerService", "Service created")
        // Add code to block notifications here
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Your code here to block notifications
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            channelId,
            "Notification Blocker Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun getNotification(): Notification {
        return Notification.Builder(this, channelId)
            .setContentTitle("Notification Blocker")
            .setContentText("Blocking notifications")
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("com.example.notificationblocker.NotificationBlockerService", "Service destroyed")
        // Add code to unblock notifications here
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Block notifications
        cancelNotification(sbn.key)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Optionally handle notification removal
    }
}
