@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.notificationblocker

import android.app.LauncherActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.service.quicksettings.Tile
import androidx.compose.foundation.lazy.items
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotificationBlockerTheme {
                Scaffold(topBar = {
                    TopAppBar(title = {
                        Text("Notification blocker")
                    })
                }

                ) { paddingValues ->
                    NotificationBlocker(modifier = Modifier.padding(paddingValues))
                }

            }
        }
    }
}


@Composable
fun NotificationBlocker(modifier: Modifier = Modifier) {
    val packageManager = LocalContext.current.packageManager;
    val mainIntent = Intent(Intent.ACTION_MAIN, null)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

    val applications = packageManager.queryIntentActivities(mainIntent, 0)
//        .filter {
//            it.activityInfo.name.isNotEmpty()
//        }
        .sortedBy { it ->
            it.loadLabel(packageManager).toString().lowercase()
        };
    for (application in applications) {
        println("name")
        println(application.loadLabel(packageManager))
    }
    LazyColumn {
        items(applications) { application ->
            val resources =
                packageManager.getResourcesForApplication(application.activityInfo.applicationInfo);
            val name = if (application.activityInfo.labelRes != 0) {
                resources.getString(application.activityInfo.labelRes)
            } else {
                application.activityInfo.applicationInfo.loadLabel(packageManager).toString()
            }
            var packageName = application.activityInfo.packageName;
            var iconDrawable = application.activityInfo.loadIcon(packageManager)
            ListItem(leadingContent = {
                // TODO: Add icon
            }, headlineContent = {
                Text(application.loadLabel(packageManager).toString())
            }, supportingContent = {
                Text(packageName)

            })
        }
    }
}