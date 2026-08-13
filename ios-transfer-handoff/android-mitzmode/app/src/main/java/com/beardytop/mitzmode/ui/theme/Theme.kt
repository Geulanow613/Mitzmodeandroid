package com.beardytop.mitzmode.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = OnDarkPrimary,
    primaryContainer = BlueLight,
    onPrimaryContainer = BlueDeep,
    secondary = GoldAccentDeep,
    onSecondary = OnDarkPrimary,
    secondaryContainer = GoldAccent,
    onSecondaryContainer = OnLightPrimary,
    tertiary = OliveAccent,
    background = Parchment,
    onBackground = OnLightPrimary,
    surface = Parchment,
    onSurface = OnLightPrimary,
    surfaceVariant = ParchmentMidTone,
    onSurfaceVariant = OnLightPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueLight,
    onPrimary = BlueMidnight,
    primaryContainer = BlueDeep,
    onPrimaryContainer = BlueLight,
    secondary = GoldAccent,
    onSecondary = BlueMidnight,
    secondaryContainer = GoldAccentDeep,
    onSecondaryContainer = OnDarkPrimary,
    tertiary = OliveAccent,
    background = BlueMidnight,
    onBackground = OnDarkPrimary,
    surface = BlueDeep,
    onSurface = OnDarkPrimary,
    surfaceVariant = BlueDeep,
    onSurfaceVariant = OnDarkPrimary
)

@Composable
fun MitzModeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
