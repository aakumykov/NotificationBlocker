package com.example.notificationblocker

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.os.Binder
import android.util.Log


// https://github.com/Chagall/notification-listener-service-example/blob/master/app/src/main/java/com/github/chagall/notificationlistenerexample/NotificationListenerExampleService.java


// https://www.geeksforgeeks.org/services-in-android-using-jetpack-compose/

// https://github.com/pintukumarpatil/NotificationListenerService/blob/master/NotificationListener/src/main/java/com/pk/example/NLService.java
public class NotificationBlockerService : NotificationListenerService() {

//    private val binder = LocalBinder()

    companion object {
        var isBlockingEnabled: Boolean = false
    }

//    override fun onBind(intent: Intent): IBinder {
//        return binder
//    }
//
//    inner class LocalBinder : Binder() {
//        fun getService(): NotificationBlockerService = this@NotificationBlockerService
//    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val text = sbn?.notification?.extras?.getString("android.text");
        Log.d("NotificationBlockerService", "onNotificationPosted - text: $text")
//        if (sbn != null) {
//            val blockAll = true
//            if (blockAll) {
//                cancelAllNotifications()
//            } else {
//                val packageName = sbn.packageName;
//                cancelNotification(sbn.key)
//            }
//
//        }
        super.onNotificationPosted(sbn)
    }

    override fun onListenerConnected() {
        Log.d("NotificationService", "Listener connected")
        // Retrieve the initial blocking state
        isBlockingEnabled = getBlockingStateFromPreferences()
    }

    override fun onListenerDisconnected() {
        Log.d("NotificationService", "Listener disconnected")
    }

    private fun getBlockingStateFromPreferences(): Boolean {
        val sharedPreferences = getSharedPreferences("NotificationBlockerPrefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("blocking_enabled", false)
    }
}