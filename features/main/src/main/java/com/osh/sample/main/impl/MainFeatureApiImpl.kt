package com.osh.sample.main.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.osh.compose.features.NavigationPath
import com.osh.sample.main.MainFeatureApi
import com.osh.sample.main.MainFeatureNavigatorCallback
import com.osh.sample.main.impl.ui.ListScreen
import javax.inject.Inject

internal class MainFeatureApiImpl @Inject constructor() : MainFeatureApi {

    override fun createFeatureNavigation(
        route: NavigationPath,
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        navigatorCallback: MainFeatureNavigatorCallback
    ) {
        navGraphBuilder.navigation(startDestination = "items_list", route = route.route) {
            composable("items_list") { ListScreen(onNavigateDetails = navigatorCallback::showDetails) }
        }
    }
}