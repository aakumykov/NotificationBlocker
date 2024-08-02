package com.example.notificationblocker.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notificationblocker.R
import com.example.notificationblocker.data.Group
import com.example.notificationblocker.ui.AppViewModelProvider
import com.example.notificationblocker.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: GroupsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(floatingActionButton = {
        StartButton()
    }) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            HomeBody(
                groups = uiState,
            )
        }
    }
}

@Composable
fun HomeBody(
    groups: List<Group>,
) {
    LazyColumn() {
        items(items = groups, key = { it.id }) {
            ListItem(headlineContent = {
                Text(it.name)
            }, supportingContent = {
                Text(it.id.toString())
            })
        }
    }
}