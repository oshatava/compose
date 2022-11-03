package com.osh.compose.navigation

import com.osh.compose.features.NavigationPath
import com.osh.sample.main.MainFeatureNavigatorCallback
import com.osh.sample.splash.SplashFeatureNavigatorCallback

interface AppNavigation : SplashFeatureNavigatorCallback, MainFeatureNavigatorCallback

enum class AppRoutes(override val route: String) : NavigationPath {
    SPLASH("splash"),
    MAIN("main"),
    UNDER_CONSTRUCTION("under_construction"),
}