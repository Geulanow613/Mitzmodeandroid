package com.beardytop.beatzaddik.domain

/**
 * Halachic helpers for checklist windows (chatzos halayla, etc.).
 */
object ZmanimHelpers {

    /**
     * Chatzos halayla — midpoint between sunset and the next dawn (alot hashachar).
     */
    fun chatzosLaylaMillis(z: ZmanimSnapshot, nowMillis: Long): Long? {
        val sunset = z.sunsetMillis ?: return null
        val dawn = z.nightObligationsEndMillis ?: z.alotHaShacharMillis ?: return null
        val nightStart = if (nowMillis >= sunset) sunset else sunset - 24 * 60 * 60 * 1000L
        val nightEnd = if (nowMillis >= sunset) dawn else (z.alotHaShacharMillis ?: dawn)
        return nightStart + (nightEnd - nightStart) / 2
    }

    /**
     * When Birchot HaShachar / Birkat HaTorah become available today:
     * after chatzos halayla at night, or from dawn onward if it is already daytime.
     */
    fun birchotHashacharWindowStart(nowMillis: Long, z: ZmanimSnapshot): Long? {
        val sunset = z.sunsetMillis ?: return z.alotHaShacharMillis
        val dawn = z.nightObligationsEndMillis ?: z.alotHaShacharMillis ?: return null
        val chatzosLayla = chatzosLaylaMillis(z, nowMillis) ?: return dawn
        return when {
            nowMillis >= sunset && nowMillis < dawn -> chatzosLayla
            nowMillis >= dawn && nowMillis < sunset -> dawn
            else -> chatzosLayla
        }
    }
}
