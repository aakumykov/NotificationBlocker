package com.example.notificationblocker.ui.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notificationblocker.R
import com.example.notificationblocker.data.App
import com.example.notificationblocker.ui.AppViewModelProvider
import com.example.notificationblocker.ui.navigation.NavigationDestination
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.ui.layout.ContentScale
import com.google.accompanist.drawablepainter.rememberDrawablePainter


object GroupDestination : NavigationDestination {
    override val route = "group"
    override val titleRes = R.string.group
    const val itemIdArg = "groupId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupScreen(
    navigateBack: () -> Unit,
    viewModel: GroupViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    val uiState = viewModel.uiState.collectAsState()
    val linkedApps = viewModel.appsState.collectAsState();
    val context = LocalContext.current;

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.value.name) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
            )
        },

        ) { innerPadding ->

        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(App.getAll(context), key = { it.id }) { application ->
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
                            checked = application.id in linkedApps.value,
                            onCheckedChange = {
                                if (it) {
                                    viewModel.addApp(application.id)
                                } else {
                                    viewModel.deleteApp(application.id)
                                }
                            }
                        )
                    }
                )

            }
        }
    }
}

