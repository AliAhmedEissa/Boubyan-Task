package com.boubyan.articles.presentation.ui.components.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    size: Float = 60f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Canvas(
        modifier = modifier
            .size(size.dp)
            .padding(8.dp)
    ) {
        drawLoadingSpinner(
            rotation = rotation,
            scale = scale,
            color = color
        )
    }
}

@Composable
fun PulseLoadingAnimation(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Canvas(
        modifier = modifier.size(40.dp)
    ) {
        drawCircle(
            color = color.copy(alpha = alpha),
            radius = size.minDimension / 2 * scale
        )
    }
}

@Composable
fun DotsLoadingAnimation(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val dots = listOf(
        remember { Animatable(0f) },
        remember { Animatable(0f) },
        remember { Animatable(0f) }
    )
    
    LaunchedEffect(Unit) {
        dots.forEachIndexed { index, animatable ->
            kotlinx.coroutines.delay(index * 200L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(600),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        dots.forEach { animatable ->
            Canvas(modifier = Modifier.size(12.dp)) {
                drawCircle(
                    color = color.copy(alpha = animatable.value),
                    radius = size.minDimension / 2 * animatable.value
                )
            }
        }
    }
}

private fun DrawScope.drawLoadingSpinner(
    rotation: Float,
    scale: Float,
    color: Color
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 4 * scale
    
    // Draw rotating dots
    for (i in 0 until 8) {
        val angle = (rotation + i * 45) * (Math.PI / 180)
        val x = center.x + cos(angle).toFloat() * radius
        val y = center.y + sin(angle).toFloat() * radius
        
        val alpha = (1f - (i * 0.1f)).coerceAtLeast(0.2f)
        
        drawCircle(
            color = color.copy(alpha = alpha),
            radius = 4.dp.toPx() * scale,
            center = Offset(x, y)
        )
    }
}