package com.beardytop.beatzaddik.platform

import androidx.compose.runtime.Composable

actual fun exitApplication() = Unit

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) = Unit
