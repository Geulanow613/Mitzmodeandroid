package com.beardytop.beatzaddik.domain

/**
 * Re-evaluates stored checklist zman state against a live clock so rows unlock between
 * the 60-second [ChecklistEngine] refresh ticks.
 */
object ZmanAvailabilityLive {

    fun derive(
        availability: ItemZmanAvailability,
        windowStartMillis: Long?,
        windowEndMillis: Long?,
        nowMillis: Long,
    ): ItemZmanAvailability = when (availability) {
        ItemZmanAvailability.UPCOMING -> when {
            windowStartMillis != null && nowMillis >= windowStartMillis &&
                windowEndMillis != null && nowMillis >= windowEndMillis ->
                ItemZmanAvailability.EXPIRED
            windowStartMillis != null && nowMillis >= windowStartMillis ->
                ItemZmanAvailability.ACTIVE
            else -> ItemZmanAvailability.UPCOMING
        }
        ItemZmanAvailability.ACTIVE -> when {
            windowEndMillis != null && nowMillis >= windowEndMillis ->
                ItemZmanAvailability.EXPIRED
            else -> ItemZmanAvailability.ACTIVE
        }
        ItemZmanAvailability.EXPIRED -> ItemZmanAvailability.EXPIRED
    }
}
