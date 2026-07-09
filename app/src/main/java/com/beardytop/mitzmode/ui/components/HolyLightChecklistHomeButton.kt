package com.beardytop.mitzmode.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class SparkleSpec(
    val xFrac: Float,
    val yFrac: Float,
    val phase: Float,
    val sizeDp: Float,
)

@Composable
fun HolyLightChecklistHomeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sparkles = remember {
        List(10) { index ->
            SparkleSpec(
                xFrac = when (index % 4) {
                    0 -> Random.nextFloat() * 0.22f + 0.04f
                    1 -> Random.nextFloat() * 0.22f + 0.74f
                    else -> Random.nextFloat() * 0.56f + 0.22f
                },
                yFrac = Random.nextFloat() * 0.75f + 0.12f,
                phase = index * 0.31f,
                sizeDp = 4.5f + (index % 3) * 1.8f,
            )
        }
    }
    val transition = rememberInfiniteTransition(label = "holyLightSparkle")
    val shimmer by transition.animateFloat(
        initialValue = -0.35f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(tween(9600, easing = LinearEasing)),
        label = "shimmer",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(50),
                spotColor = Color(0xFFFFD56B),
            )
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFFFFF3B5),
                        Color(0xFFFFE082),
                        Color(0xFFFFD56B),
                        Color(0xFFE0AB2F),
                    ),
                ),
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0f),
                        Color.White.copy(alpha = 0.55f),
                        Color.White.copy(alpha = 0f),
                    ),
                    start = Offset(shimmer * 900f, 0f),
                    end = Offset(shimmer * 900f + 180f, 0f),
                ),
            )
            .border(
                width = 1.6.dp,
                color = Color(0xFFFFF8D6),
                shape = RoundedCornerShape(50),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 15.dp),
        contentAlignment = Alignment.Center,
    ) {
        SparkleField(
            sparkles = sparkles,
            modifier = Modifier.matchParentSize(),
        )
        TranslatableText(
            "Holy Light Checklist",
            enableHalachicTerms = false,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.55.sp,
                textAlign = TextAlign.Center,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.32f),
                    offset = Offset(0f, 1.8f),
                    blurRadius = 2.8f,
                ),
            ),
            color = Color(0xFF1A3D72),
        )
    }
}

@Composable
private fun SparkleField(
    sparkles: List<SparkleSpec>,
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "sparkleField")
    val tick by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(5400, easing = LinearEasing)),
        label = "tick",
    )

    Canvas(modifier = modifier) {
        sparkles.forEach { spec ->
            val phase = (tick + spec.phase) % 1f
            val alpha = when {
                phase < 0.35f -> phase / 0.35f
                phase < 0.7f -> 1f - (phase - 0.35f) / 0.35f
                else -> 0.12f
            }
            val scale = 0.55f + alpha * 0.65f
            val center = Offset(size.width * spec.xFrac, size.height * spec.yFrac)
            val radius = spec.sizeDp * density * scale
            drawJewishStar(
                center = center,
                radius = radius,
                color = Color.White.copy(alpha = 0.25f + alpha * 0.75f),
            )
            drawJewishStar(
                center = center,
                radius = radius * 0.55f,
                color = Color(0xFFFFD56B).copy(alpha = alpha * 0.85f),
            )
        }
    }
}

private fun DrawScope.drawJewishStar(
    center: Offset,
    radius: Float,
    color: Color,
) {
    fun trianglePath(startAngleRad: Double): Path {
        val path = Path()
        repeat(3) { i ->
            val angle = startAngleRad + i * 2.0 * PI / 3.0
            val x = center.x + (cos(angle) * radius).toFloat()
            val y = center.y + (sin(angle) * radius).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        return path
    }
    drawPath(trianglePath(-PI / 2), color)
    drawPath(trianglePath(PI / 2), color)
}
