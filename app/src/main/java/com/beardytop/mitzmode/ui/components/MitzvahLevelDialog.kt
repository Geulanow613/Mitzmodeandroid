package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.BitmapFactory
import com.beardytop.mitzmode.util.MitzvahLevels
import com.beardytop.mitzmode.ui.theme.Cinzel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MitzvahLevelDialog(
    count: Int,
    currentLevel: String,
    onDismiss: () -> Unit,
    /** At 1800+ tier: tap app icon to replay `finalreward.mp4`. */
    onRequestFinalRewardVideo: () -> Unit = {}
) {
    val numStars = MitzvahLevels.starCount(currentLevel)

    val isUltimateMitzMode = currentLevel == MitzvahLevels.MITZ_MODE
    val ultimateNeon = remember {
        listOf(
            Color(0xFF00F0FF),
            Color(0xFF8B5CFF),
            Color(0xFFFF3D9A),
            Color(0xFFFFEA71),
            Color(0xFF00FFC8)
        )
    }
    val ultimateFrameBrush = remember {
        Brush.linearGradient(
            colors = ultimateNeon,
            start = Offset.Zero,
            end = Offset(520f, 520f)
        )
    }
    val ultimatePillFill = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x4000E5FF),
                Color(0x359633FF),
                Color(0x38FF4D9A)
            )
        )
    }
    val flourishColor = if (isUltimateMitzMode) Color(0xFF66FFF8).copy(alpha = 0.9f)
    else DialogGoldBorder.copy(alpha = 0.55f)

    val context = LocalContext.current
    val appIconBitmap = remember {
        try {
            context.assets.open("app_icon.png").use { stream ->
                BitmapFactory.decodeStream(stream)?.asImageBitmap()
            }
        } catch (_: Exception) {
            null
        }
    }

    ParchmentDialog(
        onDismiss = onDismiss,
        title = null,
        showCloseIcon = true,
        confirmButton = { GoldButton(onClick = onDismiss, text = "Close") }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            // Certificate frame (double border + corner marks)
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .then(
                        if (isUltimateMitzMode) {
                            Modifier
                                .border(
                                    BorderStroke(2.2.dp, ultimateFrameBrush),
                                    RoundedCornerShape(16.dp)
                                )
                                .padding(6.dp)
                                .border(
                                    BorderStroke(1.2.dp, ultimateFrameBrush),
                                    RoundedCornerShape(12.dp)
                                )
                        } else {
                            Modifier
                                .border(
                                    width = 1.4.dp,
                                    color = DialogGoldBorder.copy(alpha = 0.70f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(6.dp)
                                .border(
                                    width = 0.8.dp,
                                    color = DialogGoldBorder.copy(alpha = 0.38f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        }
                    )
            )

            // Top-left corner flourish
            Text(
                text = "❦",
                fontSize = 14.sp,
                color = flourishColor,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp, top = 8.dp)
            )
            // Top-right corner flourish
            Text(
                text = "❦",
                fontSize = 14.sp,
                color = flourishColor,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp, top = 8.dp)
            )
            // Bottom-left corner flourish
            Text(
                text = "❦",
                fontSize = 14.sp,
                color = flourishColor,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 10.dp, bottom = 8.dp)
            )
            // Bottom-right corner flourish
            Text(
                text = "❦",
                fontSize = 14.sp,
                color = flourishColor,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
            // Crown emblem (single Magen David; ultimate tier uses accent color)
            Text(
                text = "✡",
                fontSize = 36.sp,
                color = if (isUltimateMitzMode) Color(0xFFFFEA8A) else DialogGoldBorder,
                textAlign = TextAlign.Center
            )

            // Headline in Cinzel for that elegant certificate feel
            if (isUltimateMitzMode) {
                TranslatableText(
                    text = "Certificate of\nAchievement",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = Cinzel,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp,
                        letterSpacing = 1.sp,
                        brush = ultimateFrameBrush
                    ),
                    color = Color.Unspecified,
                    textAlign = TextAlign.Center
                )
            } else {
                TranslatableText(
                    text = "Certificate of\nAchievement",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = Cinzel,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        lineHeight = 36.sp,
                        letterSpacing = 1.sp
                    ),
                    color = DialogGoldBorder,
                    textAlign = TextAlign.Center
                )
            }

            // Decorative gold rule
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(if (isUltimateMitzMode) 3.dp else 2.dp)
                    .background(
                        if (isUltimateMitzMode) {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF00E5FF),
                                    Color(0xFFB388FF),
                                    Color(0xFFFF6AD5),
                                    Color(0xFFFFEA8A),
                                    Color.Transparent
                                )
                            )
                        } else {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    DialogGoldBorder,
                                    Color.Transparent
                                )
                            )
                        }
                    )
            )

            TranslatableText(
                text = "This document certifies that\nI have completed",
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                ),
                color = DialogTextPrimary
            )

            // The big count
            if (isUltimateMitzMode) {
                MitzvahCountLabel(
                    count = count,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    ),
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF7DF9FF),
                            Color(0xFFE8B4FF),
                            Color(0xFFFF9AD5),
                            Color(0xFFFFF4B8)
                        ),
                        start = Offset(0f, 40f),
                        end = Offset(600f, 120f)
                    ),
                )
            } else {
                MitzvahCountLabel(
                    count = count,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    color = DialogGoldBorder,
                )
            }

            TranslatableText(
                text = "Current Level",
                style = MaterialTheme.typography.titleSmall.copy(
                    letterSpacing = 1.5.sp
                ),
                color = if (isUltimateMitzMode) Color(0xFF2A1A4A).copy(alpha = 0.75f) else DialogTextMuted
            )

            // Level pill
            Box(
                modifier = if (isUltimateMitzMode) {
                    Modifier
                        .border(
                            BorderStroke(2.dp, ultimateFrameBrush),
                            RoundedCornerShape(50)
                        )
                        .background(ultimatePillFill, RoundedCornerShape(50))
                        .padding(horizontal = 22.dp, vertical = 10.dp)
                } else {
                    Modifier
                        .border(
                            width = 1.5.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    DialogGoldBorder,
                                    DialogGoldBright,
                                    DialogGoldBorder
                                )
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .background(
                            color = DialogGoldBorder.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 22.dp, vertical = 10.dp)
                }
            ) {
                if (isUltimateMitzMode) {
                    TranslatableText(
                        text = currentLevel,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = Cinzel,
                            fontWeight = FontWeight.Bold,
                            brush = ultimateFrameBrush
                        ),
                        color = Color.Unspecified,
                        enableHalachicTerms = false,
                    )
                } else {
                    TranslatableText(
                        text = currentLevel,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = Cinzel,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = DialogGoldBorder,
                        enableHalachicTerms = false,
                    )
                }
            }

            if (isUltimateMitzMode) {
                TranslatableText(
                    text = "Legendary tier — thank you\nfor lighting up the world.\nKeep Going!",
                    style = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp,
                        letterSpacing = 0.3.sp
                    ),
                    color = Color(0xFF4A2A6E).copy(alpha = 0.85f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Decorative bottom rule
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(if (isUltimateMitzMode) 3.dp else 2.dp)
                    .background(
                        if (isUltimateMitzMode) {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFFFFEA8A),
                                    Color(0xFFFF6AD5),
                                    Color(0xFF7CF9FF),
                                    Color.Transparent
                                )
                            )
                        } else {
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    DialogGoldBorder,
                                    Color.Transparent
                                )
                            )
                        }
                    )
            )

                if (isUltimateMitzMode) {
                    appIconBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = "Mitz Mode",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(76.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    BorderStroke(2.dp, ultimateFrameBrush),
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable(onClick = onRequestFinalRewardVideo)
                        )
                    }
                } else if (numStars > 0) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                        maxItemsInEachRow = 4
                    ) {
                        repeat(numStars) {
                            Text(
                                text = "✡",
                                fontSize = 28.sp,
                                color = DialogGoldBorder,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
