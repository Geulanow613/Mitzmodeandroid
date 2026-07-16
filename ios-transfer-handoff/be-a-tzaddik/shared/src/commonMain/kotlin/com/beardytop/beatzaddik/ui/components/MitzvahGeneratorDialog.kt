package com.beardytop.beatzaddik.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import beatzaddik.shared.generated.resources.Res
import beatzaddik.shared.generated.resources.ic_torah_scroll
import com.beardytop.beatzaddik.domain.GeneratorMitzvah
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val ParchmentTop = Color(0xFFFFFBEE)
private val ParchmentMid = Color(0xFFFFF8E1)
private val ParchmentBase = Color(0xFFFFF0CC)
private val GoldBorder = Color(0xFFB8860B)
private val NavyBlue = Color(0xFF2B5BA8)
private val TextBrown = Color(0xFF1A0A00)
private val CardShape = RoundedCornerShape(20.dp)

/**
 * Original Mitz Mode generator card: Torah scroll crest, parchment card, Accept/Next,
 * and holy-light flash on Accept. No dialog title — “Mitzvah Me” is only the nav button label.
 */
@Composable
fun MitzvahGeneratorDialog(
    mitzvah: GeneratorMitzvah,
    onAccept: () -> Unit,
    onNext: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var accepted by remember(mitzvah.id) { mutableStateOf(false) }
    var acceptLocked by remember(mitzvah.id) { mutableStateOf(false) }
    var textSize by remember { mutableStateOf(16.sp) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val flashAlpha = remember { Animatable(0f) }
    val sweepProgress = remember { Animatable(0f) }
    var flashVisible by remember { mutableStateOf(false) }
    var acceptFlashVariant by remember { mutableIntStateOf(0) }
    var displayedFlashVariant by remember { mutableIntStateOf(0) }

    LaunchedEffect(mitzvah.id) {
        scrollState.scrollTo(0)
        acceptLocked = false
        accepted = false
        sweepProgress.snapTo(0f)
    }

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
            dismissOnClickOutside = false,
        ),
    ) {
        BoxWithConstraints(modifier.fillMaxSize()) {
            val screenWidthDp = maxWidth.value
            val screenHeightDp = maxHeight.value
            val buttonTextSize = when {
                screenWidthDp < 360f -> 13.sp
                screenWidthDp < 400f -> 15.sp
                else -> 16.sp
            }

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable(
                        enabled = allowOutsideDismiss,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { onDismiss() },
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                val scrollIconSize = 108.dp
                val cardTopPadding = scrollIconSize * 0.68f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = cardTopPadding)
                            .wrapContentHeight()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                            ) {},
                        shape = CardShape,
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.5.dp, GoldBorder.copy(alpha = 0.6f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    ) {
                        Box(Modifier.fillMaxWidth()) {
                            Canvas(Modifier.matchParentSize()) {
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colorStops = arrayOf(
                                            0f to ParchmentTop,
                                            0.3f to ParchmentMid,
                                            1f to ParchmentBase,
                                        ),
                                    ),
                                )
                                drawRect(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFEDD9A3).copy(alpha = 0.10f),
                                            Color.Transparent,
                                        ),
                                        center = Offset(size.width * 0.5f, size.height * 0.5f),
                                        radius = maxOf(size.width, size.height) * 0.6f,
                                    ),
                                )
                                drawRect(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFB8860B).copy(alpha = 0.06f),
                                            Color.Transparent,
                                        ),
                                        center = Offset(size.width * 0.5f, size.height * 1.05f),
                                        radius = maxOf(size.width, size.height) * 0.7f,
                                    ),
                                )
                                drawFiligreeFrame(GoldBorder)
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(Modifier.height(scrollIconSize * 0.36f))

                                Canvas(Modifier.size(22.dp)) {
                                    drawStarOfDavid(
                                        center = Offset(size.width / 2f, size.height / 2f),
                                        radius = size.minDimension / 2f,
                                        color = GoldBorder.copy(alpha = 0.7f),
                                        strokeWidth = 1.4f.dp.toPx(),
                                    )
                                }

                                Spacer(Modifier.height(6.dp))

                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Row(
                                        modifier = Modifier.padding(start = 2.dp),
                                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                                    ) {
                                        IconButton(
                                            onClick = {
                                                textSize = (textSize.value - 2f).coerceAtLeast(12f).sp
                                            },
                                            modifier = Modifier.size(30.dp),
                                        ) {
                                            Text(
                                                "a",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = TextBrown.copy(0.55f),
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                textSize = (textSize.value + 2f).coerceAtMost(22f).sp
                                            },
                                            modifier = Modifier.size(30.dp),
                                        ) {
                                            Text(
                                                "A",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = TextBrown.copy(0.55f),
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = onDismiss,
                                        modifier = Modifier
                                            .padding(end = 2.dp)
                                            .size(30.dp),
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            "Close",
                                            tint = TextBrown.copy(0.5f),
                                            modifier = Modifier.size(16.dp),
                                        )
                                    }
                                }

                                Spacer(Modifier.height(6.dp))
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                    color = GoldBorder.copy(alpha = 0.35f),
                                    thickness = 0.8.dp,
                                )
                                Spacer(Modifier.height(8.dp))

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = (screenHeightDp * 0.42f).dp)
                                        .verticalScroll(scrollState)
                                        .padding(horizontal = 28.dp, vertical = 6.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                ) {
                                    // Body: glossary underlines only. Resource URLs stay in the rows below.
                                    AppText(
                                        text = mitzvah.text,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = textSize,
                                            lineHeight = (textSize.value * 1.55f).sp,
                                        ),
                                        color = TextBrown,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth(),
                                    )

                                    mitzvah.links.forEach { link ->
                                        GeneratorLinkText(
                                            displayText = link.displayText,
                                            url = link.url,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = NavyBlue,
                                                fontSize = (textSize.value + 1.5f).sp,
                                                lineHeight = (textSize.value * 1.5f).sp,
                                                textDecoration = TextDecoration.Underline,
                                            ),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 4.dp),
                                        )
                                    }
                                    Spacer(Modifier.height(4.dp))
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                                    color = GoldBorder.copy(alpha = 0.35f),
                                    thickness = 0.8.dp,
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 22.dp, vertical = 14.dp),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                ) {
                                    HeirloomButton(
                                        text = if (accepted) "Accepted ✓" else "Accept",
                                        textSize = buttonTextSize,
                                        enabled = !accepted,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            if (!acceptLocked) {
                                                acceptLocked = true
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
                                                            tween(120, easing = LinearEasing),
                                                        )
                                                        delay(140)
                                                        flashAlpha.animateTo(
                                                            0f,
                                                            tween(780, easing = LinearOutSlowInEasing),
                                                        )
                                                    }
                                                    val sweep = async {
                                                        sweepProgress.animateTo(
                                                            1f,
                                                            tween(520, easing = FastOutSlowInEasing),
                                                        )
                                                    }
                                                    fade.await()
                                                    sweep.await()
                                                    flashVisible = false
                                                    acceptFlashVariant = (variant + 1) % 4
                                                }
                                            }
                                        },
                                    )

                                    HeirloomButton(
                                        text = "Next",
                                        textSize = buttonTextSize,
                                        modifier = Modifier.weight(1f),
                                        onClick = onNext,
                                    )
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(scrollIconSize * 1.6f, scrollIconSize)
                            .align(Alignment.TopCenter),
                        contentAlignment = Alignment.Center,
                    ) {
                        Canvas(Modifier.fillMaxSize()) {
                            val cx = size.width / 2f
                            val cy = size.height / 2f
                            val maxR = maxOf(size.width, size.height) * 0.92f

                            for (i in 0 until 8) {
                                val angleDeg = i * 45f
                                val angle = angleDeg * (PI.toFloat() / 180f)
                                val upFactor = ((1f - sin(angle)) / 2f).coerceIn(0f, 1f)
                                val maxAlpha = if (i % 2 == 0) 0.36f else 0.22f
                                val beamAlpha = maxAlpha * upFactor
                                if (beamAlpha < 0.01f) continue

                                val halfSpread = 0.06f
                                val path = Path().apply {
                                    moveTo(cx, cy)
                                    lineTo(
                                        cx + cos(angle - halfSpread) * maxR,
                                        cy + sin(angle - halfSpread) * maxR,
                                    )
                                    lineTo(
                                        cx + cos(angle + halfSpread) * maxR,
                                        cy + sin(angle + halfSpread) * maxR,
                                    )
                                    close()
                                }
                                drawPath(
                                    path = path,
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFFFFEFC0).copy(alpha = beamAlpha),
                                            Color(0xFFFFD56B).copy(alpha = beamAlpha * 0.55f),
                                            Color.Transparent,
                                        ),
                                        center = Offset(cx, cy),
                                        radius = maxR,
                                    ),
                                )
                            }

                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFFFFBE6).copy(alpha = 0.78f),
                                        Color(0xFFFFE082).copy(alpha = 0.55f),
                                        Color(0xFFFFD56B).copy(alpha = 0.22f),
                                        Color.Transparent,
                                    ),
                                    center = Offset(cx, cy),
                                    radius = size.width * 0.40f,
                                ),
                                radius = size.width * 0.40f,
                                center = Offset(cx, cy),
                            )
                        }

                        Image(
                            painter = painterResource(Res.drawable.ic_torah_scroll),
                            contentDescription = "Torah scroll",
                            modifier = Modifier
                                .width(scrollIconSize * 1.4f)
                                .height(scrollIconSize * 0.88f),
                        )
                    }
                }
            }

            HolyLightFlashOverlay(
                visible = flashVisible,
                alpha = flashAlpha.value,
                sweepT = sweepProgress.value,
                variant = displayedFlashVariant,
            )
        }
    }
}

