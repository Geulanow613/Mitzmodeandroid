package com.beardytop.beatzaddik.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Brief splash-style candlelight when all daily mitzvot are complete.
 * Visual only — does not block interaction or show a dialog.
 */
@Composable
fun CandlelightRewardOverlay(
    visible: Boolean,
    flameScale: Float,
    modifier: Modifier = Modifier
) {
    if (!visible) return
    val glow = flameScale.coerceIn(0f, 1.2f)
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width * 0.5f
            val cy = size.height * 0.44f
            val r = size.maxDimension * 0.85f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFF3C4).copy(alpha = 0.35f * glow),
                        Color(0xFFFFB74D).copy(alpha = 0.18f * glow),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy),
                    radius = r
                ),
                radius = r,
                center = Offset(cx, cy)
            )
        }
        SplashCandlelight(flameScale = flameScale, intensified = true)
    }
}

class CandlelightRewardController internal constructor(
    private val flameScale: Animatable<Float, AnimationVector1D>,
    private val scope: kotlinx.coroutines.CoroutineScope
) {
    var visible by mutableStateOf(false)
        private set

    val scale: Float get() = flameScale.value

    fun trigger() {
        if (visible) return
        scope.launch {
            visible = true
            flameScale.snapTo(0.5f)
            flameScale.animateTo(1.12f, tween(850, easing = FastOutSlowInEasing))
            delay(1_750)
            flameScale.animateTo(0f, tween(650, easing = FastOutSlowInEasing))
            visible = false
        }
    }
}

@Composable
fun rememberCandlelightRewardController(): CandlelightRewardController {
    val flameScale = remember { Animatable(0f) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    return remember { CandlelightRewardController(flameScale, scope) }
}
