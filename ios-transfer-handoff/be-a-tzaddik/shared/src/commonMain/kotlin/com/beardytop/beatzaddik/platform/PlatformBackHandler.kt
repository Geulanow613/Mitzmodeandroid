package com.beardytop.beatzaddik.platform

import androidx.compose.runtime.Composable

/** Closes the app (Android). No-op on other platforms. */
expect fun exitApplication()

/**
 * Handles the system back gesture/button when [enabled].
 * On Android this intercepts the default "exit app" behavior.
 */
@Composable
expect fun PlatformBackHandler(enabled: Boolean = true, onBack: () -> Unit)
