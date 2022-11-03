package com.osh.sample.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.osh.compose.features.FeatureNavigationGraphCreator
import com.osh.compose.features.NavigationPath
import com.osh.compose.features.NavigatorCallback

interface SplashFeatureApi : SplashFeatureNavigationGraphCreator

interface SplashFeatureNavigatorCallback : NavigatorCallback {
    fun showMain()
}

interface SplashFeatureNavigationGraphCreator :
    FeatureNavigationGraphCreator<SplashFeatureNavigatorCallback> {
    override fun createFeatureNavigation(
        route: NavigationPath,
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        navigatorCallback: SplashFeatureNavigatorCallback
    )
}