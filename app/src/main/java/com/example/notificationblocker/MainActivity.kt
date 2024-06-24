@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.notificationblocker

import android.app.LauncherActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.lifecycle.ViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class MainActivity : ComponentActivity() {
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
                            BlockAllSwitch()
                        }
                    )
                }
                ) { paddingValues ->
                    NotificationBlocker(modifier = Modifier.padding(paddingValues))
                }

            }
        }
    }
}

@Composable
fun BlockAllSwitch(modifier: Modifier = Modifier) {
    var blockAll = remember {
        mutableStateOf(false)
    }
    Switch(
        checked = blockAll.value,
        onCheckedChange = {
            // https://stackoverflow.com/questions/42451538/how-to-start-the-notificationlistenerservice-on-android
            blockAll.value = !blockAll.value
        },
    )
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
    println("in get applications")
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