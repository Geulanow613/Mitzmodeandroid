package com.beardytop.mitzmode.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.beardytop.mitzmode.R
import com.beardytop.mitzmode.data.Mitzvah
import com.beardytop.mitzmode.util.DeviceCapabilityChecker
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import java.util.concurrent.atomic.AtomicBoolean

// ── Sparkle helpers (kept for external use) ─────────────────────────────────

@Composable
fun SparkleText(
    text: String,
    isAnimating: Boolean,
    onAnimationComplete: () -> Unit,
    modifier: Modifier = Modifier,
    textSize: TextUnit = 16.sp
) {
    val context = LocalContext.current
    val isCapableDevice = DeviceCapabilityChecker.canHandleVideoBackground(context)

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            delay(1000)
            onAnimationComplete()
        }
    }

    Box(contentAlignment = Alignment.Center) {
        Text(
            text = text,
            modifier = modifier
                .alpha(if (isAnimating) 0f else 1f)
                .graphicsLayer(
                    scaleX = if (isAnimating) 1.1f else 1f,
                    scaleY = if (isAnimating) 1.1f else 1f
                ),
            style = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                fontSize = textSize
            ),
            color = Color.White,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Visible
        )

        if (isAnimating && isCapableDevice) {
            val particles = remember { List(5) { Random.nextFloat() } }
            particles.forEach { offset ->
                val transition = rememberInfiniteTransition(label = "sparkle")
                val particleAlpha by transition.animateFloat(
                    initialValue = 1f, targetValue = 0f,
                    animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart),
                    label = "alpha"
                )
                val particleOffset by transition.animateFloat(
                    initialValue = 0f, targetValue = -50f,
                    animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart),
                    label = "offset"
                )
                Text(
                    text = "✡",
                    modifier = Modifier
                        .offset(x = (offset * 50 - 25).dp, y = particleOffset.dp)
                        .alpha(particleAlpha),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun TranslatableSparkleText(
    text: String,
    isAnimating: Boolean,
    onAnimationComplete: () -> Unit,
    modifier: Modifier = Modifier,
    textSize: TextUnit = 16.sp,
) {
    val context = LocalContext.current
    val isCapableDevice = DeviceCapabilityChecker.canHandleVideoBackground(context)

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            delay(1000)
            onAnimationComplete()
        }
    }

    Box(contentAlignment = Alignment.Center) {
        Text(
            text = text,
            modifier = modifier
                .alpha(if (isAnimating) 0f else 1f)
                .graphicsLayer(
                    scaleX = if (isAnimating) 1.1f else 1f,
                    scaleY = if (isAnimating) 1.1f else 1f
                ),
            style = MaterialTheme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                fontSize = textSize
            ),
            color = Color.White,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Visible
        )

        if (isAnimating && isCapableDevice) {
            val particles = remember { List(5) { Random.nextFloat() } }
            particles.forEach { offset ->
                val transition = rememberInfiniteTransition(label = "sparkle")
                val particleAlpha by transition.animateFloat(
                    initialValue = 1f, targetValue = 0f,
                    animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart),
                    label = "alpha"
                )
                val particleOffset by transition.animateFloat(
                    initialValue = 0f, targetValue = -50f,
                    animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Restart),
                    label = "offset"
                )
                Text(
                    text = "✡",
                    modifier = Modifier
                        .offset(x = (offset * 50 - 25).dp, y = particleOffset.dp)
                        .alpha(particleAlpha),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
            }
        }
    }
}

// ── Theme constants ──────────────────────────────────────────────────────────

private val ParchmentTop  = Color(0xFFFFFBEE)
private val ParchmentMid  = Color(0xFFFFF8E1)
private val ParchmentBase = Color(0xFFFFF0CC)
private val GoldBorder    = Color(0xFFB8860B)
private val GoldRay       = Color(0xFFFFD700)
private val NavyBlue      = Color(0xFF2B5BA8)
private val TextBrown     = Color(0xFF1A0A00)
private val CardShape     = RoundedCornerShape(20.dp)

// ── Main dialog ──────────────────────────────────────────────────────────────

