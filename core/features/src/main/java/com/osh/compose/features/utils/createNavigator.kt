package com.osh.compose.features.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.osh.compose.features.NavigationPath

fun createNavigator(navController: NavHostController): (path: NavigationPath) -> Unit {
    return { path ->
        navController.navigate(path.route)
    }
}

fun createNavigator(navController: NavHostController, path: NavigationPath): () -> Unit {
    return {
        navController.navigate(path.route)
    }
}


@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: NavigationPath,
    builder: NavGraphBuilder.() -> Unit
) = androidx.navigation.compose.NavHost(
    navController = navController,
    startDestination = startDestination.route,
    builder = builder
)

fun NavGraphBuilder.navigation(
    startDestination: NavigationPath,
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    builder: NavGraphBuilder.() -> Unit
) = navigation(startDestination.route, route, arguments, deepLinks, builder)

fun NavGraphBuilder.navigation(
    startDestination: NavigationPath,
    route: NavigationPath,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    builder: NavGraphBuilder.() -> Unit
) = navigation(startDestination.route, route.route, arguments, deepLinks, builder)

fun NavGraphBuilder.composable(
    route: NavigationPath,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) = composable(route.route, arguments, deepLinks, content)