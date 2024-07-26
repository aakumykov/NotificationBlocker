package com.example.notificationblocker

import android.app.ActivityManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.notificationblocker.ui.theme.NotificationBlockerTheme
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat


class MainActivity : ComponentActivity() {

    private lateinit var serviceIntent: Intent

    private fun isServiceRunningInForeground(): Boolean {
        val manager = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        @Suppress("DEPRECATION")
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (NotificationBlockerService::class.java.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        serviceIntent = Intent(this, NotificationBlockerService::class.java)
        Log.d("NB", "com.example.notificationblocker.MainActivity.onCreate")

        setContent {
            NotificationBlockerTheme {
                var isServiceRunning by remember { mutableStateOf(isServiceRunningInForeground()) }

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                if (isServiceRunning) {
                                    stopService(serviceIntent)
                                } else {
                                    if (!NotificationManagerCompat.getEnabledListenerPackages(this)
                                            .contains(
                                                packageName
                                            )
                                    ) {
                                        startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                                    }

                                    startForegroundService(
                                        serviceIntent
                                    )
                                }
                                isServiceRunning = !isServiceRunning
                            }
                        ) {
                            Icon(
                                imageVector = if (isServiceRunning) Icons.Default.Close else Icons.Default.PlayArrow,
                                contentDescription = if (isServiceRunning) "Pause" else "Play"
                            )
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Your UI content
                    }
                }
            }
        }
    }
}
