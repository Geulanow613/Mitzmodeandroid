package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.platform.KashrutNotifications

class KashrutTimerService {
    fun startMeal(profile: UserProfile, category: MealCategory, nowMillis: Long): KashrutWait {
        val waitMinutes = when (category) {
            MealCategory.MEAT -> profile.meatToDairyHours() * 60
            MealCategory.DAIRY -> profile.dairyToMeatWaitMinutes()
        }
        val ends = nowMillis + waitMinutes * 60_000L
        return KashrutWait(category, nowMillis, ends)
    }

    fun remainingMillis(wait: KashrutWait?, nowMillis: Long): Long? {
        wait ?: return null
        return (wait.endsAtEpochMillis - nowMillis).coerceAtLeast(0)
    }

    fun label(wait: KashrutWait?): String? {
        wait ?: return null
        return when (wait.category) {
            MealCategory.MEAT -> "Dairy allowed after meat wait ends"
            MealCategory.DAIRY -> "Meat allowed after dairy wait ends"
        }
    }

    fun scheduleEndNotification(wait: KashrutWait, profile: UserProfile) {
        KashrutNotifications.schedule(wait, profile)
    }

    fun cancelNotification() {
        KashrutNotifications.cancel()
    }
}
