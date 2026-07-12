package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.UserProfile

actual object KashrutNotifications {
    actual fun schedule(wait: KashrutWait, profile: UserProfile) = Unit
    actual fun cancel() = Unit
    actual fun showFinished(wait: KashrutWait, profile: UserProfile) = Unit
    actual fun dismissStatusNotification() = Unit
    actual fun areNotificationsAllowed(): Boolean = true
    actual fun requestPermission(onResult: (granted: Boolean) -> Unit) = onResult(true)
    actual fun openNotificationSettings() = Unit
}
