package com.example.notificationblocker.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notificationblocker.R
import com.example.notificationblocker.data.App
import com.example.notificationblocker.data.allApplicationsAppId
import com.example.notificationblocker.data.doNotDisturbAppId
import com.example.notificationblocker.ui.AppViewModelProvider
import com.example.notificationblocker.ui.navigation.NavigationDestination
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onGroupClick: (id: Int) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home)) },
            )
        },
        floatingActionButton = {
            StartButton()
        },
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            HomeBody(
                onGroupClick = onGroupClick,
                viewModel = viewModel,
            )
        }
    }
}


@Composable
fun HomeBody(
    onGroupClick: (id: Int) -> Unit,
    viewModel: HomeViewModel,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current;
    val groups by viewModel.groups.collectAsState()
    val appIds by viewModel.apps.collectAsState()



    LazyColumn() {
        item {
            ListItem(
                headlineContent = { Text(stringResource(R.string.do_not_disturb)) },
                trailingContent = {
                    Switch(
                        checked = appIds.contains(doNotDisturbAppId),
                        onCheckedChange = {
                            viewModel.toggleApp(doNotDisturbAppId, it)
                        },
                    )
                },
            )
        }
        item {
            Divider()
        }
        item {
            ListItem(
                headlineContent = { Text(stringResource(R.string.add_group)) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_group),
                    )

                },
                modifier = Modifier.clickable {

                    scope.launch {
                        viewModel.addGroup().also {
                            onGroupClick(it)
                        }
                    }
                },

                )
        }
        items(items = groups, key = { it.id }) { group ->
            ListItem(
                modifier = Modifier.clickable {
                    onGroupClick(group.id)
                },
                headlineContent = {
                    Text(group.name)
                },

                trailingContent = {
                    Switch(
                        checked = group.active,
                        onCheckedChange = {
                            viewModel.toggleGroup(group, it)
                        },
                    )
                },
            )
        }
        item {
            Divider()
        }
        item {
            ListItem(
                headlineContent = { Text(stringResource(R.string.all_applications)) },
                trailingContent = {
                    Switch(
                        checked = appIds.contains(allApplicationsAppId),
                        onCheckedChange = {
                            viewModel.toggleApp(allApplicationsAppId, it)
                        },
                    )
                },
            )
        }
        item {
            Divider()
        }

        items(App.allApps, key = { it.id }) { application ->
            ListItem(
                leadingContent = {
                    Image(
                        painter = rememberDrawablePainter(application.icon),
                        contentDescription = application.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(64.dp),
                    )
                },
                headlineContent = {
                    Text(application.name)
                },
                supportingContent = {
                    Text(application.id)
                },
                trailingContent = {
                    Switch(
                        checked = appIds.contains(application.id),
                        onCheckedChange = {
                            viewModel.toggleApp(application.id, it)
                        },
                    )
                },
            )

        }
    }
}