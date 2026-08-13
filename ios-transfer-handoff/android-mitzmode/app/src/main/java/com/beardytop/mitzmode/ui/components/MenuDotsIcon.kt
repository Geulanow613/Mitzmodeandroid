package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Vertically stacked menu dots, geometrically centered (unlike [MoreVert] glyph padding). */
@Composable
fun MenuDotsIcon(
    tint: Color,
    modifier: Modifier = Modifier,
    dotSize: Dp = 4.dp,
    dotSpacing: Dp = 3.dp,
) {
    Column(
        modifier = modifier.size(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dotSpacing, Alignment.CenterVertically),
    ) {
        repeat(3) {
            Box(
                Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(tint)
            )
        }
    }
}
