package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.UserProfile

expect object KashrutNotifications {
    fun schedule(wait: KashrutWait, profile: UserProfile)
    fun cancel()
}
