package com.valentinvignal.notificationblocker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.valentinvignal.notificationblocker.ui.navigation.NotificationBlockerNavGraph

@Composable
fun NotificationBlockerApp(navController: NavHostController = rememberNavController()) {
    NotificationBlockerNavGraph(navController = navController)
}
