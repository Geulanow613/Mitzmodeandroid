package com.beardytop.beatzaddik.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp

object TextScaleDefaults {
    const val MIN = 0.75f
    const val MAX = 2.5f
    const val STEP = 0.1f
    const val NORMAL = 1f

    fun coerce(scale: Float): Float = scale.coerceIn(MIN, MAX)

    fun percentLabel(scale: Float): String {
        val pct = (coerce(scale) * 100).toInt()
        return "$pct%"
    }
}

object TzaddikColors {
    val GoldBorder = Color(0xFFB8860B)
    val GoldBright = Color(0xFFFFD56B)
    val GoldDeep = Color(0xFF8A6508)
    val ParchTop = Color(0xFFFFFBEE)
    val ParchMid = Color(0xFFFFF8E1)
    val ParchBase = Color(0xFFFFF0CC)
    val TextBrown = Color(0xFF1A0A00)
    val TextMuted = Color(0xFF5C4A2E)
    val NavyDeep = Color(0xFF12305A)
    val NavyMid = Color(0xFF1F4685)
    val CheckBlue = Color(0xFF2B5BA8)
}

private val scheme = darkColorScheme(
    primary = TzaddikColors.GoldBright,
    onPrimary = TzaddikColors.TextBrown,
    surface = TzaddikColors.ParchBase,
    onSurface = TzaddikColors.TextBrown
)

/**
 * Serif headings give a refined, traditional feel; clean sans body keeps long
 * explanations highly legible. Uses platform-available families (no font assets).
 */
private val Display = FontFamily.Serif

private val TzaddikTypography = Typography(
    displayLarge = TextStyle(fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 40.sp, lineHeight = 48.sp),
    displayMedium = TextStyle(fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 42.sp),
    displaySmall = TextStyle(fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 30.sp, lineHeight = 38.sp),
    headlineLarge = TextStyle(fontFamily = Display, fontWeight = FontWeight.Bold, fontSize = 30.sp, lineHeight = 38.sp),
    headlineMedium = TextStyle(fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 26.sp, lineHeight = 34.sp),
    headlineSmall = TextStyle(fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 30.sp, letterSpacing = 0.3.sp),
    titleLarge = TextStyle(fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 26.sp),
    titleMedium = TextStyle(fontFamily = Display, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 24.sp, letterSpacing = 0.2.sp),
    titleSmall = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 17.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 13.sp, letterSpacing = 0.4.sp),
    labelMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp, letterSpacing = 0.3.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp, letterSpacing = 0.3.sp)
)

@Composable
fun TzaddikTheme(
    textScale: Float = 1f,
    content: @Composable () -> Unit
) {
    val base = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(
            base.density,
            base.fontScale * TextScaleDefaults.coerce(textScale)
        )
    ) {
        MaterialTheme(colorScheme = scheme, typography = TzaddikTypography, content = content)
    }
}
