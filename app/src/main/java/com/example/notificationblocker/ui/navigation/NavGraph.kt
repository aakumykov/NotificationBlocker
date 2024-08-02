package com.example.notificationblocker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.notificationblocker.ui.home.HomeDestination
import com.example.notificationblocker.ui.home.HomeScreen

@Composable
fun NotificationBlockerNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier =modifier,
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(

            )
        }
    }
}
