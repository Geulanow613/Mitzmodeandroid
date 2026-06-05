package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.UserProfile

actual object KashrutNotifications {
    actual fun schedule(wait: KashrutWait, profile: UserProfile) = Unit
    actual fun cancel() = Unit
}
