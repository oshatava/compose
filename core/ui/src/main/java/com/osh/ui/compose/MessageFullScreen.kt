package com.osh.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun MessageFullScreen(
    message: String,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        val (content) = createRefs()
        Text(
            modifier = Modifier
                .constrainAs(content) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            text = message
        )
    }
}