@Composable
fun MitzvahDialog(
    mitzvah: Mitzvah,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    onNext: () -> Unit
) {
    var accepted by remember(mitzvah.id) { mutableStateOf(false) }
    /** Synchronous guard: Compose may not recompose before a second tap sees old [accepted]. */
    val acceptOnce = remember(mitzvah.id) { AtomicBoolean(false) }
    var textSize by remember { mutableStateOf(16.sp) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val flashAlpha = remember { Animatable(0f) }
    val sweepProgress = remember { Animatable(0f) }
    var flashVisible by remember { mutableStateOf(false) }
    /** Cycles 0→1→2→3 on each Accept: diagonal L→R, full-screen sheet, diagonal R→L, bottom→top. */
    var acceptFlashVariant by remember { mutableIntStateOf(0) }
    /** Snapshot of [acceptFlashVariant] for the in-flight flash overlay. */
    var displayedFlashVariant by remember { mutableIntStateOf(0) }

    LaunchedEffect(mitzvah.id) {
        scrollState.scrollTo(0)
        acceptOnce.set(false)
        accepted = false
        sweepProgress.snapTo(0f)
    }

    val configuration = LocalConfiguration.current
    val buttonTextSize = when {
        configuration.screenWidthDp < 360 -> 13.sp
        configuration.screenWidthDp < 400 -> 15.sp
        else -> 16.sp
    }

    // Opening tap can otherwise dismiss immediately via outside-click / scrim.
    var allowOutsideDismiss by remember(mitzvah.id) { mutableStateOf(false) }
    LaunchedEffect(mitzvah.id) {
        allowOutsideDismiss = false
        delay(350)
        allowOutsideDismiss = true
    }

    Dialog(
        onDismissRequest = { if (allowOutsideDismiss) onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnClickOutside = false,
        )
    ) {
        Box(Modifier.fillMaxSize()) {

            // Scrim
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable(
                        enabled = allowOutsideDismiss,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDismiss() }
            )

            // ── Dialog content ─────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                val scrollIconSize = 108.dp
                val cardTopPadding = scrollIconSize * 0.68f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    // ── Card ──────────────────────────────────────────
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = cardTopPadding)
                            .wrapContentHeight()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {},
                        shape = CardShape,
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.5.dp, GoldBorder.copy(alpha = 0.6f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                    ) {
                        Box(Modifier.fillMaxWidth()) {
                            // Parchment gradient + filigree frame drawn behind content
                            Canvas(Modifier.matchParentSize()) {
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            0f to ParchmentTop,
                                            0.3f to ParchmentMid,
                                            1f to ParchmentBase
                                        )
                                    )
                                )
                                // Subtle paper-grain wash for an aged feel
                                drawRect(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFEDD9A3).copy(alpha = 0.10f),
                                            Color.Transparent
                                        ),
                                        center = Offset(size.width * 0.5f, size.height * 0.5f),
                                        radius = maxOf(size.width, size.height) * 0.6f
                                    )
                                )
                                drawRect(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFB8860B).copy(alpha = 0.06f),
                                            Color.Transparent
                                        ),
                                        center = Offset(size.width * 0.5f, size.height * 1.05f),
                                        radius = maxOf(size.width, size.height) * 0.7f
                                    )
                                )
                                // Classy inset filigree frame
                                drawFiligreeFrame(GoldBorder)
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Space for scroll overlap
                                Spacer(Modifier.height(scrollIconSize * 0.36f))

                                // Star of David
                                Canvas(Modifier.size(22.dp)) {
                                    drawStarOfDavid(
                                        center = Offset(size.width / 2f, size.height / 2f),
                                        radius = size.minDimension / 2f,
                                        color = GoldBorder.copy(alpha = 0.7f),
                                        strokeWidth = 1.4f.dp.toPx()
                                    )
                                }

                                Spacer(Modifier.height(6.dp))

                                // Controls row
                                Row(
                                    Modifier.fillMaxWidth().padding(horizontal = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.padding(start = 2.dp),
                                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                                    ) {
                                        IconButton(
                                            onClick = { textSize = (textSize.value - 2f).coerceAtLeast(12f).sp },
                                            modifier = Modifier.size(30.dp)
                                        ) {
                                            Text("a", style = MaterialTheme.typography.labelMedium, color = TextBrown.copy(0.55f))
                                        }
                                        IconButton(
                                            onClick = { textSize = (textSize.value + 2f).coerceAtMost(22f).sp },
                                            modifier = Modifier.size(30.dp)
                                        ) {
                                            Text("A", style = MaterialTheme.typography.labelLarge, color = TextBrown.copy(0.55f))
                                        }
                                    }
                                    IconButton(
                                        onClick = onDismiss,
                                        modifier = Modifier
                                            .padding(end = 2.dp)
                                            .size(30.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close, "Close",
                                            tint = TextBrown.copy(0.5f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(Modifier.height(6.dp))
                                // Same horizontal inset as footer divider so both rules span identically
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                    color = GoldBorder.copy(alpha = 0.35f),
                                    thickness = 0.8.dp
                                )
                                Spacer(Modifier.height(8.dp))

                                // Scrollable text
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = (configuration.screenHeightDp * 0.42f).dp)
                                        .verticalScroll(scrollState)
                                        .padding(horizontal = 28.dp, vertical = 6.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    TranslatableText(
                                        text = mitzvah.text,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = textSize,
                                            lineHeight = (textSize.value * 1.55f).sp
                                        ),
                                        color = TextBrown,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                        knownLinks = mitzvah.links.orEmpty(),
                                    )

                                    mitzvah.links.orEmpty().forEach { link ->
                                        LinkText(
                                            displayText = link.displayText.orEmpty(),
                                            url = link.url.orEmpty(),
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = NavyBlue,
                                                fontSize = (textSize.value + 1.5f).sp,
                                                lineHeight = (textSize.value * 1.5f).sp
                                            ),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp)
                                        )
                                    }
                                    Spacer(Modifier.height(4.dp))
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                                    color = GoldBorder.copy(alpha = 0.35f),
                                    thickness = 0.8.dp
                                )

                                // Refined heirloom-style buttons
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 22.dp, vertical = 14.dp),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    HeirloomButton(
                                        text = if (accepted) "Accepted ✓" else "Accept",
                                        textSize = buttonTextSize,
                                        enabled = !accepted,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            if (acceptOnce.compareAndSet(false, true)) {
                                                // Lock UI immediately so the button greys with ✓; persist without waiting on the flash.
                                                accepted = true
                                                onAccept()
                                                scope.launch {
                                                    val variant = acceptFlashVariant
                                                    displayedFlashVariant = variant
                                                    flashVisible = true
                                                    flashAlpha.snapTo(0f)
                                                    sweepProgress.snapTo(0f)
                                                    val fade = async {
                                                        flashAlpha.animateTo(
                                                            1f,
                                                            tween(120, easing = LinearEasing)
                                                        )
                                                        delay(140)
                                                        flashAlpha.animateTo(
                                                            0f,
                                                            tween(780, easing = LinearOutSlowInEasing)
                                                        )
                                                    }
                                                    val sweep = async {
                                                        sweepProgress.animateTo(
                                                            1f,
                                                            tween(520, easing = FastOutSlowInEasing)
                                                        )
                                                    }
                                                    fade.await()
                                                    sweep.await()
                                                    flashVisible = false
                                                    acceptFlashVariant = (variant + 1) % 4
                                                }
                                            }
                                        }
                                    )

                                    HeirloomButton(
                                        text = "Next",
                                        textSize = buttonTextSize,
                                        modifier = Modifier.weight(1f),
                                        onClick = onNext
                                    )
                                }
                            }
                        }
                    }

                    // ── Torah scroll floating above card ──────────────
                    Box(
                        modifier = Modifier
                            .size(scrollIconSize * 1.6f, scrollIconSize)
                            .align(Alignment.TopCenter),
                        contentAlignment = Alignment.Center
                    ) {
                        // Warm golden light — full fan of rays.
                        // Each beam's alpha is driven by its upward direction so rays
                        // pointing down fade to near-zero naturally; no hard clip.
                        Canvas(Modifier.fillMaxSize()) {
                            val cx = size.width / 2f
                            val cy = size.height / 2f
                            val maxR = maxOf(size.width, size.height) * 0.92f

                            for (i in 0 until 8) {
                                val angleDeg = i * 45f
                                val angle = angleDeg * (PI.toFloat() / 180f)
                                // sin(angle) is -1 when pointing straight UP (270°),
                                // +1 when straight DOWN (90°).  Map to [0..1] upward factor.
                                val upFactor = ((1f - sin(angle)) / 2f).coerceIn(0f, 1f)
                                val maxAlpha = if (i % 2 == 0) 0.36f else 0.22f
                                val beamAlpha = maxAlpha * upFactor
                                if (beamAlpha < 0.01f) continue

                                val halfSpread = 0.06f
                                val path = Path().apply {
                                    moveTo(cx, cy)
                                    lineTo(
                                        cx + cos(angle - halfSpread) * maxR,
                                        cy + sin(angle - halfSpread) * maxR
                                    )
                                    lineTo(
                                        cx + cos(angle + halfSpread) * maxR,
                                        cy + sin(angle + halfSpread) * maxR
                                    )
                                    close()
                                }
                                drawPath(
                                    path = path,
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFFFEFC0).copy(alpha = beamAlpha),
                                            Color(0xFFFFD56B).copy(alpha = beamAlpha * 0.55f),
                                            Color.Transparent
                                        ),
                                        center = Offset(cx, cy),
                                        radius = maxR
                                    )
                                )
                            }

                            // Warm candlelight halo — golden, full sphere looks natural
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFFFFBE6).copy(alpha = 0.78f),
                                        Color(0xFFFFE082).copy(alpha = 0.55f),
                                        Color(0xFFFFD56B).copy(alpha = 0.22f),
                                        Color.Transparent
                                    ),
                                    center = Offset(cx, cy),
                                    radius = size.width * 0.40f
                                ),
                                radius = size.width * 0.40f,
                                center = Offset(cx, cy)
                            )
                        }

                        // Actual scroll illustration
                        Image(
                            painter = painterResource(R.drawable.ic_torah_scroll),
                            contentDescription = "Torah scroll",
                            modifier = Modifier
                                .width(scrollIconSize * 1.4f)
                                .height(scrollIconSize * 0.88f)
                        )
                    }
                }
            }

            // ── Full-screen holy flash ─────────────────────────────────
            if (flashVisible) {
                HolyLightFlash(
                    alpha = flashAlpha.value,
                    sweepT = sweepProgress.value,
                    variant = displayedFlashVariant
                )
            }
        }
    }
}

