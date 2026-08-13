package com.beardytop.beatzaddik.platform

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.beardytop.beatzaddik.domain.Gender

private const val TAG = "LauncherIcon"
private const val ALIAS_TZADDIK = "LauncherTzaddik"
private const val ALIAS_TZADEKET = "LauncherTzadeket"

/** Alias class simple name used to start this process, if any. */
private var sessionLaunchAlias: String? = null
private var pendingDisable: ComponentName? = null

/** Call from MainActivity.onCreate before applying icon state. */
fun recordLauncherEntryIntent(activity: Activity) {
    sessionLaunchAlias = activity.intent.launcherAliasName()
        ?: activity.inferEnabledLauncherAlias()
}

/** Call from MainActivity.onStop to disable the old launcher alias safely. */
fun flushPendingLauncherAliasDisable(activity: Activity) {
    val component = pendingDisable ?: return
    pendingDisable = null
    try {
        activity.packageManager.setAliasEnabled(component, enabled = false)
    } catch (e: Exception) {
        Log.w(TAG, "Could not disable launcher alias on stop", e)
    }
}

actual fun applyLauncherIcon(gender: Gender) {
    val activity = PlatformActivityHolder.currentActivity() ?: return
    val decorView = activity.window?.decorView
    val run = Runnable { applyLauncherIconNow(activity, gender) }
    if (decorView != null) {
        decorView.post(run)
    } else {
        activity.runOnUiThread(run)
    }
}

private fun applyLauncherIconNow(activity: Activity, gender: Gender) {
    try {
        val pm = activity.packageManager
        val pkg = activity.packageName
        val tzaddik = ComponentName(pkg, "$pkg.$ALIAS_TZADDIK")
        val tzadeket = ComponentName(pkg, "$pkg.$ALIAS_TZADEKET")
        val wantFemale = gender == Gender.FEMALE

        val tzaddikOn = pm.isAliasEnabled(tzaddik, defaultEnabled = true)
        val tzadeketOn = pm.isAliasEnabled(tzadeket, defaultEnabled = false)
        if (wantFemale == tzadeketOn && (!wantFemale) == tzaddikOn) return

        val launchAlias = sessionLaunchAlias
        if (wantFemale) {
            pm.setAliasEnabled(tzadeket, enabled = true)
            disableAliasWhenSafe(
                pm = pm,
                component = tzaddik,
                aliasName = ALIAS_TZADDIK,
                launchAlias = launchAlias
            )
        } else {
            pm.setAliasEnabled(tzaddik, enabled = true)
            disableAliasWhenSafe(
                pm = pm,
                component = tzadeket,
                aliasName = ALIAS_TZADEKET,
                launchAlias = launchAlias
            )
        }
    } catch (e: Exception) {
        Log.w(TAG, "Could not update launcher icon", e)
    }
}

/**
 * Disabling the alias that launched this session kills the process.
 * Enable the target alias immediately; disable the other now only if it
 * was not the launch entry, otherwise defer until [flushPendingLauncherAliasDisable].
 */
private fun disableAliasWhenSafe(
    pm: PackageManager,
    component: ComponentName,
    aliasName: String,
    launchAlias: String?
) {
    // If we cannot tell how the user opened the app, defer disable to onStop.
    if (launchAlias == null || aliasName == launchAlias) {
        pendingDisable = component
        return
    }
    pendingDisable = null
    pm.setAliasEnabled(component, enabled = false)
}

private fun Intent.launcherAliasName(): String? {
    val fromComponent = component?.className?.substringAfterLast('.')
    if (fromComponent == ALIAS_TZADDIK || fromComponent == ALIAS_TZADEKET) return fromComponent
    return null
}

private fun Activity.inferEnabledLauncherAlias(): String? {
    val pm = packageManager
    val pkg = packageName
    val tzaddikOn = pm.isAliasEnabled(ComponentName(pkg, "$pkg.$ALIAS_TZADDIK"), defaultEnabled = true)
    val tzadeketOn = pm.isAliasEnabled(ComponentName(pkg, "$pkg.$ALIAS_TZADEKET"), defaultEnabled = false)
    return when {
        tzadeketOn && !tzaddikOn -> ALIAS_TZADEKET
        tzaddikOn && !tzadeketOn -> ALIAS_TZADDIK
        else -> null
    }
}

private fun PackageManager.isAliasEnabled(
    component: ComponentName,
    defaultEnabled: Boolean
): Boolean = when (getComponentEnabledSetting(component)) {
    PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> true
    PackageManager.COMPONENT_ENABLED_STATE_DISABLED -> false
    else -> defaultEnabled
}

private fun PackageManager.setAliasEnabled(component: ComponentName, enabled: Boolean) {
    val state = if (enabled) {
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    } else {
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    }
    setComponentEnabledSetting(
        component,
        state,
        PackageManager.DONT_KILL_APP
    )
}
