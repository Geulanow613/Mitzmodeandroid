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
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.R
import com.beardytop.mitzmode.data.Mitzvah
import com.beardytop.mitzmode.util.DeviceCapabilityChecker
import com.beardytop.mitzmode.viewmodel.TranslationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

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
    translationViewModel: TranslationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isCapableDevice = DeviceCapabilityChecker.canHandleVideoBackground(context)
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()

    var translatedText by remember(text, currentLanguage) { mutableStateOf(text) }

    LaunchedEffect(text, currentLanguage, translationEnabled) {
        translatedText = if (translationEnabled && currentLanguage != "en") {
            translationViewModel.translateText(text)
        } else text
    }

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            delay(1000)
            onAnimationComplete()
        }
    }

    Box(contentAlignment = Alignment.Center) {
        Text(
            text = translatedText,
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
    var textSize by remember { mutableStateOf(16.sp) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val flashAlpha = remember { Animatable(0f) }
    var flashVisible by remember { mutableStateOf(false) }

    LaunchedEffect(mitzvah.id) {
        scrollState.scrollTo(0)
        accepted = false
    }

    val configuration = LocalConfiguration.current
    val buttonTextSize = when {
        configuration.screenWidthDp < 360 -> 13.sp
        configuration.screenWidthDp < 400 -> 15.sp
        else -> 16.sp
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(Modifier.fillMaxSize()) {

            // ── Scrim ──────────────────────────────────────────────────
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable(
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
                            // Parchment gradient + vine border drawn behind content
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
                                // Olive branch corner ornaments (kept as-is)
                                drawVineBorder(GoldBorder.copy(alpha = 0.42f))
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
                                        modifier = Modifier.fillMaxWidth()
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
                                            if (!accepted) {
                                                scope.launch {
                                                    flashVisible = true
                                                    flashAlpha.snapTo(0f)
                                                    flashAlpha.animateTo(1f, tween(90, easing = LinearEasing))
                                                    delay(200)
                                                    flashAlpha.animateTo(0f, tween(850, easing = LinearOutSlowInEasing))
                                                    flashVisible = false
                                                    accepted = true
                                                    onAccept()
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
                HolyLightFlash(alpha = flashAlpha.value)
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

@Composable
private fun HolyLightFlash(alpha: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val maxDim = maxOf(size.width, size.height) * 1.15f

        // 1. Solid white fill — completely blinding
        drawRect(Color.White)

        // 2. Soft wide golden beams (same soft style as the scroll glow, but bigger)
        for (i in 0 until 16) {
            val angle = (i * 22.5f) * (PI.toFloat() / 180f)
            val isMain = i % 2 == 0
            val halfSpread = if (isMain) 0.09f else 0.04f
            val beamAlpha = if (isMain) 0.50f else 0.22f
            val path = Path().apply {
                moveTo(center.x, center.y)
                lineTo(
                    center.x + cos(angle - halfSpread) * maxDim,
                    center.y + sin(angle - halfSpread) * maxDim
                )
                lineTo(
                    center.x + cos(angle + halfSpread) * maxDim,
                    center.y + sin(angle + halfSpread) * maxDim
                )
                close()
            }
            drawPath(
                path = path,
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFFFCC).copy(alpha = beamAlpha),
                        Color(0xFFFFEE77).copy(alpha = beamAlpha * 0.6f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = maxDim
                )
            )
        }

        // 3. Concentric soft rings
        for (ring in 1..5) {
            val ringR = maxDim * 0.17f * ring
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0f to Color.Transparent,
                        0.75f to Color(0xFFFFFFDD).copy(alpha = 0.3f / ring),
                        0.88f to GoldRay.copy(alpha = 0.2f / ring),
                        1f to Color.Transparent
                    ),
                    center = center, radius = ringR
                ),
                radius = ringR, center = center
            )
        }

        // 4. Central radial burst: white core → warm gold → transparent
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to Color.White,
                    0.10f to Color(0xFFFFFFEE),
                    0.30f to GoldRay.copy(alpha = 0.65f),
                    0.60f to Color(0xFFFFEE99).copy(alpha = 0.30f),
                    1f to Color.Transparent
                ),
                center = center, radius = maxDim * 0.65f
            ),
            radius = maxDim * 0.65f, center = center
        )

        // 5. Large Star of David silhouette
        drawStarOfDavid(
            center = center,
            radius = 80f.dp.toPx(),
            color = GoldRay.copy(alpha = 0.45f),
            strokeWidth = 4f.dp.toPx()
        )
    }
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
 * Draw an elegant inset filigree gold frame: a thin double-line border with
 * small flourish curls and tiny dots at each corner. Sits between the parchment
 * fill and the olive-branch ornaments to add an antique, classy feel.
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

    // ── Corner flourish: thin curl + tiny dot — antique scrollwork ───────
    val curlReach = 10f.dp.toPx()
    val dotR = 1.1f.dp.toPx()

    // Each corner described by its anchor and outward signs
    data class Corner(val ax: Float, val ay: Float, val sx: Float, val sy: Float)
    val corners = listOf(
        Corner(inset + cornerR * 0.2f,             inset + cornerR * 0.2f,              1f,  1f),
        Corner(w - inset - cornerR * 0.2f,         inset + cornerR * 0.2f,             -1f,  1f),
        Corner(inset + cornerR * 0.2f,             h - inset - cornerR * 0.2f,          1f, -1f),
        Corner(w - inset - cornerR * 0.2f,         h - inset - cornerR * 0.2f,         -1f, -1f)
    )

    corners.forEach { (ax, ay, sx, sy) ->
        // Inward-pointing flourish curl that hooks back on itself
        val p0 = Offset(ax + sx * curlReach * 1.0f, ay + sy * curlReach * 0.05f)
        val p1c1 = Offset(ax + sx * curlReach * 0.5f, ay + sy * curlReach * 0.05f)
        val p1c2 = Offset(ax + sx * curlReach * 0.05f, ay + sy * curlReach * 0.4f)
        val p1 = Offset(ax + sx * curlReach * 0.05f, ay + sy * curlReach * 1.0f)
        drawPath(
            path = Path().apply {
                moveTo(p0.x, p0.y)
                cubicTo(p1c1.x, p1c1.y, p1c2.x, p1c2.y, p1.x, p1.y)
            },
            color = accentColor,
            style = Stroke(width = 0.85f.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Inner mirror curl for richer scrollwork
        val q0 = Offset(ax + sx * curlReach * 0.55f, ay + sy * curlReach * 0.55f)
        val q1c1 = Offset(ax + sx * curlReach * 0.30f, ay + sy * curlReach * 0.55f)
        val q1c2 = Offset(ax + sx * curlReach * 0.30f, ay + sy * curlReach * 0.80f)
        val q1 = Offset(ax + sx * curlReach * 0.55f, ay + sy * curlReach * 0.80f)
        drawPath(
            path = Path().apply {
                moveTo(q0.x, q0.y)
                cubicTo(q1c1.x, q1c1.y, q1c2.x, q1c2.y, q1.x, q1.y)
            },
            color = accentColor.copy(alpha = 0.55f),
            style = Stroke(width = 0.7f.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Tiny gold dot at the curl meeting-point
        drawCircle(
            color = accentColor,
            radius = dotR,
            center = Offset(ax + sx * curlReach * 0.1f, ay + sy * curlReach * 0.1f)
        )
    }

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

/**
 * Draw delicate olive branches in each corner of the parchment.
 * Small lance-shaped leaves alternate along a thin curved stem, subtle and airy.
 */
private fun DrawScope.drawVineBorder(color: Color) {
    val w = size.width
    val h = size.height
    val pad = 4f.dp.toPx()
    val armLenH = 44f.dp.toPx()   // horizontal reach — unchanged
    val armLenV = 60f.dp.toPx()   // vertical reach — extended per user request

    // Muted, translucent olive palette — intentionally subtle
    val leafDeep      = Color(0xFF4F6A2A).copy(alpha = 0.72f)
    val leafMid       = Color(0xFF7A9A45).copy(alpha = 0.65f)
    val leafSoft      = Color(0xFFA8C26F).copy(alpha = 0.55f)
    val leafHighlight = Color(0xFFD3E2A6).copy(alpha = 0.35f)
    val berryDark     = Color(0xFF5C7C2E).copy(alpha = 0.65f)
    val berryLight    = Color(0xFFAFC774).copy(alpha = 0.70f)
    val veinColor     = Color(0xFF3D5320).copy(alpha = 0.30f)
    val stemColor     = Color(0xFF6B5A33).copy(alpha = 0.50f)
    val stemSoft      = stemColor.copy(alpha = 0.28f)

    data class Corner(val ox: Float, val oy: Float, val sx: Float, val sy: Float)

    /**
     * Lance-shaped olive leaf with watercolor shading and a midrib vein.
     * The leaf grows from `base` toward `tip`, with optional sideways curve.
     */
    fun drawOliveLeaf(
        base: Offset,
        tip: Offset,
        widthRatio: Float = 0.24f,
        curveBias: Float = 0f
    ) {
        val dx = tip.x - base.x
        val dy = tip.y - base.y
        val len = kotlin.math.sqrt(dx * dx + dy * dy).coerceAtLeast(0.0001f)
        // Unit normal (perpendicular to base→tip)
        val nx = -dy / len
        val ny = dx / len
        val half = len * widthRatio

        // Slightly bend the leaf's mid-spine for a more organic look
        val mid = Offset(
            (base.x + tip.x) / 2f + nx * half * curveBias * 0.4f,
            (base.y + tip.y) / 2f + ny * half * curveBias * 0.4f
        )
        val ctrlA = Offset(mid.x + nx * half, mid.y + ny * half)
        val ctrlB = Offset(mid.x - nx * half, mid.y - ny * half)

        // Body fill — soft → mid → deep gradient
        drawPath(
            path = Path().apply {
                moveTo(base.x, base.y)
                quadraticTo(ctrlA.x, ctrlA.y, tip.x, tip.y)
                quadraticTo(ctrlB.x, ctrlB.y, base.x, base.y)
                close()
            },
            brush = Brush.linearGradient(
                colors = listOf(leafSoft, leafMid, leafDeep),
                start = base, end = tip
            )
        )

        // Soft highlight stripe along one half of the leaf
        val highlightCtrl = Offset(
            ctrlA.x - nx * half * 0.45f,
            ctrlA.y - ny * half * 0.45f
        )
        drawPath(
            path = Path().apply {
                moveTo(base.x, base.y)
                quadraticTo(highlightCtrl.x, highlightCtrl.y, tip.x, tip.y)
                quadraticTo(
                    (base.x + tip.x) / 2f, (base.y + tip.y) / 2f,
                    base.x, base.y
                )
                close()
            },
            brush = Brush.linearGradient(
                colors = listOf(
                    leafHighlight.copy(alpha = 0.55f),
                    leafHighlight.copy(alpha = 0.15f),
                    Color.Transparent
                ),
                start = base, end = tip
            )
        )

        // Midrib (center vein)
        drawLine(
            color = veinColor,
            start = base,
            end = Offset(
                tip.x - dx * 0.06f,
                tip.y - dy * 0.06f
            ),
            strokeWidth = 0.55f.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Outline — darker rim for definition
        drawPath(
            path = Path().apply {
                moveTo(base.x, base.y)
                quadraticTo(ctrlA.x, ctrlA.y, tip.x, tip.y)
                quadraticTo(ctrlB.x, ctrlB.y, base.x, base.y)
                close()
            },
            color = leafDeep.copy(alpha = 0.45f),
            style = Stroke(width = 0.45f.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }

    fun drawOliveBerry(center: Offset, radius: Float) {
        // Soft shadow under berry
        drawCircle(
            color = leafDeep.copy(alpha = 0.25f),
            radius = radius * 1.05f,
            center = Offset(center.x + radius * 0.15f, center.y + radius * 0.2f)
        )
        // Main berry with radial highlight
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(berryLight, leafMid, berryDark),
                center = Offset(center.x - radius * 0.35f, center.y - radius * 0.4f),
                radius = radius * 1.7f
            ),
            radius = radius,
            center = center
        )
        // Specular highlight
        drawCircle(
            color = leafHighlight.copy(alpha = 0.85f),
            radius = radius * 0.32f,
            center = Offset(center.x - radius * 0.4f, center.y - radius * 0.45f)
        )
    }

    /** Tangent (unit vector) of a cubic bezier at parameter t. */
    fun bezTangent(
        p0: Offset, p1: Offset, p2: Offset, p3: Offset, t: Float
    ): Offset {
        val mt = 1f - t
        val dx = 3f * mt * mt * (p1.x - p0.x) +
                6f * mt * t * (p2.x - p1.x) +
                3f * t * t * (p3.x - p2.x)
        val dy = 3f * mt * mt * (p1.y - p0.y) +
                6f * mt * t * (p2.y - p1.y) +
                3f * t * t * (p3.y - p2.y)
        val mag = kotlin.math.sqrt(dx * dx + dy * dy).coerceAtLeast(0.0001f)
        return Offset(dx / mag, dy / mag)
    }

    // Leaf placement spec along the stem (t in 0..1)
    data class LeafSpec(
        val t: Float,
        val side: Float,      // +1 outer, -1 inner (relative to corner)
        val lengthDp: Float,
        val angleDeg: Float,  // angle off the stem tangent
        val curve: Float,     // sideways curvature of the leaf body
        val widthRatio: Float = 0.24f
    )

    // 11 small leaves alternating per branch — closely spaced like a wreath
    val leaves = listOf(
        LeafSpec(0.02f, +1f, 5.5f, 50f, +0.20f, widthRatio = 0.21f),
        LeafSpec(0.11f, -1f, 5.0f, 46f, -0.18f, widthRatio = 0.21f),
        LeafSpec(0.20f, +1f, 6.5f, 50f, +0.22f, widthRatio = 0.20f),
        LeafSpec(0.30f, -1f, 5.5f, 46f, -0.18f, widthRatio = 0.20f),
        LeafSpec(0.40f, +1f, 7.0f, 52f, +0.24f, widthRatio = 0.20f),
        LeafSpec(0.50f, -1f, 6.0f, 46f, -0.20f, widthRatio = 0.20f),
        LeafSpec(0.60f, +1f, 7.0f, 52f, +0.22f, widthRatio = 0.20f),
        LeafSpec(0.69f, -1f, 5.5f, 46f, -0.18f, widthRatio = 0.20f),
        LeafSpec(0.78f, +1f, 6.5f, 50f, +0.22f, widthRatio = 0.20f),
        LeafSpec(0.88f, -1f, 5.0f, 44f, -0.18f, widthRatio = 0.20f),
        LeafSpec(0.96f, +1f, 5.0f, 48f, +0.20f, widthRatio = 0.20f)
    )

    listOf(
        Corner(pad,     pad,      1f,  1f),
        Corner(w - pad, pad,     -1f,  1f),
        Corner(pad,     h - pad,  1f, -1f),
        Corner(w - pad, h - pad, -1f, -1f)
    ).forEach { (ox, oy, sx, sy) ->
        val p0 = Offset(ox, oy + sy * armLenV)
        val p1 = Offset(ox, oy + sy * armLenV * 0.3f)
        val p2 = Offset(ox + sx * armLenH * 0.3f, oy)
        val p3 = Offset(ox + sx * armLenH, oy)

        // Outward direction (toward the dialog corner)
        val outwardX = -sx
        val outwardY = -sy

        // Stem — hair-thin, softly faded
        drawPath(
            path = Path().apply {
                moveTo(p0.x, p0.y)
                cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
            },
            color = stemSoft,
            style = Stroke(width = 1.6f.dp.toPx(), cap = StrokeCap.Round)
        )
        drawPath(
            path = Path().apply {
                moveTo(p0.x, p0.y)
                cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y)
            },
            color = stemColor,
            style = Stroke(width = 0.7f.dp.toPx(), cap = StrokeCap.Round)
        )

        // Leaves
        leaves.forEach { spec ->
            val bx = cubicBezierX(p0.x, p1.x, p2.x, p3.x, spec.t)
            val by = cubicBezierY(p0.y, p1.y, p2.y, p3.y, spec.t)
            val tangent = bezTangent(p0, p1, p2, p3, spec.t)

            // Normal to tangent, oriented to the OUTWARD side of the dialog corner
            val nxRaw = -tangent.y
            val nyRaw = tangent.x
            val outSign = if (nxRaw * outwardX + nyRaw * outwardY > 0) 1f else -1f
            val outNx = nxRaw * outSign
            val outNy = nyRaw * outSign

            // Side direction for this leaf (outer or inner)
            val sideX = outNx * spec.side
            val sideY = outNy * spec.side

            // Direction = stem tangent rotated toward chosen side by `angleDeg`
            val rad = (spec.angleDeg * PI / 180.0).toFloat()
            val cosA = cos(rad)
            val sinA = sin(rad)
            val dirX = tangent.x * cosA + sideX * sinA
            val dirY = tangent.y * cosA + sideY * sinA

            val leafLen = spec.lengthDp.dp.toPx()

            // Leaves attach right at the stem
            val base = Offset(
                bx + sideX * 0.3f.dp.toPx(),
                by + sideY * 0.3f.dp.toPx()
            )
            val tip = Offset(base.x + dirX * leafLen, base.y + dirY * leafLen)

            drawOliveLeaf(base, tip, widthRatio = spec.widthRatio, curveBias = spec.curve)
        }

        // A small cluster of olives near the middle, on the outer side
        val berryT = 0.40f
        val bxC = cubicBezierX(p0.x, p1.x, p2.x, p3.x, berryT)
        val byC = cubicBezierY(p0.y, p1.y, p2.y, p3.y, berryT)
        val tan = bezTangent(p0, p1, p2, p3, berryT)
        val nxRaw = -tan.y
        val nyRaw = tan.x
        val outSign = if (nxRaw * outwardX + nyRaw * outwardY > 0) 1f else -1f
        val nOutX = nxRaw * outSign
        val nOutY = nyRaw * outSign

        // Tiny stem connecting branch to berries
        val berryStemEnd = Offset(
            bxC + nOutX * 2.5f.dp.toPx(),
            byC + nOutY * 2.5f.dp.toPx()
        )
        drawLine(
            color = stemColor,
            start = Offset(bxC, byC),
            end = berryStemEnd,
            strokeWidth = 0.5f.dp.toPx(),
            cap = StrokeCap.Round
        )

        val berryR = 1.3f.dp.toPx()
        drawOliveBerry(
            Offset(berryStemEnd.x + tan.x * 0.4f.dp.toPx(),
                   berryStemEnd.y + tan.y * 0.4f.dp.toPx()),
            berryR
        )
        drawOliveBerry(
            Offset(berryStemEnd.x + nOutX * 2.2f.dp.toPx() - tan.x * 0.8f.dp.toPx(),
                   berryStemEnd.y + nOutY * 2.2f.dp.toPx() - tan.y * 0.8f.dp.toPx()),
            berryR * 0.85f
        )
    }

}

// Simple cubic bezier point evaluation helpers
private fun cubicBezierX(p0x: Float, p1x: Float, p2x: Float, p3x: Float, t: Float): Float {
    val mt = 1f - t
    return mt * mt * mt * p0x + 3f * mt * mt * t * p1x + 3f * mt * t * t * p2x + t * t * t * p3x
}

private fun cubicBezierY(p0y: Float, p1y: Float, p2y: Float, p3y: Float, t: Float): Float {
    val mt = 1f - t
    return mt * mt * mt * p0y + 3f * mt * mt * t * p1y + 3f * mt * t * t * p2y + t * t * t * p3y
}
