package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.mitzmode.ui.theme.Cinzel

@Composable
fun MitzvahLevelDialog(
    count: Int,
    currentLevel: String,
    onDismiss: () -> Unit
) {
    val numStars = when (currentLevel) {
        "Secular" -> 0
        "Beginner" -> 1
        "Ba'al Teshuva" -> 2
        "Master Cholent Chef" -> 3
        "Aspiring Kiddush Maker" -> 4
        "Assistant Gabbai" -> 5
        "Guy who hands out candy at shul" -> 6
        "Western Wall Reveler" -> 7
        "Sofer" -> 8
        "Tzaddik" -> 9
        "Living Sefer Torah" -> 10
        "Eliyahu HaNavi" -> 11
        "King David" -> 12
        "Moshiach!!!" -> 13
        else -> 0
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
            )

            // Top-left corner flourish
            Text(
                text = "❦",
                fontSize = 14.sp,
                color = DialogGoldBorder.copy(alpha = 0.55f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp, top = 8.dp)
            )
            // Top-right corner flourish
            Text(
                text = "❦",
                fontSize = 14.sp,
                color = DialogGoldBorder.copy(alpha = 0.55f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 10.dp, top = 8.dp)
            )
            // Bottom-left corner flourish
            Text(
                text = "❦",
                fontSize = 14.sp,
                color = DialogGoldBorder.copy(alpha = 0.55f),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 10.dp, bottom = 8.dp)
            )
            // Bottom-right corner flourish
            Text(
                text = "❦",
                fontSize = 14.sp,
                color = DialogGoldBorder.copy(alpha = 0.55f),
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
            // Crown emblem
            Text(
                text = "✡",
                fontSize = 36.sp,
                color = DialogGoldBorder
            )

            // Headline in Cinzel for that elegant certificate feel
            Text(
                text = "Certificate of\nAchievement",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = Cinzel,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    letterSpacing = 1.sp
                ),
                color = DialogGoldBorder
            )

            // Decorative gold rule
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DialogGoldBorder,
                                Color.Transparent
                            )
                        )
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
            Text(
                text = if (count == 1) "1 Mitzvah" else "$count Mitzvot",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = Cinzel,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                color = DialogGoldBorder
            )

            TranslatableText(
                text = "Current Level",
                style = MaterialTheme.typography.titleSmall.copy(
                    letterSpacing = 1.5.sp
                ),
                color = DialogTextMuted
            )

            // Level pill
            Box(
                modifier = Modifier
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
            ) {
                TranslatableText(
                    text = currentLevel,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = Cinzel,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = DialogGoldBorder
                )
            }

            // Decorative bottom rule
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(2.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                DialogGoldBorder,
                                Color.Transparent
                            )
                        )
                    )
            )

                // Stars
                if (numStars > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(numStars) {
                            Text(
                                text = "✡",
                                fontSize = 28.sp,
                                color = DialogGoldBorder,
                                modifier = Modifier.padding(horizontal = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
