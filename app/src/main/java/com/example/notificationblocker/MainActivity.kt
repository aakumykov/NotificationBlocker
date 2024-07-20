@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.notificationblocker

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.quicksettings.Tile
import android.widget.ImageView
import androidx.compose.foundation.lazy.items
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notificationblocker.ui.theme.NotificationBlockerTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.text.NumberFormat
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.provider.Settings
import android.util.Log


class MainActivity : ComponentActivity() {


    private var serviceBound = false
    private var notificationListenerService: NotificationBlockerService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            serviceBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            serviceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificationBlockerTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Text(stringResource(R.string.notification_blocker))
                        },
                        actions = {
                            val context = LocalContext.current;
                            BlockAllSwitch(
                                onSwitchChange = { isChecked ->
                                    setBlockingEnabled(context, isChecked);
                                },
                            )
                        }
                    )
                }
                ) { paddingValues ->
                    NotificationBlocker(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }

    override fun onStart() {
        Log.d("MainActivity", "onStart")
        super.onStart()
        Intent(this, NotificationBlockerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        Log.d("MainActivity", "onStop")
        super.onStop()
        if (serviceBound) {
            unbindService(connection)
            serviceBound = false
        }
    }

    override fun onResume() {
        Log.d("MainActivity", "onResume")
        super.onResume()
        if (!isNotificationServiceEnabled(this)) {
            // Request the user to enable notification listener access
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

    }

    private fun setBlockingEnabled(context: Context, enable: Boolean) {
        handleNotificationBlocking(context, enable)
        val sharedPreferences = context.getSharedPreferences("NotificationBlockerPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("blocking_enabled", enable)
            apply()
        }
        NotificationBlockerService.isBlockingEnabled = enable
    }

}

@Composable
fun BlockAllSwitch(onSwitchChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("NotificationBlockerPrefs", Context.MODE_PRIVATE)
    var blockAll by remember { mutableStateOf(sharedPreferences.getBoolean("blocking_enabled", false)) }

    Switch(
        checked = blockAll,
        onCheckedChange = { isChecked ->
            println("notificationBlocker in onChanged");
            blockAll = isChecked
            // Call function to handle notification blocking
            onSwitchChange(isChecked);
        },
    )
}


fun handleNotificationBlocking(context: Context, enable: Boolean) {
    // https://stackoverflow.com/questions/42451538/how-to-start-the-notificationlistenerservice-on-android
    if (!isNotificationServiceEnabled(context)) {
        // Request the user to enable notification listener access
        context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
    } else {
        // If already enabled, bind the service and set the state
        Intent(context, NotificationBlockerService::class.java).also { intent ->
            context.bindService(intent, object : ServiceConnection {
                override fun onServiceConnected(className: ComponentName, service: IBinder) {
                    NotificationBlockerService.isBlockingEnabled =  enable
                }

                override fun onServiceDisconnected(arg0: ComponentName) {
                    // Handle service disconnected
                }
            }, Context.BIND_AUTO_CREATE)
        }
    }
}

private fun isNotificationServiceEnabled(context: Context): Boolean {
    val cn = ComponentName(context, NotificationBlockerService::class.java)
    val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    return flat?.contains(cn.flattenToString()) == true
}

data class ApplicationsState(
    val hasData: Boolean = false,
    val applications: List<ResolveInfo> = listOf(),
)

class ApplicationsViewModel(packageManager: PackageManager) : ViewModel() {
    private val _uiState = MutableStateFlow(ApplicationsState())
    val uiState = _uiState.asStateFlow()

    init {
        val applications = getApplications(packageManager);
        _uiState.value = ApplicationsState(hasData = true, applications = applications)
    }
}

private fun getApplications(packageManager: PackageManager): List<ResolveInfo> {
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

    return packageManager.queryIntentActivities(mainIntent, 0).sortedBy { it ->
        it.loadLabel(packageManager).toString().lowercase()
    };

}

@Composable
fun NotificationBlocker(modifier: Modifier = Modifier) {
    val packageManager = LocalContext.current.packageManager;

    val applicationsState by ApplicationsViewModel(packageManager).uiState.collectAsState()

    if (!applicationsState.hasData) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.loading),
                textAlign = TextAlign.Center,
            )
        }
    } else {
        LazyColumn {
            items(
                applicationsState.applications,
                key = { it.activityInfo.packageName },
            ) { application ->
                val resources =
                    packageManager.getResourcesForApplication(application.activityInfo.applicationInfo);
                val name = if (application.activityInfo.labelRes != 0) {
                    resources.getString(application.activityInfo.labelRes)
                } else {
                    application.activityInfo.applicationInfo.loadLabel(packageManager).toString()
                }
                val packageName = application.activityInfo.packageName;
                val iconDrawable: Drawable = application.activityInfo.loadIcon(packageManager)

                ListItem(
                    leadingContent = {
                        Image(
                            painter = rememberDrawablePainter(iconDrawable),
                            contentDescription = packageName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(64.dp),
                        )
                    },
                    headlineContent = {
                        Text(application.loadLabel(packageManager).toString())
                    },
                    supportingContent = {
                        Text(packageName)
                    },
                    trailingContent = {
                        Switch(
                            checked = false,
                            onCheckedChange = {}
                        )
                    }
                )
            }
        }
    }


}