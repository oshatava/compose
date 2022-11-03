package com.osh.compose.navigation.impl

import androidx.navigation.NavController
import com.osh.compose.navigation.AppNavigation
import com.osh.compose.navigation.AppRoutes
import com.osh.compose.navigation.AppRoutes.UNDER_CONSTRUCTION

internal class AppNavigationImpl(
    private val navController: NavController
) : AppNavigation {

    override fun showMain() {
        navController.navigate(AppRoutes.MAIN.route) {
            popUpTo(AppRoutes.SPLASH.route) {
                inclusive = true
            }
        }
    }

    override fun back() {
        navController.popBackStack()
    }

    override fun close() {
        navController.popBackStack()
    }

    override fun showDetails(detailId: String) {
        navController.navigate(UNDER_CONSTRUCTION.route)
    }
}