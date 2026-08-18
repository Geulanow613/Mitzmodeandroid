@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.MealCategory
import com.beardytop.beatzaddik.domain.UserProfile
import com.beardytop.beatzaddik.navigation.AppNavigation
import kotlinx.datetime.Clock
import platform.AudioToolbox.AudioServicesPlaySystemSound
import platform.AudioToolbox.kSystemSoundID_Vibrate
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNAuthorizationStatusDenied
import platform.UserNotifications.UNAuthorizationStatusEphemeral
import platform.UserNotifications.UNAuthorizationStatusNotDetermined
import platform.UserNotifications.UNAuthorizationStatusProvisional
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionBanner
import platform.UserNotifications.UNNotificationPresentationOptionList
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject
import kotlin.concurrent.AtomicReference

/**
 * iOS local notifications for the kashrut timer.
 *
 * Ported from Mitz Mode (sharedmodule) and trimmed to Be a Tzaddik's expect API.
 * iOS has no sticky status-bar chronometer; we post a Notification Center item
 * plus a scheduled end alert.
 */
actual object KashrutNotifications {
    internal const val END_ID = "kashrut_timer_end"
    internal const val STATUS_ID = "kashrut_timer_status"
    internal const val THREAD_ID = "kashrut-timer"
    internal const val USERINFO_VIBRATE = "kashrut_vibrate"

    /** null = unknown, true = granted, false = denied/restricted. */
    private val allowedCache = AtomicReference<Boolean?>(null)
    private val centerDelegate = KashrutNotificationCenterDelegate()

    init {
        warmUp()
    }

    /** Call at app launch so taps on cold-start notifications still reach our delegate. */
    fun warmUp() {
        ensureDelegate()
        refreshPermissionStatus()
    }

    actual fun areNotificationsAllowed(): Boolean {
        return allowedCache.value == true
    }

    actual fun requestPermission(onResult: (granted: Boolean) -> Unit) {
        ensureDelegate()
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.getNotificationSettingsWithCompletionHandler { settings ->
            val status = settings?.authorizationStatus
            applyAuthorizationStatus(status)
            when (status) {
                UNAuthorizationStatusAuthorized,
                UNAuthorizationStatusProvisional,
                UNAuthorizationStatusEphemeral,
                -> onResult(true)
                UNAuthorizationStatusNotDetermined -> {
                    val opts =
                        UNAuthorizationOptionAlert or
                            UNAuthorizationOptionSound or
                            UNAuthorizationOptionBadge
                    center.requestAuthorizationWithOptions(opts) { granted, _ ->
                        allowedCache.value = granted
                        onResult(granted)
                    }
                }
                else -> {
                    allowedCache.value = false
                    onResult(false)
                }
            }
        }
    }

    actual fun openNotificationSettings() {
        val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString) ?: return
        val app = UIApplication.sharedApplication
        if (app.canOpenURL(url)) {
            app.openURL(url, options = emptyMap<Any?, Any>(), completionHandler = null)
        }
    }

    actual fun schedule(wait: KashrutWait, profile: UserProfile) {
        ensureDelegate()
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.removePendingNotificationRequestsWithIdentifiers(listOf(END_ID, STATUS_ID))
        center.removeDeliveredNotificationsWithIdentifiers(listOf(STATUS_ID))
        center.getNotificationSettingsWithCompletionHandler { settings ->
            applyAuthorizationStatus(settings?.authorizationStatus)
            if (allowedCache.value != true) return@getNotificationSettingsWithCompletionHandler
            if (profile.showKashrutTimerNotification) {
                postStatus(wait, finished = false, sound = false, vibrate = false)
            }
            scheduleEnd(wait, profile)
        }
    }

    actual fun cancel() {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        center.removePendingNotificationRequestsWithIdentifiers(listOf(END_ID, STATUS_ID))
        center.removeDeliveredNotificationsWithIdentifiers(listOf(END_ID, STATUS_ID))
    }

    actual fun showFinished(wait: KashrutWait, profile: UserProfile) {
        ensureDelegate()
        cancel()
        if (!profile.showKashrutTimerNotification) return
        UNUserNotificationCenter.currentNotificationCenter()
            .getNotificationSettingsWithCompletionHandler { settings ->
                applyAuthorizationStatus(settings?.authorizationStatus)
                if (allowedCache.value != true) return@getNotificationSettingsWithCompletionHandler
                postStatus(
                    wait,
                    finished = true,
                    sound = profile.kashrutTimerSound,
                    vibrate = profile.kashrutTimerVibrate,
                )
            }
    }

    actual fun dismissStatusNotification() {
        UNUserNotificationCenter.currentNotificationCenter()
            .removeDeliveredNotificationsWithIdentifiers(listOf(STATUS_ID))
        UNUserNotificationCenter.currentNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(listOf(STATUS_ID))
    }

    private fun refreshPermissionStatus() {
        UNUserNotificationCenter.currentNotificationCenter()
            .getNotificationSettingsWithCompletionHandler { settings ->
                applyAuthorizationStatus(settings?.authorizationStatus)
            }
    }

    private fun applyAuthorizationStatus(status: Long?) {
        when (status) {
            UNAuthorizationStatusAuthorized,
            UNAuthorizationStatusProvisional,
            UNAuthorizationStatusEphemeral,
            -> allowedCache.value = true
            UNAuthorizationStatusNotDetermined,
            UNAuthorizationStatusDenied,
            -> allowedCache.value = false
            else -> Unit
        }
    }

    private fun ensureDelegate() {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        if (center.delegate !== centerDelegate) {
            center.delegate = centerDelegate
        }
    }

    private fun scheduleEnd(wait: KashrutWait, profile: UserProfile) {
        val nowMs = Clock.System.now().toEpochMilliseconds()
        val delaySec = ((wait.endsAtEpochMillis - nowMs) / 1000.0).coerceAtLeast(1.0)
        val content = UNMutableNotificationContent().apply {
            setTitle("Kashrut timer")
            setBody("You can now eat ${oppositeLabel(wait.category)}")
            if (profile.kashrutTimerSound) {
                setSound(UNNotificationSound.defaultSound)
            }
            setUserInfo(mapOf(USERINFO_VIBRATE to profile.kashrutTimerVibrate))
            setThreadIdentifier(THREAD_ID)
        }
        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(delaySec, repeats = false)
        val request = UNNotificationRequest.requestWithIdentifier(END_ID, content, trigger)
        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) { error: NSError? ->
            if (error != null) {
                println("KashrutNotifications: failed to schedule end: $error")
            }
        }
    }

    private fun postStatus(wait: KashrutWait, finished: Boolean, sound: Boolean, vibrate: Boolean) {
        val nowMs = Clock.System.now().toEpochMilliseconds()
        val remaining = (wait.endsAtEpochMillis - nowMs).coerceAtLeast(0L)
        val content = UNMutableNotificationContent().apply {
            setTitle("Kashrut timer")
            setBody(
                if (finished) {
                    "You can now eat ${oppositeLabel(wait.category)}"
                } else {
                    "Waiting before ${oppositeLabel(wait.category)} · ${formatRemaining(remaining)}"
                },
            )
            if (sound) setSound(UNNotificationSound.defaultSound)
            setUserInfo(mapOf(USERINFO_VIBRATE to vibrate))
            setThreadIdentifier(THREAD_ID)
        }
        val request = UNNotificationRequest.requestWithIdentifier(STATUS_ID, content, null)
        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) { error: NSError? ->
            if (error != null) {
                println("KashrutNotifications: failed to post status: $error")
            }
        }
        if (vibrate) playVibrate()
    }

    private fun oppositeLabel(category: MealCategory): String = when (category) {
        MealCategory.MEAT -> "dairy"
        MealCategory.DAIRY -> "meat"
    }

    private fun formatRemaining(ms: Long): String {
        val totalMin = ((ms + 59_999L) / 60_000L).toInt().coerceAtLeast(0)
        val h = totalMin / 60
        val m = totalMin % 60
        return when {
            h > 0 && m > 0 -> "${h}h ${m}m left"
            h > 0 -> "${h}h left"
            m > 0 -> "${m}m left"
            else -> "ending soon"
        }
    }

    internal fun playVibrateIfRequested(userInfo: Map<Any?, *>?) {
        val flag = userInfo?.get(USERINFO_VIBRATE)
        val shouldVibrate = when (flag) {
            is Boolean -> flag
            else -> false
        }
        if (shouldVibrate) playVibrate()
    }

    private fun playVibrate() {
        AudioServicesPlaySystemSound(kSystemSoundID_Vibrate)
    }
}

/** Required so banners appear while the app is in the foreground, and taps open Timer. */
private class KashrutNotificationCenterDelegate :
    NSObject(),
    UNUserNotificationCenterDelegateProtocol {

    @ObjCSignatureOverride
    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (platform.UserNotifications.UNNotificationPresentationOptions) -> Unit,
    ) {
        KashrutNotifications.playVibrateIfRequested(willPresentNotification.request.content.userInfo)
        withCompletionHandler(
            UNNotificationPresentationOptionBanner or
                UNNotificationPresentationOptionList or
                UNNotificationPresentationOptionSound,
        )
    }

    @ObjCSignatureOverride
    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        didReceiveNotificationResponse: UNNotificationResponse,
        withCompletionHandler: () -> Unit,
    ) {
        val request = didReceiveNotificationResponse.notification.request
        val id = request.identifier
        val thread = request.content.threadIdentifier
        if (
            id == KashrutNotifications.END_ID ||
            id == KashrutNotifications.STATUS_ID ||
            thread == KashrutNotifications.THREAD_ID
        ) {
            AppNavigation.requestTimerTab()
        }
        withCompletionHandler()
    }
}
