package com.example.notificationblocker.ui.home

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import com.example.notificationblocker.NotificationBlockerListenerService

@Composable
fun StartButton() {

    var isServiceRunning by remember { mutableStateOf(NotificationBlockerListenerService.active) }
    val context = LocalContext.current
    return FloatingActionButton(onClick = {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (isServiceRunning) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        } else {
            if (!NotificationManagerCompat.getEnabledListenerPackages(context).contains(
                    context.packageName
                )
            ) {
                context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }

            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        }
        isServiceRunning = !isServiceRunning
        NotificationBlockerListenerService.active = isServiceRunning
    }) {
        Icon(
            imageVector = if (isServiceRunning) Icons.Default.Close else Icons.Default.PlayArrow,
            contentDescription = if (isServiceRunning) "Pause" else "Play"
        )
    }
}