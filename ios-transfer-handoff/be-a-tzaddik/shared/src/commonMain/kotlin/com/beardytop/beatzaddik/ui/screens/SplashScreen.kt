package com.beardytop.beatzaddik.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.beatzaddik.ui.components.SplashCandleAmbient
import com.beardytop.beatzaddik.ui.components.SplashCandleCanvas
import com.beardytop.beatzaddik.ui.components.rememberCandleFlickerState
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    isFemale: Boolean = false,
    titleOverride: String? = null
) {
    val splashTitle = titleOverride ?: if (isFemale) "Be a Tzadeket" else "Be a Tzaddik"
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val flameScale = remember { Animatable(0.6f) }

    LaunchedEffect(Unit) {
        flameScale.animateTo(1f, tween(700, easing = FastOutSlowInEasing))
        titleAlpha.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
        delay(200)
        subtitleAlpha.animateTo(1f, tween(600, easing = FastOutSlowInEasing))
        delay(1600)
        titleAlpha.animateTo(0f, tween(500))
        subtitleAlpha.animateTo(0f, tween(500))
        flameScale.animateTo(0f, tween(400))
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A1628), Color(0xFF0D0820))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val flickerState = rememberCandleFlickerState()

        SplashCandleAmbient(
            flameScale = flameScale.value,
            flickerState = flickerState,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            SplashCandleCanvas(flameScale = flameScale.value, flickerState = flickerState)

            Spacer(Modifier.height(24.dp))

            Text(
                splashTitle,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    letterSpacing = 0.5.sp
                ),
                color = TzaddikColors.GoldBright,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                "Your daily companion\nfor Torah-observant living",
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 24.sp
                ),
                color = TzaddikColors.ParchTop.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(subtitleAlpha.value)
            )
        }
    }
}
