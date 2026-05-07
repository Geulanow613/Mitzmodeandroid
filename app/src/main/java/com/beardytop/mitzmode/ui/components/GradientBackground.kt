package com.beardytop.mitzmode.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.*

/**
 * Lush, painterly night-sky background with multiple gradient layers,
 * a warm gold glow at the top, gently twinkling stars, and slow drifting
 * light beams. Built to feel like a softly-lit Torah scroll night sky.
 */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(180_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val twinkle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )

    val auraPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6_000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraPulse"
    )

    // Pre-generated star field — fixed positions so they don't dance around.
    val stars = remember { generateStars(seed = 42, count = 80) }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Base vertical gradient — deep midnight blue → indigo → plum
        drawRect(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0f   to Color(0xFF050B1F),
                    0.45f to Color(0xFF0E1B47),
                    0.85f to Color(0xFF1A0B3D),
                    1f   to Color(0xFF0A0420)
                )
            ),
            size = Size(w, h)
        )

        // 2. Subtle radial vignette to add depth
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.45f)
                ),
                center = Offset(w * 0.5f, h * 0.45f),
                radius = max(w, h) * 0.85f
            ),
            size = Size(w, h)
        )

        // 3. Warm golden aura at the top, gently pulsing — reads as divine light
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFE082).copy(alpha = 0.18f),
                    Color(0xFFFFD56B).copy(alpha = 0.10f),
                    Color(0xFFB8860B).copy(alpha = 0.04f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h * 0.05f),
                radius = w * 0.85f * auraPulse
            ),
            radius = w * 0.85f * auraPulse,
            center = Offset(w * 0.5f, h * 0.05f)
        )

        // 4. A second soft purple aura at the bottom for richness
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF6E2DC9).copy(alpha = 0.18f),
                    Color(0xFF3A1372).copy(alpha = 0.08f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h * 1.0f),
                radius = w * 0.95f
            ),
            radius = w * 0.95f,
            center = Offset(w * 0.5f, h * 1.0f)
        )

        // 5. Slow rotating light shimmer overlay
        rotate(rotation, pivot = Offset(w * 0.5f, h * 0.5f)) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.025f),
                        Color.Transparent,
                        Color(0xFFFFE082).copy(alpha = 0.04f),
                        Color.Transparent
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(w, h)
                ),
                size = Size(w, h)
            )
        }

        // 6. Stars with multi-layer glow & gentle twinkle
        stars.forEach { star ->
            val phase = (twinkle + star.phase) % 1f
            val pulse = (sin(phase * 2 * PI).toFloat() + 1f) / 2f  // 0..1
            val alpha = (star.baseAlpha * (0.55f + 0.45f * pulse)).coerceIn(0.05f, 0.95f)
            val r = star.radius * (0.85f + 0.3f * pulse)
            val cx = star.x * w
            val cy = star.y * h
            val color = when (star.tint) {
                StarTint.GOLD  -> Color(0xFFFFD56B)
                StarTint.AMBER -> Color(0xFFFFB300)
                StarTint.WHITE -> Color(0xFFFFFFFF)
                StarTint.BLUE  -> Color(0xFFB7C7FF)
            }

            // Outer halo
            drawCircle(
                color = color.copy(alpha = alpha * 0.18f),
                radius = r * 4.5f,
                center = Offset(cx, cy)
            )
            // Mid glow
            drawCircle(
                color = color.copy(alpha = alpha * 0.45f),
                radius = r * 2.2f,
                center = Offset(cx, cy)
            )
            // Core
            drawCircle(
                color = color.copy(alpha = alpha),
                radius = r,
                center = Offset(cx, cy)
            )

            // For the brightest stars, draw a subtle 4-point glint
            if (star.isBright) {
                val glintLen = r * 6f
                val glintColor = color.copy(alpha = alpha * 0.6f)
                drawLine(
                    color = glintColor,
                    start = Offset(cx - glintLen, cy),
                    end = Offset(cx + glintLen, cy),
                    strokeWidth = 0.6f
                )
                drawLine(
                    color = glintColor,
                    start = Offset(cx, cy - glintLen),
                    end = Offset(cx, cy + glintLen),
                    strokeWidth = 0.6f
                )
            }
        }

        // 7. Faint long light rays slowly sweeping across the canvas
        rotate(rotation * 0.25f, pivot = Offset(w * 0.5f, h * 0.5f)) {
            for (i in 0..2) {
                val rayCenterX = w * (0.3f + i * 0.2f)
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFFFFE082).copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    ),
                    topLeft = Offset(rayCenterX - w * 0.04f, -h * 0.2f),
                    size = Size(w * 0.08f, h * 1.4f)
                )
            }
        }

        // 8. Bottom soft fade-up so content text reads cleanly
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.32f)
                ),
                startY = h * 0.55f,
                endY = h
            ),
            size = Size(w, h)
        )
    }
}

private enum class StarTint { GOLD, AMBER, WHITE, BLUE }

private data class Star(
    val x: Float,
    val y: Float,
    val radius: Float,
    val baseAlpha: Float,
    val phase: Float,
    val tint: StarTint,
    val isBright: Boolean
)

/**
 * Generates a deterministic star field that's pleasing — most stars small/white,
 * a few gold accents, and a handful of "bright" stars with glints.
 */
private fun generateStars(seed: Int, count: Int): List<Star> {
    val rng = java.util.Random(seed.toLong())
    return List(count) {
        val rand = rng.nextFloat()
        val tint = when {
            rand < 0.55f -> StarTint.WHITE
            rand < 0.78f -> StarTint.GOLD
            rand < 0.92f -> StarTint.AMBER
            else         -> StarTint.BLUE
        }
        val isBright = rng.nextFloat() < 0.12f
        Star(
            x = rng.nextFloat(),
            y = rng.nextFloat(),
            radius = if (isBright) 1.6f + rng.nextFloat() * 1.2f else 0.6f + rng.nextFloat() * 1.0f,
            baseAlpha = if (isBright) 0.85f else 0.35f + rng.nextFloat() * 0.3f,
            phase = rng.nextFloat(),
            tint = tint,
            isBright = isBright
        )
    }
}
