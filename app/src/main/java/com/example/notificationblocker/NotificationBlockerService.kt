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
        Log.d("NB", "NotificationBlockerService.onCreate")

        // Add code to block notifications here
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Your code here to block notifications
        Log.d("NB", "NotificationBlockerService.onStartCommand")
        return START_STICKY
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
            .setContentTitle("Running...")
            .build()
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
        cancelNotification(sbn.key)
        super.onNotificationPosted(sbn)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Optionally handle notification removal
    }

    override fun onListenerConnected() {
        Log.d("NB", "NotificationBlockerService.onListenerConnected")
    }
}
