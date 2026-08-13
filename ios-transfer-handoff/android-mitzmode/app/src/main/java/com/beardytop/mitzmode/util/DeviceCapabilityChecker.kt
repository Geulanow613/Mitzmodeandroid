package com.beardytop.mitzmode.util

import android.content.Context

object DeviceCapabilityChecker {
    /**
     * Looping home-screen video background is disabled app-wide; all devices use the static
     * gradient ([com.beardytop.mitzmode.ui.components.LowEndDeviceBackground]).
     * Kept for call sites that gate optional UI flourishes (e.g. dialog sparkle particles).
     */
    @Suppress("UNUSED_PARAMETER")
    fun canHandleVideoBackground(context: Context): Boolean = false
}
