package com.osh.sample.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.osh.compose.features.FeatureNavigationGraphCreator
import com.osh.compose.features.NavigationPath
import com.osh.compose.features.NavigatorCallback

data class MainFeatureConfig(
    val baseAPIUrl: String
)

interface MainFeatureNavigatorCallback : NavigatorCallback {
    fun showDetails(detailId: String)
}

interface MainFeatureApi : FeatureNavigationGraphCreator<MainFeatureNavigatorCallback> {
    override fun createFeatureNavigation(
        route: NavigationPath,
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        navigatorCallback: MainFeatureNavigatorCallback
    )
}
