package com.beardytop.beatzaddik.ui.tour

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.beatzaddik.ui.components.AppText
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

data class AppTourStep(
    val title: String,
    val message: String,
    val target: TourTarget? = null,
    /** Bottom-nav tab to show while this step is active (null = leave current). */
    val forceTab: Int? = null,
    val cardBiasY: Float = 0f,
)

fun mitzModeAppTourSteps(): List<AppTourStep> = listOf(
    AppTourStep(
        title = "Welcome to Mitz Mode!",
        message = "Your daily companion for Torah-observant living. " +
            "Ready to level up your spiritual growth? Here is a quick tour of how the app keeps you in-the-know, " +
            "tracks your daily mitzvot, and brings you closer to your Creator with every tap!",
        cardBiasY = 0f,
    ),
    AppTourStep(
        title = "Today & what’s coming",
        message = "Up here you’ll find the date, Hebrew calendar, and upcoming Jewish holidays " +
            "and observances. The app uses GPS (or your chosen city) to calculate accurate zmanim — prayer times, " +
            "candle lighting, seasonal items, and more — for where you are.",
        target = TourTarget.HeaderUpcoming,
        forceTab = 0,
        cardBiasY = 0.55f,
    ),
    AppTourStep(
        title = "Your checklist",
        message = "Down here is today’s checklist. Mitzvot appear as they become available. " +
            "Check them off as you complete them — they usually reset at nightfall (tzeit), " +
            "but the exact window depends on each mitzvah.",
        target = TourTarget.Checklist,
        forceTab = 0,
        cardBiasY = -0.35f,
    ),
    AppTourStep(
        title = "Mitzvah button",
        message = "Tap the center Mitzvah button anytime for a mitzvah you can do right now — " +
            "Torah learning, prayer, good deeds, and more. Instant inspiration when you want it. " +
            "Got a couple minutes in an elevator? Now you can pop a mitzvah into your life!",
        target = TourTarget.NavMitzvah,
        forceTab = 0,
        cardBiasY = -0.45f,
    ),
    AppTourStep(
        title = "Kashrut timer",
        message = "Ate meat or dairy? Start a wait timer here so you know when it’s time for the next meal category — " +
            "calm, clear kashrut timing without guesswork.",
        target = TourTarget.NavTimer,
        forceTab = 1,
        cardBiasY = -0.45f,
    ),
    AppTourStep(
        title = "Blessings",
        message = "Common blessings are ready here — including Birkat Hamazon — for when you don’t have a siddur handy. " +
            "You’re all set. Let’s go make some light!",
        target = TourTarget.NavBless,
        forceTab = 2,
        cardBiasY = -0.45f,
    ),
)