// ── Refined heirloom-style button used in the Mitzvah popup ──────────────────

@Composable
private fun HeirloomButton(
    text: String,
    textSize: androidx.compose.ui.unit.TextUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    // Navy gradient body, double gold border, subtle inner highlight, refined typography.
    val deepNavy = Color(0xFF12305A)
    val midNavy  = Color(0xFF1F4685)
    val brightNavy = Color(0xFF2B5BA8)
    val outerGold = GoldBorder.copy(alpha = if (enabled) 0.85f else 0.45f)
    val innerGold = Color(0xFFFFD56B).copy(alpha = if (enabled) 0.55f else 0.30f)
    val disabledTone = Color(0xFF6E7B92)

    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(50))
            .background(
                brush = if (enabled) {
                    Brush.verticalGradient(
                        colors = listOf(brightNavy, midNavy, deepNavy)
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(disabledTone.copy(alpha = 0.85f), disabledTone)
                    )
                }
            )
            .border(width = 1.4.dp, color = outerGold, shape = RoundedCornerShape(50))
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Inner gold hairline ring for that double-frame feel
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(2.5.dp)
                .border(width = 0.6.dp, color = innerGold, shape = RoundedCornerShape(50))
        )

        // Top-edge glossy highlight
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(2.dp)
                .padding(top = 1.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = if (enabled) 0.18f else 0.06f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(50)
                )
                .align(Alignment.TopCenter)
        )

        TranslatableText(
            text = text,
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = com.beardytop.mitzmode.ui.theme.Cinzel,
                fontSize = textSize,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black.copy(alpha = 0.45f),
                    offset = Offset(0f, 1.2f),
                    blurRadius = 3f
                )
            ),
            color = Color(0xFFFFF6D8)
        )
    }
}

