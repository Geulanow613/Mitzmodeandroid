package com.beardytop.beatzaddik.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.max

/**
 * Warm parchment/gold gradient background — clean, no decorative symbols.
 */
@Composable
fun HolyLightBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(8_000, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Deep navy-to-midnight base
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0f to Color(0xFF0A1628),
                    0.40f to Color(0xFF122040),
                    0.75f to Color(0xFF1A1035),
                    1f to Color(0xFF0D0820)
                )
            ),
            size = Size(w, h)
        )

        // Subtle warm gold ambient glow at top — like candlelight
        val glowAlpha = 0.10f + pulse * 0.06f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFE082).copy(alpha = glowAlpha),
                    Color(0xFFFFD56B).copy(alpha = glowAlpha * 0.5f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, 0f),
                radius = w * 0.9f
            ),
            radius = w * 0.9f,
            center = Offset(w * 0.5f, 0f)
        )

        // Warm amber glow at bottom
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF8B4513).copy(alpha = 0.08f + pulse * 0.04f),
                    Color(0xFF5C2D0A).copy(alpha = 0.04f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h),
                radius = w * 0.75f
            ),
            radius = w * 0.75f,
            center = Offset(w * 0.5f, h)
        )

        // Subtle vignette
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.35f)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = max(w, h) * 0.75f
            ),
            size = Size(w, h)
        )
    }
}
