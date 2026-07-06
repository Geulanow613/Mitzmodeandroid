package com.beardytop.mitzmode.utils

import android.content.Context

/**
 * Thin wrapper so older imports still work; logic lives in
 * [com.beardytop.mitzmode.util.DeviceCapabilityChecker].
 */
@Suppress("unused")
object DeviceCapabilityChecker {
    fun canHandleVideoBackground(context: Context): Boolean =
        com.beardytop.mitzmode.util.DeviceCapabilityChecker.canHandleVideoBackground(context)
}
