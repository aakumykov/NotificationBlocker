package com.valentinvignal.notificationblocker.ui.home

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valentinvignal.notificationblocker.NotificationBlockerListenerService
import com.valentinvignal.notificationblocker.R
import com.valentinvignal.notificationblocker.data.doNotDisturbAppId
import com.valentinvignal.notificationblocker.ui.AppViewModelProvider

@Composable
fun StartButton(
    viewModel: StartButtonViewModel =  viewModel(factory = AppViewModelProvider.Factory)
) {

    var isServiceRunning by remember { mutableStateOf(NotificationBlockerListenerService.active) }
    val context = LocalContext.current
    val apps by viewModel.apps.collectAsState();


    return FloatingActionButton(onClick = {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (isServiceRunning) {
            if (apps.contains(doNotDisturbAppId)) {
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
            }
            NotificationBlockerListenerService.appIds = emptySet();
        } else {
            if (!NotificationManagerCompat.getEnabledListenerPackages(context).contains(
                    context.packageName
                )
            ) {
                context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }

            if (apps.contains(doNotDisturbAppId)) {
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
            }
            NotificationBlockerListenerService.appIds = apps;
        }
        isServiceRunning = !isServiceRunning
        NotificationBlockerListenerService.active = isServiceRunning
    }) {
        Icon(
            imageVector = if (isServiceRunning) Icons.Default.Close else Icons.Default.PlayArrow,
            contentDescription = stringResource(
                if (isServiceRunning) R.string.pause else R.string.play
            )
        )
    }
}