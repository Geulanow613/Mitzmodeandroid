package com.beardytop.beatzaddik.domain

/**
 * Soft rating ask used before the store review link.
 * Happy path (4–5) → App Store / Play review. Lower → Formspree feedback.
 */
object AppRatingPromptPolicy {
    const val MIN_LAUNCHES = 5
    /** ~3 days after first recorded launch before we ask. */
    const val MIN_DAYS_SINCE_FIRST_LAUNCH = 3
    /** Wait after "Not now" before asking again. */
    const val SNOOZE_DAYS = 14
    const val POSITIVE_STAR_THRESHOLD = 4

    const val FORMSPREE_ENDPOINT = "https://formspree.io/f/movjwkbe"
    const val APP_STORE_URL = "https://apps.apple.com/app/mitz-mode/id6740733628"
    const val PLAY_STORE_LISTING_URL =
        "https://play.google.com/store/apps/details?id=com.beardytop.mitzmode"

    fun isEligible(profile: UserProfile, nowEpochMillis: Long): Boolean {
        if (profile.ratingPromptCompleted) return false
        if (profile.ratingPromptLaunchCount < MIN_LAUNCHES) return false
        val first = profile.ratingPromptFirstLaunchEpochMillis ?: return false
        val minAgeMs = MIN_DAYS_SINCE_FIRST_LAUNCH * 24L * 60L * 60L * 1000L
        if (nowEpochMillis - first < minAgeMs) return false
        val snoozeUntil = profile.ratingPromptSnoozeUntilEpochMillis
        if (snoozeUntil != null && nowEpochMillis < snoozeUntil) return false
        return true
    }

    fun snoozeUntil(nowEpochMillis: Long): Long =
        nowEpochMillis + SNOOZE_DAYS * 24L * 60L * 60L * 1000L
}
