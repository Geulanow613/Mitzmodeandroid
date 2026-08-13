package com.beardytop.beatzaddik.ui.components

import androidx.compose.runtime.Composable

/**
 * Full-screen final reward video (`finalreward.mp4`).
 * Starts muted; tap the bottom-corner speaker control to unmute.
 */
@Composable
expect fun FinalRewardVideoOverlay(
    visible: Boolean,
    onComplete: () -> Unit,
)
