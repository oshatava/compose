package com.osh.sample.splash.impl.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.osh.sample.splash.impl.presentation.SplashScreenViewModel
import com.osh.sample.splash.impl.presentation.SplashScreenViewModel.Event
import com.osh.ui.compose.MessageFullScreen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SplashScreen(
    model: SplashScreenViewModel = hiltViewModel(),
    onNavigateNext: () -> Unit = {}
) {
    LaunchedEffect(model) {
        model.eventsFlow.collectLatest { value ->
            when (value) {
                Event.NavigateNext -> onNavigateNext()
            }
        }
    }
    Scaffold(
        content = {
            MessageFullScreen(
                message = "HI",
                modifier = Modifier.padding(it),
            )
        }
    )
}

