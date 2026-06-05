package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun LowEndDeviceBackground(modifier: Modifier = Modifier) {
    // App-wide background: static gradient (all devices; no looping video).
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color(0xFF050B1F),
                        0.45f to Color(0xFF0E1B47),
                        0.85f to Color(0xFF1A0B3D),
                        1f to Color(0xFF0A0420)
                    )
                )
            )
    )
}
