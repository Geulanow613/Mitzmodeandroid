package com.beardytop.beatzaddik.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import beatzaddik.shared.generated.resources.Res
import beatzaddik.shared.generated.resources.app_icon
import com.beardytop.beatzaddik.domain.MitzModeFeatures
import com.beardytop.beatzaddik.domain.MitzvahLevels
import com.beardytop.beatzaddik.ui.components.AppText
import com.beardytop.beatzaddik.ui.components.MitzvahCountLabel
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import org.jetbrains.compose.resources.painterResource

/**
 * Full Mitz Mode certificate of achievement — same layout as the original
 * [MitzvahLevelDialog], shown as the Status tab.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatusScreen(
    mitzvotCount: Int,
    onReplayFinalReward: () -> Unit = {},
) {
    val currentLevel = MitzvahLevels.forCount(mitzvotCount)
    val numStars = MitzvahLevels.starCount(currentLevel)
    val isUltimateMitzMode = currentLevel == MitzvahLevels.MITZ_MODE

    val ultimateNeon = remember {
        listOf(
            Color(0xFF00F0FF),
            Color(0xFF8B5CFF),
            Color(0xFFFF3D9A),
            Color(0xFFFFEA71),
            Color(0xFF00FFC8),
        )
    }
    val ultimateFrameBrush = remember {
        Brush.linearGradient(
            colors = ultimateNeon,
            start = Offset.Zero,
            end = Offset(520f, 520f),
        )
    }
    val ultimatePillFill = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0x4000E5FF),
                Color(0x359633FF),
                Color(0x38FF4D9A),
            ),
        )
    }
    val goldBorder = TzaddikColors.GoldBorder
    val goldBright = TzaddikColors.GoldBright
    val textPrimary = TzaddikColors.TextBrown
    val textMuted = TzaddikColors.TextMuted
    val flourishColor = if (isUltimateMitzMode) {
        Color(0xFF66FFF8).copy(alpha = 0.9f)
    } else {
        goldBorder.copy(alpha = 0.55f)
    }
    val certificateFont = FontFamily.Serif

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(22.dp), ambientColor = goldBorder)
                .clip(RoundedCornerShape(22.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to TzaddikColors.ParchTop,
                            0.4f to TzaddikColors.ParchMid,
                            1f to TzaddikColors.ParchBase,
                        ),
                    ),
                )
                .border(1.4.dp, goldBorder.copy(alpha = 0.55f), RoundedCornerShape(22.dp))
                .padding(horizontal = 10.dp, vertical = 10.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .then(
                            if (isUltimateMitzMode) {
                                Modifier
                                    .border(
                                        BorderStroke(2.2.dp, ultimateFrameBrush),
                                        RoundedCornerShape(16.dp),
                                    )
                                    .padding(6.dp)
                                    .border(
                                        BorderStroke(1.2.dp, ultimateFrameBrush),
                                        RoundedCornerShape(12.dp),
                                    )
                            } else {
                                Modifier
                                    .border(
                                        width = 1.4.dp,
                                        color = goldBorder.copy(alpha = 0.70f),
                                        shape = RoundedCornerShape(16.dp),
                                    )
                                    .padding(6.dp)
                                    .border(
                                        width = 0.8.dp,
                                        color = goldBorder.copy(alpha = 0.38f),
                                        shape = RoundedCornerShape(12.dp),
                                    )
                            },
                        ),
                )

                Text(
                    text = "❦",
                    fontSize = 14.sp,
                    color = flourishColor,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 10.dp, top = 8.dp),
                )
                Text(
                    text = "❦",
                    fontSize = 14.sp,
                    color = flourishColor,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 10.dp, top = 8.dp),
                )
                Text(
                    text = "❦",
                    fontSize = 14.sp,
                    color = flourishColor,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 10.dp, bottom = 8.dp),
                )
                Text(
                    text = "❦",
                    fontSize = 14.sp,
                    color = flourishColor,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 10.dp, bottom = 8.dp),
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "✡",
                        fontSize = 36.sp,
                        color = if (isUltimateMitzMode) Color(0xFFFFEA8A) else goldBorder,
                        textAlign = TextAlign.Center,
                    )

                    if (isUltimateMitzMode) {
                        AppText(
                            text = "Certificate of\nAchievement",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = certificateFont,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                lineHeight = 36.sp,
                                letterSpacing = 1.sp,
                                brush = ultimateFrameBrush,
                            ),
                            color = Color.Unspecified,
                            textAlign = TextAlign.Center,
                            enableTerms = false,
                        )
                    } else {
                        AppText(
                            text = "Certificate of\nAchievement",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = certificateFont,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                lineHeight = 36.sp,
                                letterSpacing = 1.sp,
                            ),
                            color = goldBorder,
                            textAlign = TextAlign.Center,
                            enableTerms = false,
                        )
                    }

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
                                            Color.Transparent,
                                        ),
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            goldBorder,
                                            Color.Transparent,
                                        ),
                                    )
                                },
                            ),
                    )

                    AppText(
                        text = "This document certifies that\nI have completed",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp,
                        ),
                        color = textPrimary,
                        enableTerms = false,
                    )

                    if (isUltimateMitzMode) {
                        MitzvahCountLabel(
                            count = mitzvotCount,
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
                                    Color(0xFFFFF4B8),
                                ),
                                start = Offset(0f, 40f),
                                end = Offset(600f, 120f),
                            ),
                        )
                    } else {
                        MitzvahCountLabel(
                            count = mitzvotCount,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            ),
                            color = goldBorder,
                        )
                    }

                    AppText(
                        text = "Current Level",
                        style = MaterialTheme.typography.titleSmall.copy(
                            letterSpacing = 1.5.sp,
                        ),
                        color = if (isUltimateMitzMode) {
                            Color(0xFF2A1A4A).copy(alpha = 0.75f)
                        } else {
                            textMuted
                        },
                        enableTerms = false,
                    )

                    Box(
                        modifier = if (isUltimateMitzMode) {
                            Modifier
                                .border(
                                    BorderStroke(2.dp, ultimateFrameBrush),
                                    RoundedCornerShape(50),
                                )
                                .background(ultimatePillFill, RoundedCornerShape(50))
                                .padding(horizontal = 22.dp, vertical = 10.dp)
                        } else {
                            Modifier
                                .border(
                                    width = 1.5.dp,
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(goldBorder, goldBright, goldBorder),
                                    ),
                                    shape = RoundedCornerShape(50),
                                )
                                .background(
                                    color = goldBorder.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(50),
                                )
                                .padding(horizontal = 22.dp, vertical = 10.dp)
                        },
                    ) {
                        if (isUltimateMitzMode) {
                            AppText(
                                text = currentLevel,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = certificateFont,
                                    fontWeight = FontWeight.Bold,
                                    brush = ultimateFrameBrush,
                                ),
                                color = Color.Unspecified,
                                enableTerms = false,
                            )
                        } else {
                            AppText(
                                text = currentLevel,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = certificateFont,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                color = goldBorder,
                                enableTerms = false,
                            )
                        }
                    }

                    if (isUltimateMitzMode) {
                        AppText(
                            text = "Legendary tier — thank you\nfor lighting up the world.\nKeep Going!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp,
                                letterSpacing = 0.3.sp,
                            ),
                            color = Color(0xFF4A2A6E).copy(alpha = 0.85f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            enableTerms = false,
                        )
                    }

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
                                            Color.Transparent,
                                        ),
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            goldBorder,
                                            Color.Transparent,
                                        ),
                                    )
                                },
                            ),
                    )

                    if (isUltimateMitzMode && MitzModeFeatures.finalRewardEnabled) {
                        Image(
                            painter = painterResource(Res.drawable.app_icon),
                            contentDescription = "Replay final reward",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(76.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    BorderStroke(2.dp, ultimateFrameBrush),
                                    RoundedCornerShape(16.dp),
                                )
                                .clickable(onClick = onReplayFinalReward),
                        )
                    } else if (numStars > 0) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                            maxItemsInEachRow = 4,
                        ) {
                            repeat(numStars) {
                                Text(
                                    text = "✡",
                                    fontSize = 28.sp,
                                    color = goldBorder,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
        }
    }
}
