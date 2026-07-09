package com.beardytop.beatzaddik.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Gold mitzvah-count pill shown briefly in the top-right when embedded in Mitz Mode
 * and the user checks a checklist item.
 */
@Composable
fun MitzModeCountPillOverlay(
    mitzvotCount: Int,
    modifier: Modifier = Modifier,
) {
    var visible by remember { mutableStateOf(false) }
    var baselineCount by remember { mutableIntStateOf(mitzvotCount) }

    LaunchedEffect(mitzvotCount) {
        if (mitzvotCount > baselineCount) {
            baselineCount = mitzvotCount
            visible = true
            delay(2_500)
            visible = false
        } else if (mitzvotCount < baselineCount) {
            baselineCount = mitzvotCount
        }
    }

    val gold = Color(0xFFFFD56B)
    val pillActive = mitzvotCount > 0

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(50),
                    spotColor = gold,
                )
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (pillActive) 0.20f else 0.10f),
                            Color.White.copy(alpha = if (pillActive) 0.08f else 0.04f),
                        ),
                    ),
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            gold.copy(alpha = if (pillActive) 0.85f else 0.4f),
                            gold.copy(alpha = if (pillActive) 0.5f else 0.2f),
                        ),
                    ),
                    shape = RoundedCornerShape(50),
                )
                .padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (pillActive) gold else gold.copy(alpha = 0.5f),
                    modifier = Modifier.size(26.dp),
                )
                Text(
                    text = "$mitzvotCount",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.45f),
                            offset = Offset(0f, 1f),
                            blurRadius = 3f,
                        ),
                    ),
                    color = if (pillActive) gold else gold.copy(alpha = 0.55f),
                )
            }
        }
    }
}
