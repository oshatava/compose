package com.osh.sample.splash.impl.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.osh.sample.splash.impl.presentation.WelcomeScreenViewModel
import com.osh.sample.splash.impl.presentation.WelcomeScreenViewModel.Event
import com.osh.ui.compose.MessageFullScreen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun WelcomeScreen(
    model: WelcomeScreenViewModel = hiltViewModel(),
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
                message = "WELCOME",
                modifier = Modifier.padding(it),
            )
        }
    )
}