@Composable
private fun GeneratorLinkText(
    displayText: String,
    url: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    val openAppImage = LocalOpenAppImage.current
    val openInAppBrowser = LocalOpenInAppBrowser.current
    AppText(
        text = displayText,
        style = style,
        textAlign = TextAlign.Center,
        enableTerms = false,
        modifier = modifier.clickable {
            openChecklistUri(url, uriHandler, openAppImage, openInAppBrowser)
        },
    )
}

@Composable
private fun HeirloomButton(
    text: String,
    textSize: TextUnit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val deepNavy = Color(0xFF12305A)
    val midNavy = Color(0xFF1F4685)
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
                    Brush.verticalGradient(colors = listOf(brightNavy, midNavy, deepNavy))
                } else {
                    Brush.verticalGradient(
                        colors = listOf(disabledTone.copy(alpha = 0.85f), disabledTone),
                    )
                },
            )
            .border(width = 1.4.dp, color = outerGold, shape = RoundedCornerShape(50))
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(2.5.dp)
                .border(width = 0.6.dp, color = innerGold, shape = RoundedCornerShape(50)),
        )

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
                            Color.Transparent,
                        ),
                    ),
                    shape = RoundedCornerShape(50),
                )
                .align(Alignment.TopCenter),
        )

        AppText(
            text = text,
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = textSize,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.45f),
                    offset = Offset(0f, 1.2f),
                    blurRadius = 3f,
                ),
            ),
            color = Color(0xFFFFF6D8),
            enableTerms = false,
        )
    }
}

