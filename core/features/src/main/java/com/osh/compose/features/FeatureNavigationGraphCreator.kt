package com.osh.compose.features

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController

interface NavigatorCallback {
    fun back()
    fun close()
}

interface FeatureNavigationGraphCreator<T : NavigatorCallback> {
    fun createFeatureNavigation(
        route: NavigationPath,
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        navigatorCallback: T
    )
}