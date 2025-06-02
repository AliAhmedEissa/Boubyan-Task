package com.boubyan.articles.presentation.ui.components.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FadeTransition(): ContentTransform {
    return fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
}