// ── Holy light flash ─────────────────────────────────────────────────────────

/**
 * [variant] 0: diagonal blade lower-left → upper-right. 1: wide horizontal sheet (whole screen).
 * 2: diagonal opposite (right → left). 3: horizontal band rising bottom → top.
 * [sweepT] is 0→1 during the accept animation.
 */
@Composable
private fun HolyLightFlash(alpha: Float, sweepT: Float, variant: Int) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        val w = size.width
        val h = size.height
        val maxD = maxOf(w, h)
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

/** Very wide horizontal bright sheet — reads as light washing over the whole screen. */
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

/** Horizontal band of light traveling upward (bottom → top). */
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

// ── Drawing helpers ──────────────────────────────────────────────────────────

private fun DrawScope.drawStarOfDavid(
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
        color = color, style = style
    )
    drawPath(
        Path().apply {
            moveTo(center.x, center.y + radius)
            lineTo(center.x + radius * cos30, center.y - radius * sin30)
            lineTo(center.x - radius * cos30, center.y - radius * sin30)
            close()
        },
        color = color, style = style
    )
}

/**
 * Draw an elegant inset gold frame: thin double-line rounded border with
 * optional mid-edge accents. Sits on the parchment fill.
 */
private fun DrawScope.drawFiligreeFrame(goldColor: Color) {
    val w = size.width
    val h = size.height
    val inset = 16f.dp.toPx()
    val cornerR = 14f.dp.toPx()
    val outerStroke = 1.0f.dp.toPx()
    val innerStroke = 0.55f.dp.toPx()
    val gapBetweenLines = 3.5f.dp.toPx()

    val outerColor = goldColor.copy(alpha = 0.55f)
    val innerColor = goldColor.copy(alpha = 0.32f)
    val accentColor = goldColor.copy(alpha = 0.70f)

    // ── Outer thin gold rounded-rectangle ────────────────────────────────
    drawRoundRect(
        color = outerColor,
        topLeft = Offset(inset, inset),
        size = androidx.compose.ui.geometry.Size(w - 2 * inset, h - 2 * inset),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerR, cornerR),
        style = Stroke(width = outerStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
    // ── Hairline inner gold rounded-rectangle ────────────────────────────
    drawRoundRect(
        color = innerColor,
        topLeft = Offset(inset + gapBetweenLines, inset + gapBetweenLines),
        size = androidx.compose.ui.geometry.Size(
            w - 2 * (inset + gapBetweenLines),
            h - 2 * (inset + gapBetweenLines)
        ),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
            cornerR - gapBetweenLines * 0.6f,
            cornerR - gapBetweenLines * 0.6f
        ),
        style = Stroke(width = innerStroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // ── Mid-edge pinstripe accents ──────────────────────────────────────
    val edgeAccentLen = 14f.dp.toPx()
    val edgeAccentColor = accentColor.copy(alpha = 0.45f)
    val midX = w / 2f
    val midY = h / 2f
    val edgeStroke = 0.5f.dp.toPx()
    // Top
    drawLine(edgeAccentColor,
        Offset(midX - edgeAccentLen, inset + gapBetweenLines * 1.6f),
        Offset(midX + edgeAccentLen, inset + gapBetweenLines * 1.6f),
        edgeStroke, StrokeCap.Round)
    // Bottom
    drawLine(edgeAccentColor,
        Offset(midX - edgeAccentLen, h - inset - gapBetweenLines * 1.6f),
        Offset(midX + edgeAccentLen, h - inset - gapBetweenLines * 1.6f),
        edgeStroke, StrokeCap.Round)
    // Left
    drawLine(edgeAccentColor,
        Offset(inset + gapBetweenLines * 1.6f, midY - edgeAccentLen),
        Offset(inset + gapBetweenLines * 1.6f, midY + edgeAccentLen),
        edgeStroke, StrokeCap.Round)
    // Right
    drawLine(edgeAccentColor,
        Offset(w - inset - gapBetweenLines * 1.6f, midY - edgeAccentLen),
        Offset(w - inset - gapBetweenLines * 1.6f, midY + edgeAccentLen),
        edgeStroke, StrokeCap.Round)
}
