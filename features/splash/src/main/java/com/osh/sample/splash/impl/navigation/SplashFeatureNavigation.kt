package com.osh.sample.splash.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.osh.compose.features.NavigationPath
import com.osh.compose.features.utils.composable
import com.osh.compose.features.utils.createNavigator
import com.osh.compose.features.utils.navigation
import com.osh.sample.splash.SplashFeatureNavigationGraphCreator
import com.osh.sample.splash.SplashFeatureNavigatorCallback
import com.osh.sample.splash.impl.ui.SplashScreen
import com.osh.sample.splash.impl.ui.WelcomeScreen
import javax.inject.Inject

internal class SplashFeatureNavigationGraphCreatorImpl @Inject constructor(

) : SplashFeatureNavigationGraphCreator {

    override fun createFeatureNavigation(
        route: NavigationPath,
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        navigatorCallback: SplashFeatureNavigatorCallback
    ) {
        val navigate = createNavigator(navController)
        val hiPath = SplashFeatureInternalNavigation.Hi(route)
        val welcomePath = SplashFeatureInternalNavigation.Welcome(route)
        val navigateToMain = { navigatorCallback.showMain() }
        val navigateToWelcomePath = { navigate(welcomePath) }

        navGraphBuilder.navigation(startDestination = hiPath, route = route) {
            composable(hiPath) {
                SplashScreen(onNavigateNext = navigateToWelcomePath)
            }
            composable(welcomePath) {
                WelcomeScreen(onNavigateNext = navigateToMain)
            }
        }
    }
}

sealed class SplashFeatureInternalNavigation(override val route: String) : NavigationPath {
    class Hi(parentRoute: NavigationPath) :
        SplashFeatureInternalNavigation("${parentRoute.route}_hi")

    class Welcome(parentRoute: NavigationPath) :
        SplashFeatureInternalNavigation("${parentRoute.route}_welcome")
}