@Composable
fun MitzModeAppTourOverlay(
    currentStep: Int,
    steps: List<AppTourStep>,
    targetBounds: Map<TourTarget, Rect>,
    overlayOriginInRoot: Offset,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (steps.isEmpty() || currentStep !in steps.indices) return

    val step = steps[currentStep]
    val density = LocalDensity.current
    val padPx = with(density) { 14.dp.toPx() }
    val cornerPx = with(density) { 18.dp.toPx() }

    val infinite = rememberInfiniteTransition(label = "tourGlow")
    val pulse by infinite.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )
    val sparkle by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "sparkle",
    )

    val highlightLocal = remember(step.target, targetBounds, overlayOriginInRoot, padPx) {
        step.target?.let { id ->
            targetBounds[id]?.let { root ->
                Rect(
                    left = root.left - overlayOriginInRoot.x - padPx,
                    top = root.top - overlayOriginInRoot.y - padPx,
                    right = root.right - overlayOriginInRoot.x + padPx,
                    bottom = root.bottom - overlayOriginInRoot.y + padPx,
                )
            }
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val w = constraints.maxWidth.toFloat()
        val h = constraints.maxHeight.toFloat()

        Canvas(modifier = Modifier.fillMaxSize()) {
            val scrim = Color.Black.copy(alpha = 0.72f)
            val hole = highlightLocal?.let { rect ->
                Rect(
                    left = rect.left.coerceIn(0f, w),
                    top = rect.top.coerceIn(0f, h),
                    right = rect.right.coerceIn(0f, w),
                    bottom = rect.bottom.coerceIn(0f, h),
                ).takeIf { it.width > 8f && it.height > 8f }
            }

            if (hole == null) {
                drawRect(scrim)
            } else {
                val left = hole.left
                val top = hole.top
                val right = hole.right
                val bottom = hole.bottom
                if (top > 0f) drawRect(scrim, Offset(0f, 0f), Size(w, top))
                if (bottom < h) drawRect(scrim, Offset(0f, bottom), Size(w, h - bottom))
                if (left > 0f) drawRect(scrim, Offset(0f, top), Size(left, bottom - top))
                if (right < w) drawRect(scrim, Offset(right, top), Size(w - right, bottom - top))

                val glow = TzaddikColors.GoldBright.copy(alpha = 0.25f + 0.45f * pulse)
                val stroke = TzaddikColors.GoldBright.copy(alpha = 0.55f + 0.4f * pulse)
                drawRoundRect(
                    color = glow,
                    topLeft = Offset(left, top),
                    size = Size(right - left, bottom - top),
                    cornerRadius = CornerRadius(cornerPx, cornerPx),
                    style = Stroke(width = 10f + 6f * pulse),
                )
                drawRoundRect(
                    color = stroke,
                    topLeft = Offset(left, top),
                    size = Size(right - left, bottom - top),
                    cornerRadius = CornerRadius(cornerPx, cornerPx),
                    style = Stroke(width = 2.5f),
                )
            }

            // Soft gold sparkles near the card / center
            val sparkleColor = TzaddikColors.GoldBright
            for (i in 0 until 8) {
                val t = (sparkle + i / 8f) % 1f
                val x = w * (0.12f + ((i * 37) % 80) / 100f)
                val y = h * (0.18f + ((i * 53) % 60) / 100f)
                val a = (0.15f + 0.35f * (1f - kotlin.math.abs(t - 0.5f) * 2f)) * pulse
                drawCircle(
                    color = sparkleColor.copy(alpha = a),
                    radius = 2.2f + (i % 3),
                    center = Offset(x, y),
                )
            }
        }

        val cardAlignment: Alignment = when {
            step.target == null -> Alignment.Center
            else -> BiasAlignment(0f, step.cardBiasY.coerceIn(-0.85f, 0.85f))
        }

        Column(
            modifier = Modifier
                .align(cardAlignment)
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top-right dismiss lives on the card row below; also a floating X near top
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(40.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(TzaddikColors.ParchBase.copy(alpha = 0.95f))
                        .border(1.dp, TzaddikColors.GoldBorder.copy(alpha = 0.55f), CircleShape),
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close tour",
                        tint = TzaddikColors.TextBrown,
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    (fadeIn(tween(220)) + slideInVertically { it / 6 }) togetherWith
                        (fadeOut(tween(160)) + slideOutVertically { -it / 8 })
                },
                label = "tourCard",
            ) { stepIndex ->
                val s = steps[stepIndex]
                val stepHasNext = stepIndex < steps.lastIndex
                val stepHasBack = stepIndex > 0
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(16.dp, RoundedCornerShape(22.dp), ambientColor = TzaddikColors.GoldBorder)
                        .clip(RoundedCornerShape(22.dp))
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    TzaddikColors.ParchTop,
                                    TzaddikColors.ParchMid,
                                    TzaddikColors.ParchBase,
                                ),
                            ),
                        )
                        .border(
                            width = 1.4.dp,
                            brush = Brush.linearGradient(
                                listOf(
                                    TzaddikColors.GoldBright.copy(alpha = 0.85f),
                                    TzaddikColors.GoldBorder.copy(alpha = 0.45f),
                                    TzaddikColors.GoldBright.copy(alpha = 0.7f),
                                ),
                            ),
                            shape = RoundedCornerShape(22.dp),
                        )
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        steps.indices.forEach { i ->
                            val active = i == stepIndex
                            Box(
                                modifier = Modifier
                                    .height(7.dp)
                                    .width(if (active) 22.dp else 7.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (active) TzaddikColors.GoldBright
                                        else TzaddikColors.GoldBorder.copy(alpha = 0.35f),
                                    ),
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    AppText(
                        "Step ${stepIndex + 1} of ${steps.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = TzaddikColors.TextMuted,
                        enableTerms = false,
                    )
                    Spacer(Modifier.height(12.dp))
                    AppText(
                        s.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 21.sp,
                        ),
                        color = TzaddikColors.NavyDeep,
                        textAlign = TextAlign.Center,
                        enableTerms = false,
                    )
                    Spacer(Modifier.height(10.dp))
                    AppText(
                        s.message,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.5.sp, lineHeight = 22.sp),
                        color = TzaddikColors.TextBrown,
                        textAlign = TextAlign.Center,
                        enableTerms = false,
                    )
                    Spacer(Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            if (stepHasBack) {
                                TextButton(onClick = onBack) {
                                    AppText("Previous", color = TzaddikColors.GoldBright, enableTerms = false)
                                }
                            }
                            TextButton(onClick = onSkip) {
                                AppText("Skip", color = TzaddikColors.TextMuted, enableTerms = false)
                            }
                        }
                        Button(
                            onClick = onNext,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TzaddikColors.GoldBright,
                                contentColor = TzaddikColors.NavyDeep,
                            ),
                            shape = RoundedCornerShape(14.dp),
                        ) {
                            AppText(
                                if (stepHasNext) "Next" else "Let's go!",
                                color = TzaddikColors.NavyDeep,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                enableTerms = false,
                            )
                        }
                    }
                }
            }
        }
    }
}
