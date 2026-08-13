package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.UserProfile

expect object KashrutNotifications {
    /** Schedules the end alarm and, if enabled in [profile], an ongoing status notification. */
    fun schedule(wait: KashrutWait, profile: UserProfile)

    /** Cancels the end alarm and dismisses status / finished notifications. */
    fun cancel()

    /** Posts or updates the “you may eat …” finished notification (no alarm). */
    fun showFinished(wait: KashrutWait, profile: UserProfile)

    /** Dismisses only the status-bar notification; leaves any end alarm alone. */
    fun dismissStatusNotification()

    /** True when the OS will allow this app to post notifications. */
    fun areNotificationsAllowed(): Boolean

    /**
     * Asks for notification permission (Android 13+ runtime dialog when possible).
     * Invokes [onResult] with whether notifications are allowed afterward.
     */
    fun requestPermission(onResult: (granted: Boolean) -> Unit)

    /** Opens the system screen where the user can enable notifications for this app. */
    fun openNotificationSettings()
}
