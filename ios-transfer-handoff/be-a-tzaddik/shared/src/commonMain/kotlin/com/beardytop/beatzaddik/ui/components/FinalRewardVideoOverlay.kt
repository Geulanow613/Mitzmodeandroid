package com.beardytop.beatzaddik.ui.components

import androidx.compose.runtime.Composable

/**
 * Full-screen final reward video (`finalreward.mp4`).
 * Android plays from assets; iOS is a no-op stub until AVPlayer is wired.
 */
@Composable
expect fun FinalRewardVideoOverlay(
    visible: Boolean,
    onComplete: () -> Unit,
)
