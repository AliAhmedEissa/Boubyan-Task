package com.boubyan.articles.presentation.ui.components.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable

/**
 * Creates a slide animation that moves content in from the right side of the screen.
 * The content slides in from the right edge and slides out to the left edge.
 * Animation duration is set to 300ms.
 *
 * @return A ContentTransform that defines the slide animation
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlideInFromRight(): ContentTransform {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(300)
    ) with slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(300)
    )
}

/**
 * Creates a slide animation that moves content in from the left side of the screen.
 * The content slides in from the left edge and slides out to the right edge.
 * Animation duration is set to 300ms.
 *
 * @return A ContentTransform that defines the slide animation
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlideInFromLeft(): ContentTransform {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth },
        animationSpec = tween(300)
    ) with slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(300)
    )
}

/**
 * Creates a slide animation that moves content in from the bottom of the screen.
 * The content slides in from the bottom edge and slides out to the top edge.
 * Animation duration is set to 300ms.
 *
 * @return A ContentTransform that defines the slide animation
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlideInFromBottom(): ContentTransform {
    return slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight },
        animationSpec = tween(300)
    ) with slideOutVertically(
        targetOffsetY = { fullHeight -> -fullHeight },
        animationSpec = tween(300)
    )
}

/**
 * Creates a fade animation that transitions content in and out.
 * The content fades in and out with a duration of 300ms.
 *
 * @return A ContentTransform that defines the fade animation
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FadeTransition(): ContentTransform {
    return fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
}