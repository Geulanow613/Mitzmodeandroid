package com.beardytop.beatzaddik.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

private val GoldRay = Color(0xFFFFD700)

/**
 * Full-screen holy light sweep (MitzMode accept-mitzvah effect).
 */
@Composable
fun HolyLightFlashOverlay(
    visible: Boolean,
    alpha: Float,
    sweepT: Float,
    variant: Int,
    modifier: Modifier = Modifier
) {
    if (!visible) return
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        val w = size.width
        val h = size.height
        val maxD = max(w, h)
        val pivot = Offset(w * 0.5f, h * 0.5f)
        when (variant % 4) {
            0 -> {
                drawHolyFlashAmbientSoft(pivot, maxD)
                drawHolyFlashDiagonalSheets(sweepT, pivot, w, h, maxD, inverted = false)
            }
            1 -> {
                drawHolyFlashAmbientFullScreen(pivot, maxD)
                drawHolyFlashFullScreenSheet(sweepT, w, h, maxD)
            }
            2 -> {
                drawHolyFlashAmbientSoft(pivot, maxD)
                drawHolyFlashDiagonalSheets(sweepT, pivot, w, h, maxD, inverted = true)
            }
            else -> {
                drawHolyFlashAmbientSoft(pivot, maxD)
                drawHolyFlashBottomToTopSheets(sweepT, w, h, maxD)
            }
        }
    }
}

class HolyFlashController internal constructor(
    private val flashAlpha: Animatable<Float, AnimationVector1D>,
    private val sweepProgress: Animatable<Float, AnimationVector1D>,
    private val scope: kotlinx.coroutines.CoroutineScope
) {
    var visible by mutableStateOf(false)
        private set
    var displayedVariant by mutableIntStateOf(0)
        private set
    private var nextVariant = 0

    val alpha: Float get() = flashAlpha.value
    val sweepT: Float get() = sweepProgress.value

    fun trigger() {
        val variant = nextVariant
        displayedVariant = variant
        visible = true
        scope.launch {
            flashAlpha.snapTo(0f)
            sweepProgress.snapTo(0f)
            coroutineScope {
                val fade = async {
                    flashAlpha.animateTo(1f, tween(120, easing = LinearEasing))
                    delay(140)
                    flashAlpha.animateTo(0f, tween(780, easing = LinearOutSlowInEasing))
                }
                val sweep = async {
                    sweepProgress.animateTo(1f, tween(520, easing = FastOutSlowInEasing))
                }
                fade.await()
                sweep.await()
            }
            visible = false
            nextVariant = (variant + 1) % 4
        }
    }
}

@Composable
fun rememberHolyFlashController(): HolyFlashController {
    val flashAlpha = remember { Animatable(0f) }
    val sweepProgress = remember { Animatable(0f) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    return remember { HolyFlashController(flashAlpha, sweepProgress, scope) }
}

private fun DrawScope.drawHolyFlashAmbientSoft(pivot: Offset, maxD: Float) {
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFF8E1).copy(alpha = 0.28f),
                Color(0xFFFFECB3).copy(alpha = 0.10f),
                Color.Transparent
            ),
            center = pivot,
            radius = maxD * 1.05f
        )
    )
}

private fun DrawScope.drawHolyFlashAmbientFullScreen(pivot: Offset, maxD: Float) {
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFFDE7).copy(alpha = 0.42f),
                Color(0xFFFFECB3).copy(alpha = 0.18f),
                Color(0xFFFFF8E1).copy(alpha = 0.08f),
                Color.Transparent
            ),
            center = pivot,
            radius = maxD * 1.2f
        )
    )
}

