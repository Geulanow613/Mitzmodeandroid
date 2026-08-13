package com.beardytop.mitzmode.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TourStep(
    val message: String
)

@Composable
fun GlowingDot(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFFD700),
    dotSize: androidx.compose.ui.unit.Dp = 10.dp
) {
    val density = LocalDensity.current
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val sizePx = with(density) { dotSize.toPx() }
    Box(
        modifier = modifier
            .size((dotSize.value * 2).dp)
            .drawBehind {
                drawCircle(
                    color = color.copy(alpha = alpha * 0.4f),
                    radius = sizePx,
                    center = center
                )
                drawCircle(
                    color = color.copy(alpha = alpha),
                    radius = sizePx / 2f,
                    center = center
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(dotSize)
                .drawBehind {
                    drawCircle(
                        color = Color.White.copy(alpha = alpha * 0.9f),
                        radius = sizePx / 4f,
                        center = center
                    )
                }
        )
    }
}

@Composable
fun AppTourOverlay(
    currentStep: Int,
    steps: List<TourStep>,
    stepBounds: Map<Int, Rect>,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (steps.isEmpty() || currentStep !in steps.indices) return

    val density = LocalDensity.current
    val stepMessage = steps[currentStep].message
    val hasNext = currentStep < steps.lastIndex
    val hasBack = currentStep > 0
    val paddingPx = with(density) { 24.dp.roundToPx() }

    TourContent(
        currentStep = currentStep,
        stepMessage = stepMessage,
        hasNext = hasNext,
        hasBack = hasBack,
        stepBounds = stepBounds,
        paddingPx = paddingPx,
        onNext = onNext,
        onBack = onBack,
        onSkip = onSkip,
        onDismiss = onDismiss,
        modifier = modifier
    )
}

@Composable
private fun TourContent(
    currentStep: Int,
    stepMessage: String,
    hasNext: Boolean,
    hasBack: Boolean,
    stepBounds: Map<Int, Rect>,
    paddingPx: Int,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    Box(modifier = modifier.fillMaxSize()) {
        // Frame-based scrim: draw 4 rects around the highlight so the center stays visible (no BlendMode)
        Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val scrimColor = Color.Black.copy(alpha = 0.72f)

            stepBounds[currentStep]?.let { highlightRect ->
                val padded = highlightRect.inflate(paddingPx.toFloat())
                val left = padded.left.coerceIn(0f, w)
                val top = padded.top.coerceIn(0f, h)
                val right = padded.right.coerceIn(0f, w)
                val bottom = padded.bottom.coerceIn(0f, h)

                // Top strip
                if (top > 0f) drawRect(scrimColor, topLeft = Offset(0f, 0f), size = Size(w, top))
                // Bottom strip
                if (bottom < h) drawRect(
                    scrimColor,
                    topLeft = Offset(0f, bottom),
                    size = Size(w, h - bottom)
                )
                // Left strip
                if (left > 0f) drawRect(
                    scrimColor,
                    topLeft = Offset(0f, top),
                    size = Size(left, bottom - top)
                )
                // Right strip
                if (right < w) drawRect(
                    scrimColor,
                    topLeft = Offset(right, top),
                    size = Size(w - right, bottom - top)
                )
            } ?: run {
                // No bounds yet: full scrim
                drawRect(scrimColor)
            }
        }

        // Tour card positioning: keep cards off the highlighted control
        val cardAlignment = when (currentStep) {
            1 -> BiasAlignment(0f, -0.55f)     // Checklist button at bottom — card at top
            2, 3 -> BiasAlignment(0f, 0.5f)   // Menu, Add a Mitzvah
            4 -> BiasAlignment(0f, -0.5f)    // Mitzvah count / level
            else -> Alignment.BottomCenter   // Mitzvah Me button
        }
        Card(
            modifier = Modifier
                .align(cardAlignment)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(
                    top = when (currentStep) {
                        1, 4 -> 24.dp
                        else -> 0.dp
                    },
                    bottom = if (cardAlignment == Alignment.BottomCenter) 48.dp else 24.dp
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TranslatableText(
                    text = stepMessage,
                    enableHalachicTerms = false,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (hasBack) {
                            TextButton(onClick = onBack) {
                                TranslatableText("Back", enableHalachicTerms = false, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        TextButton(onClick = onSkip) {
                            TranslatableText("Skip", enableHalachicTerms = false, color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (hasNext) {
                            Button(
                                onClick = onNext,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                TranslatableText("Next", enableHalachicTerms = false)
                            }
                        } else {
                            Button(
                                onClick = onNext,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                TranslatableText("Done", enableHalachicTerms = false)
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close tour",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

