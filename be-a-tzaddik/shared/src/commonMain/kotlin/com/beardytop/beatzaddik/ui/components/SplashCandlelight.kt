package com.beardytop.beatzaddik.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

internal data class CandleFlickerState(val flicker: Float, val glow: Float)

@Composable
internal fun rememberCandleFlickerState(): CandleFlickerState {
    val infiniteTransition = rememberInfiniteTransition(label = "flame")
    val flicker by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            tween(420, easing = LinearEasing),
            RepeatMode.Reverse
        ),
        label = "flicker"
    )
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "glow"
    )
    return CandleFlickerState(flicker, glow)
}

@Composable
internal fun SplashCandleAmbient(
    flameScale: Float,
    flickerState: CandleFlickerState,
    modifier: Modifier = Modifier,
    intensified: Boolean = false
) {
    val scale = flameScale.coerceIn(0f, 1.2f)
    Canvas(modifier = modifier.fillMaxSize()) {
        drawCandleAmbientGlow(
            center = Offset(size.width * 0.5f, size.height * 0.44f),
            radius = size.width * if (intensified) 0.72f else 0.55f,
            glow = flickerState.glow,
            scale = scale,
            intensified = intensified
        )
    }
}

@Composable
internal fun SplashCandleCanvas(
    flameScale: Float,
    flickerState: CandleFlickerState,
    modifier: Modifier = Modifier.height(160.dp),
    intensified: Boolean = false
) {
    val scale = flameScale.coerceIn(0f, 1.2f)
    val haloAlpha = if (intensified) 0.38f else 0.22f
    val haloOuter = if (intensified) 0.18f else 0.10f
    Canvas(modifier = modifier.alpha(scale)) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val candleW = w * 0.18f
        val candleH = h * 0.35f
        val candleTop = h * 0.64f

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFFBEE), Color(0xFFFFF0CC)),
                startY = candleTop,
                endY = candleTop + candleH
            ),
            topLeft = Offset(cx - candleW / 2, candleTop),
            size = Size(candleW, candleH)
        )
        drawLine(
            color = Color(0xFF3A2A10),
            start = Offset(cx, candleTop),
            end = Offset(cx, candleTop - h * 0.06f),
            strokeWidth = w * 0.012f
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFE082).copy(alpha = haloAlpha * flickerState.flicker * scale),
                    Color(0xFFFF8C00).copy(alpha = haloOuter * scale),
                    Color.Transparent
                ),
                center = Offset(cx, candleTop - h * 0.24f),
                radius = w * (if (intensified) 0.55f else 0.42f)
            ),
            radius = w * (if (intensified) 0.55f else 0.42f),
            center = Offset(cx, candleTop - h * 0.24f)
        )
        drawCandleFlame(
            cx = cx,
            tipY = candleTop - h * 0.56f * scale,
            baseY = candleTop - h * 0.04f,
            maxWidth = w * 0.22f,
            flicker = flickerState.flicker,
            alpha = scale
        )
    }
}

/** Full-screen splash candle (ambient + flame), used for the completion reward. */
@Composable
fun SplashCandlelight(
    flameScale: Float,
    modifier: Modifier = Modifier,
    intensified: Boolean = false
) {
    val flickerState = rememberCandleFlickerState()
    val candleHeight = if (intensified) 200.dp else 160.dp
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SplashCandleAmbient(
            flameScale = flameScale,
            flickerState = flickerState,
            modifier = Modifier.fillMaxSize(),
            intensified = intensified
        )
        SplashCandleCanvas(
            flameScale = flameScale,
            flickerState = flickerState,
            modifier = Modifier.height(candleHeight),
            intensified = intensified
        )
    }
}
