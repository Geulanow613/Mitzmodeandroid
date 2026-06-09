package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek

/**
 * Melave malkah and similar Motzei Shabbat mitzvot: from tzeit hakochavim Saturday
 * until alot hashachar Sunday. Excludes Motzei Shabbat into Yom Tov.
 */
object MotzeiShabbatWindow {

    fun isActive(cal: DayInfo, tomorrowCal: DayInfo, nowMillis: Long): Boolean {
        val z = cal.zmanim ?: return false
        if (!z.hasLocationTimes) return false
        val tzeit = z.tzeitMillis ?: return false
        val dawn = ZmanPeriodLogic.nightObligationWindowEnd(z) ?: return false

        if (cal.isShabbat && !cal.isErevShabbat) {
            if (nowMillis < tzeit) return false
            if (tomorrowCal.isYomTovAssurBemelacha) return false
            return nowMillis < dawn
        }

        if (cal.date.dayOfWeek == DayOfWeek.SUNDAY && nowMillis < dawn) {
            if (cal.isYomTovAssurBemelacha) return false
            return true
        }

        return false
    }

    fun startMillis(cal: DayInfo, nowMillis: Long): Long? {
        val z = cal.zmanim ?: return null
        val tzeit = z.tzeitMillis ?: return null
        return if (cal.isShabbat && !cal.isErevShabbat) {
            tzeit
        } else if (cal.date.dayOfWeek == DayOfWeek.SUNDAY) {
            tzeit - 24 * 60 * 60 * 1000L
        } else {
            null
        }
    }

    fun endMillis(cal: DayInfo): Long? =
        cal.zmanim?.let { ZmanPeriodLogic.nightObligationWindowEnd(it) }
}
