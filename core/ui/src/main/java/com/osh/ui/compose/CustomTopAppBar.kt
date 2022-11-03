package com.osh.ui.compose

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@ExperimentalMaterial3Api
@Composable
fun CustomTopAppBar(
    titleText: String,
    titleTextStyle: TextStyle,
    titleExpandedTextStyle: TextStyle,
    titleExpandedContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    bgColor: Color = MaterialTheme.colorScheme.surface,
    maxHeight: Dp,
    pinnedHeight: Dp,
    bottomPadding: Dp,
    startEndPadding: Dp,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    if (maxHeight <= pinnedHeight) {
        throw IllegalArgumentException(
            "A CustomTopAppBar max height should be greater than its pinned height"
        )
    }
    val pinnedHeightPx: Float
    val maxHeightPx: Float
    val bottomPaddingPx: Int
    LocalDensity.current.run {
        pinnedHeightPx = pinnedHeight.toPx()
        maxHeightPx = maxHeight.toPx()
        bottomPaddingPx = bottomPadding.roundToPx()
    }

    SideEffect {
        if (scrollBehavior?.state?.heightOffsetLimit != pinnedHeightPx - maxHeightPx) {
            scrollBehavior?.state?.heightOffsetLimit = pinnedHeightPx - maxHeightPx
        }
    }

    val transitionFraction = min(1f, max(0f, scrollBehavior?.state?.collapsedFraction ?: 0f))
    val titleContentAlpha = 1f - transitionFraction

    val appBarDragModifier = if (scrollBehavior != null && !scrollBehavior.isPinned) {
        Modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                scrollBehavior.state.heightOffset = scrollBehavior.state.heightOffset + delta
            },
            onDragStopped = { velocity ->
                settleAppBar(
                    scrollBehavior.state,
                    velocity,
                    scrollBehavior.flingAnimationSpec,
                    scrollBehavior.snapAnimationSpec
                )
            }
        )
    } else {
        Modifier
    }

    Surface(
        modifier = modifier.then(appBarDragModifier),
        color = bgColor,
    ) {
        Column {
            Layout(
                content = {
                    Box(Modifier.layoutId("titleExpanded")) {
                        Text(
                            titleText,
                            style = calculateTextStyle(
                                titleExpandedTextStyle,
                                titleTextStyle,
                                transitionFraction
                            )
                        )
                    }
                    Box(
                        Modifier
                            .alpha(titleContentAlpha)
                            .layoutId("titleContent")
                    ) {
                        titleExpandedContent()
                    }
                },
                modifier = Modifier.clipToBounds(),
            ) { measurables, constraints ->
                val maxTitleWidth = constraints.maxWidth - (startEndPadding * 2).roundToPx()

                val titleExpandedPlaceable = measurables.first { it.layoutId == "titleExpanded" }
                    .measure(constraints.copy(minWidth = 0, maxWidth = maxTitleWidth))

                val titleContentPlaceable = measurables.first { it.layoutId == "titleContent" }
                    .measure(constraints.copy(minWidth = 0, maxWidth = maxTitleWidth))

                val layoutHeight = max(
                    (maxHeightPx + (scrollBehavior?.state?.heightOffset ?: 0f)).roundToInt(),
                    pinnedHeightPx.roundToInt()
                )

                layout(constraints.maxWidth, layoutHeight) {
                    titleExpandedPlaceable.placeRelative(
                        x = calculatePosition(
                            from = startEndPadding.toPx(),
                            to = (constraints.maxWidth - titleExpandedPlaceable.width) / 2f,
                            perscent = (1 - transitionFraction)
                        ).roundToInt(),
                        y = max(
                            layoutHeight - titleExpandedPlaceable.height - titleContentPlaceable.height,
                            pinnedHeightPx.roundToInt() - titleExpandedPlaceable.height
                        ) - bottomPaddingPx
                    )
                    titleContentPlaceable.placeRelative(
                        x = startEndPadding.roundToPx(),
                        y = layoutHeight - titleContentPlaceable.height - bottomPaddingPx
                    )
                }
            }
        }
    }
}

private fun calculatePosition(from: Float, to: Float, perscent: Float): Float {
    return from * perscent + to * (1 - perscent)
}

private fun calculateTextStyle(from: TextStyle, to: TextStyle, perscent: Float): TextStyle {
    return when {
        perscent <= 0f -> from
        perscent >= 1f -> to
        perscent < 0.5 -> from.copy(
            fontSize = calculateTextUnit(
                from.fontSize,
                to.fontSize,
                (1 - perscent)
            )
        )
        else -> to.copy(fontSize = calculateTextUnit(from.fontSize, to.fontSize, (1 - perscent)))
    }
}

private fun calculateTextUnit(from: TextUnit, to: TextUnit, perscent: Float): TextUnit {
    return from * perscent + to * (1 - perscent)
}

@OptIn(ExperimentalUnitApi::class)
private operator fun TextUnit.plus(textUnit: TextUnit): TextUnit {
    return TextUnit(value = this.value + textUnit.value, type = type)
}

@OptIn(ExperimentalMaterial3Api::class)
private suspend fun settleAppBar(
    state: TopAppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?
): Velocity {
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    // In case there is an initial velocity that was left after a previous user fling, animate to
    // continue the motion to expand or collapse the app bar.
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    // Snap if animation specs were provided.
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 &&
            state.heightOffset > state.heightOffsetLimit
        ) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec
            ) { state.heightOffset = value }
        }
    }
    return Velocity(0f, remainingVelocity)
}