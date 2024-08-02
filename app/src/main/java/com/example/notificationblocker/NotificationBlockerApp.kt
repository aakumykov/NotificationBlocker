package com.example.notificationblocker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.notificationblocker.ui.navigation.NotificationBlockerNavGraph

@Composable
fun NotificationBlockerApp(navController: NavHostController = rememberNavController()) {
    NotificationBlockerNavGraph(navController = navController)
}
