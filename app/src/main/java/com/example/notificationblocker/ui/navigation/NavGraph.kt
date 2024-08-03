package com.example.notificationblocker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notificationblocker.ui.group.GroupDestination
import com.example.notificationblocker.ui.group.GroupScreen
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
        modifier = modifier,
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                onGroupClick = { navController.navigate("${GroupDestination.route}/$it") }
            )
        }
        composable(
            route = GroupDestination.routeWithArgs,
            arguments = listOf(navArgument(GroupDestination.itemIdArg) {
                type = NavType.IntType
            }),
        ) {
            GroupScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}
