package com.osh.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.osh.compose.features.utils.NavHost
import com.osh.compose.features.utils.composable
import com.osh.compose.navigation.AppRoutes
import com.osh.compose.navigation.impl.AppNavigationImpl
import com.osh.compose.ui.theme.ComposeTheme
import com.osh.sample.main.MainFeatureApi
import com.osh.sample.splash.SplashFeatureApi
import com.osh.ui.compose.MessageFullScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainFeatureApi: MainFeatureApi

    @Inject
    lateinit var splashFeatureApi: SplashFeatureApi

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme(
                //darkTheme = true
            ) {
                val navController = rememberNavController()
                val appNavigation = remember { AppNavigationImpl(navController) }
                NavHost(navController = navController, startDestination = AppRoutes.SPLASH) {
                    splashFeatureApi.createFeatureNavigation(
                        AppRoutes.SPLASH,
                        this,
                        navController,
                        appNavigation
                    )
                    mainFeatureApi.createFeatureNavigation(
                        AppRoutes.MAIN,
                        this,
                        navController,
                        appNavigation
                    )
                    composable(AppRoutes.UNDER_CONSTRUCTION) {
                        Scaffold(content = {
                            MessageFullScreen(
                                message = "Under construction",
                                modifier = Modifier.padding(it)
                            )
                        })
                    }
                }
            }
        }
    }
}