private fun DrawScope.drawStarOfDavid(
    center: Offset,
    radius: Float,
    color: Color,
    strokeWidth: Float,
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
        style = style,
    )
    drawPath(
        Path().apply {
            moveTo(center.x, center.y + radius)
            lineTo(center.x + radius * cos30, center.y - radius * sin30)
            lineTo(center.x - radius * cos30, center.y - radius * sin30)
            close()
        },
        color = color,
        style = style,
    )
}

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

    drawRoundRect(
        color = outerColor,
        topLeft = Offset(inset, inset),
        size = Size(w - 2 * inset, h - 2 * inset),
        cornerRadius = CornerRadius(cornerR, cornerR),
        style = Stroke(width = outerStroke, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
    drawRoundRect(
        color = innerColor,
        topLeft = Offset(inset + gapBetweenLines, inset + gapBetweenLines),
        size = Size(
            w - 2 * (inset + gapBetweenLines),
            h - 2 * (inset + gapBetweenLines),
        ),
        cornerRadius = CornerRadius(
            cornerR - gapBetweenLines * 0.6f,
            cornerR - gapBetweenLines * 0.6f,
        ),
        style = Stroke(width = innerStroke, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )

    val edgeAccentLen = 14f.dp.toPx()
    val edgeAccentColor = accentColor.copy(alpha = 0.45f)
    val midX = w / 2f
    val midY = h / 2f
    val edgeStroke = 0.5f.dp.toPx()
    drawLine(
        edgeAccentColor,
        Offset(midX - edgeAccentLen, inset + gapBetweenLines * 1.6f),
        Offset(midX + edgeAccentLen, inset + gapBetweenLines * 1.6f),
        edgeStroke,
        StrokeCap.Round,
    )
    drawLine(
        edgeAccentColor,
        Offset(midX - edgeAccentLen, h - inset - gapBetweenLines * 1.6f),
        Offset(midX + edgeAccentLen, h - inset - gapBetweenLines * 1.6f),
        edgeStroke,
        StrokeCap.Round,
    )
    drawLine(
        edgeAccentColor,
        Offset(inset + gapBetweenLines * 1.6f, midY - edgeAccentLen),
        Offset(inset + gapBetweenLines * 1.6f, midY + edgeAccentLen),
        edgeStroke,
        StrokeCap.Round,
    )
    drawLine(
        edgeAccentColor,
        Offset(w - inset - gapBetweenLines * 1.6f, midY - edgeAccentLen),
        Offset(w - inset - gapBetweenLines * 1.6f, midY + edgeAccentLen),
        edgeStroke,
        StrokeCap.Round,
    )
}