private fun DrawScope.drawHolyFlashDiagonalSheets(
    sweepT: Float,
    pivot: Offset,
    w: Float,
    h: Float,
    maxD: Float,
    inverted: Boolean
) {
    val t = if (inverted) 1f - sweepT else sweepT
    val tLag = (t - 0.07f).coerceAtLeast(0f)
    rotate(degrees = -34f, pivot = pivot) {
        val bladeW = maxD * 0.38f
        val travel = w + bladeW * 2.4f
        val cx = -bladeW * 1.2f + t * travel
        drawRect(
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Transparent,
                    0.35f to Color(0xFFFFFDE7).copy(alpha = 0.5f),
                    0.5f to Color.White.copy(alpha = 0.95f),
                    0.65f to Color(0xFFFFF8DC).copy(alpha = 0.55f),
                    1f to Color.Transparent
                ),
                start = Offset(cx, 0f),
                end = Offset(cx + bladeW, 0f)
            ),
            topLeft = Offset(cx, -h * 0.6f),
            size = Size(bladeW, h * 2.4f)
        )
    }
    rotate(degrees = -34f, pivot = pivot) {
        val bladeW = maxD * 0.52f
        val travel = w + bladeW * 2.1f
        val cx = -bladeW * 1.15f + tLag * travel
        drawRect(
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Transparent,
                    0.40f to GoldRay.copy(alpha = 0.32f),
                    0.5f to Color(0xFFFFFDE7).copy(alpha = 0.42f),
                    0.60f to GoldRay.copy(alpha = 0.24f),
                    1f to Color.Transparent
                ),
                start = Offset(cx, 0f),
                end = Offset(cx + bladeW, 0f)
            ),
            topLeft = Offset(cx, -h * 0.6f),
            size = Size(bladeW, h * 2.4f)
        )
    }
}

private fun DrawScope.drawHolyFlashFullScreenSheet(sweepT: Float, w: Float, h: Float, maxD: Float) {
    val bladeW = maxD * 1.28f
    val travel = w + bladeW * 0.85f
    val cx = -bladeW * 0.08f + sweepT * travel
    drawRect(
        brush = Brush.linearGradient(
            colorStops = arrayOf(
                0f to Color.Transparent,
                0.22f to Color(0xFFFFFDE7).copy(alpha = 0.55f),
                0.45f to Color.White.copy(alpha = 0.98f),
                0.55f to Color.White.copy(alpha = 0.98f),
                0.78f to Color(0xFFFFF8DC).copy(alpha = 0.50f),
                1f to Color.Transparent
            ),
            start = Offset(cx, 0f),
            end = Offset(cx + bladeW, 0f)
        ),
        topLeft = Offset(cx, -h * 0.08f),
        size = Size(bladeW, h * 1.16f)
    )
    val lag = (sweepT - 0.06f).coerceAtLeast(0f)
    val cx2 = -bladeW * 0.12f + lag * travel
    drawRect(
        brush = Brush.linearGradient(
            colorStops = arrayOf(
                0f to Color.Transparent,
                0.35f to GoldRay.copy(alpha = 0.38f),
                0.5f to Color(0xFFFFFDE7).copy(alpha = 0.48f),
                0.65f to GoldRay.copy(alpha = 0.26f),
                1f to Color.Transparent
            ),
            start = Offset(cx2, 0f),
            end = Offset(cx2 + bladeW * 0.92f, 0f)
        ),
        topLeft = Offset(cx2, -h * 0.06f),
        size = Size(bladeW * 0.92f, h * 1.12f)
    )
}

private fun DrawScope.drawHolyFlashBottomToTopSheets(sweepT: Float, w: Float, h: Float, maxD: Float) {
    val bandH = maxD * 0.42f
    val travel = h + bandH * 2.35f
    val cy = h + bandH * 0.35f - sweepT * travel
    drawRect(
        brush = Brush.linearGradient(
            colorStops = arrayOf(
                0f to Color.Transparent,
                0.32f to Color(0xFFFFFDE7).copy(alpha = 0.52f),
                0.5f to Color.White.copy(alpha = 0.94f),
                0.68f to Color(0xFFFFF8DC).copy(alpha = 0.52f),
                1f to Color.Transparent
            ),
            start = Offset(0f, cy),
            end = Offset(0f, cy + bandH)
        ),
        topLeft = Offset(-w * 0.12f, cy),
        size = Size(w * 1.24f, bandH)
    )
    val tLag = (sweepT - 0.07f).coerceAtLeast(0f)
    val cy2 = h + bandH * 0.35f - tLag * travel
    val bandH2 = maxD * 0.52f
    drawRect(
        brush = Brush.linearGradient(
            colorStops = arrayOf(
                0f to Color.Transparent,
                0.38f to GoldRay.copy(alpha = 0.34f),
                0.5f to Color(0xFFFFFDE7).copy(alpha = 0.44f),
                0.62f to GoldRay.copy(alpha = 0.24f),
                1f to Color.Transparent
            ),
            start = Offset(0f, cy2),
            end = Offset(0f, cy2 + bandH2)
        ),
        topLeft = Offset(-w * 0.10f, cy2),
        size = Size(w * 1.20f, bandH2)
    )
}
