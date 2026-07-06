package com.beardytop.mitzmode.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.*

/**
 * Lush, painterly night-sky background with multiple gradient layers,
 * a warm gold glow at the top, gently twinkling Stars of David, and slow drifting
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

    // Pre-generated field — fixed positions / rotations so glyphs don't drift.
    val stars = remember { generateStars(seed = 42, count = 0) }

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

        // 6. Stars of David — soft halos + stroked Magen David, gently twinkling
        stars.forEach { star ->
            val phase = (twinkle + star.phase) % 1f
            val pulse = (sin(phase * 2 * PI).toFloat() + 1f) / 2f  // 0..1
            val alpha = (star.baseAlpha * (0.55f + 0.45f * pulse)).coerceIn(0.06f, 0.92f)
            val cx = star.x * w
            val cy = star.y * h
            val color = when (star.tint) {
                StarTint.GOLD  -> Color(0xFFFFD56B)
                StarTint.AMBER -> Color(0xFFFFB300)
                StarTint.WHITE -> Color(0xFFFFFFFF)
                StarTint.BLUE  -> Color(0xFFB7C7FF)
            }
            val triR = (star.radius * (3.2f + 0.35f * pulse) + 2.0f).coerceIn(2.8f, 12.5f)
            val strokeW = (triR * 0.20f).coerceIn(0.5f, 2.1f) * if (star.isBright) 1.2f else 1f

            drawCircle(
                color = color.copy(alpha = alpha * 0.14f),
                radius = triR * 2.6f,
                center = Offset(cx, cy)
            )
            drawCircle(
                color = color.copy(alpha = alpha * 0.24f),
                radius = triR * 1.35f,
                center = Offset(cx, cy)
            )

            rotate(star.rotationDeg, pivot = Offset(cx, cy)) {
                drawStarOfDavidGlyph(
                    center = Offset(cx, cy),
                    radius = triR,
                    color = color.copy(alpha = alpha),
                    strokeWidth = strokeW
                )
                if (star.isBright) {
                    drawStarOfDavidGlyph(
                        center = Offset(cx, cy),
                        radius = triR * 1.12f,
                        color = color.copy(alpha = alpha * 0.28f),
                        strokeWidth = strokeW * 0.45f
                    )
                }
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
    val isBright: Boolean,
    val rotationDeg: Float
)

/**
 * Generates a deterministic field — mostly small white / gold Magen Davids,
 * a few brighter ones with a soft outer echo.
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
            isBright = isBright,
            rotationDeg = rng.nextFloat() * 360f
        )
    }
}

/** Two equilateral triangles — classic Magen David outline (same geometry as dialog artwork). */
private fun DrawScope.drawStarOfDavidGlyph(
    center: Offset,
    radius: Float,
    color: Color,
    strokeWidth: Float
) {
    val cos30 = cos(30.0 * PI / 180.0).toFloat()
    val sin30 = sin(30.0 * PI / 180.0).toFloat()
    val style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round)

    drawPath(
        Path().apply {
            moveTo(center.x, center.y - radius)
            lineTo(center.x + radius * cos30, center.y + radius * sin30)
            lineTo(center.x - radius * cos30, center.y + radius * sin30)
            close()
        },
        color = color,
        style = style
    )
    drawPath(
        Path().apply {
            moveTo(center.x, center.y + radius)
            lineTo(center.x + radius * cos30, center.y - radius * sin30)
            lineTo(center.x - radius * cos30, center.y - radius * sin30)
            close()
        },
        color = color,
        style = style
    )
}
