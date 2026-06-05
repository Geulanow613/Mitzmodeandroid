package com.beardytop.beatzaddik.platform

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import java.lang.ref.WeakReference

object PlatformActivityHolder {
    private var activityRef: WeakReference<Activity>? = null

    fun bind(activity: Activity) {
        activityRef = WeakReference(activity)
    }

    fun unbind(activity: Activity) {
        if (activityRef?.get() === activity) activityRef = null
    }

    fun currentActivity(): Activity? = activityRef?.get()
}

actual fun exitApplication() {
    PlatformActivityHolder.currentActivity()?.finish()
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}
