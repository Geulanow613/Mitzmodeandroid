package com.beardytop.beatzaddik.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
 * Gold mitzvah-count pill (unified / embedded).
 * Hidden by default; briefly fades in when the count increases, then fades out again.
 * Tap opens Status when [onClick] is set (only while visible).
 */
@Composable
fun MitzModeCountPillOverlay(
    mitzvotCount: Int,
    modifier: Modifier = Modifier,
    alwaysVisible: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    var highlight by remember { mutableStateOf(false) }
    var lastCount by remember { mutableStateOf(mitzvotCount) }

    LaunchedEffect(mitzvotCount) {
        if (mitzvotCount > lastCount) {
            highlight = true
            delay(2_500)
            highlight = false
        }
        lastCount = mitzvotCount
    }

    val gold = Color(0xFFFFD56B)
    val pillActive = mitzvotCount > 0 || highlight
    val alpha by animateFloatAsState(
        targetValue = when {
            alwaysVisible -> 1f
            highlight -> 1f
            else -> 0f
        },
        label = "countPillAlpha",
    )
    if (alpha <= 0.01f && !alwaysVisible) return

    Box(
        modifier = modifier
            .alpha(alpha)
            .then(
                if (onClick != null && alpha > 0.5f) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
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
