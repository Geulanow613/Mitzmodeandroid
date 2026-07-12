package com.beardytop.beatzaddik.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/** iOS stub — final reward playback can be wired to AVPlayer later. */
@Composable
actual fun FinalRewardVideoOverlay(
    visible: Boolean,
    onComplete: () -> Unit,
) {
    if (visible) {
        LaunchedEffect(Unit) { onComplete() }
    }
}
