package com.beardytop.beatzaddik.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

/** Shared candle flame drawing (splash + completion reward). */
internal fun DrawScope.drawCandleFlame(
    cx: Float,
    tipY: Float,
    baseY: Float,
    maxWidth: Float,
    flicker: Float,
    alpha: Float
) {
    val h = baseY - tipY

    val innerPath = Path().apply {
        moveTo(cx, tipY + h * 0.05f)
        cubicTo(
            cx + maxWidth * 0.15f * flicker, tipY + h * 0.3f,
            cx + maxWidth * 0.22f * flicker, tipY + h * 0.65f,
            cx, baseY
        )
        cubicTo(
            cx - maxWidth * 0.22f * flicker, tipY + h * 0.65f,
            cx - maxWidth * 0.15f * flicker, tipY + h * 0.3f,
            cx, tipY + h * 0.05f
        )
        close()
    }
    drawPath(
        innerPath,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFFDE7).copy(alpha = 0.95f * alpha),
                Color(0xFFFFE082).copy(alpha = 0.90f * alpha),
                Color(0xFFFFB300).copy(alpha = 0.80f * alpha),
                Color(0xFFFF6F00).copy(alpha = 0.60f * alpha)
            ),
            startY = tipY,
            endY = baseY
        )
    )

    val outerPath = Path().apply {
        moveTo(cx, tipY)
        cubicTo(
            cx + maxWidth * 0.32f * flicker, tipY + h * 0.28f,
            cx + maxWidth * 0.42f * flicker, tipY + h * 0.62f,
            cx, baseY
        )
        cubicTo(
            cx - maxWidth * 0.42f * flicker, tipY + h * 0.62f,
            cx - maxWidth * 0.32f * flicker, tipY + h * 0.28f,
            cx, tipY
        )
        close()
    }
    drawPath(
        outerPath,
        brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFD700).copy(alpha = 0.55f * alpha),
                Color(0xFFFF8C00).copy(alpha = 0.70f * alpha),
                Color(0xFFFF4500).copy(alpha = 0.50f * alpha),
                Color.Transparent
            ),
            startY = tipY,
            endY = baseY
        )
    )
}

internal fun DrawScope.drawCandleAmbientGlow(
    center: Offset,
    radius: Float,
    glow: Float,
    scale: Float,
    intensified: Boolean = false
) {
    val innerAlpha = if (intensified) 0.52f else 0.28f
    val midAlpha = if (intensified) 0.28f else 0.12f
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFD56B).copy(alpha = innerAlpha * glow * scale),
                Color(0xFFFF8C00).copy(alpha = midAlpha * glow * scale),
                Color.Transparent
            ),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )
    if (intensified) {
        val outerRadius = radius * 1.45f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFE082).copy(alpha = 0.22f * glow * scale),
                    Color(0xFFFFB300).copy(alpha = 0.10f * glow * scale),
                    Color.Transparent
                ),
                center = center,
                radius = outerRadius
            ),
            radius = outerRadius,
            center = center
        )
    }
}
