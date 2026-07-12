package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek

/**
 * Melave Malka and similar Motzei Shabbat mitzvot: from tzeit hakochavim Saturday
 * until alot hashachar Sunday. Excludes Motzei Shabbat into Yom Tov.
 */
object MotzeiShabbatWindow {

    /**
     * Dawn that ends Melave Malka (Sunday alot after Motzei Shabbat).
     * On a Sunday [ZmanimSnapshot], [ZmanPeriodLogic.nightObligationWindowEnd] is Monday dawn —
     * use today's alot hashachar for this mitzvah instead.
     */
    fun melaveMalkaEndMillis(z: ZmanimSnapshot, isSundayCivilDay: Boolean): Long? =
        if (isSundayCivilDay) {
            z.alotHaShacharMillis ?: z.sunriseMillis
        } else {
            ZmanPeriodLogic.nightObligationWindowEnd(z)
        }

    fun isActive(cal: DayInfo, tomorrowCal: DayInfo, nowMillis: Long): Boolean {
        val z = cal.zmanim ?: return false
        if (!z.hasLocationTimes) return false
        val tzeit = z.tzeitMillis ?: return false
        val dawn = melaveMalkaEndMillis(z, cal.date.dayOfWeek == DayOfWeek.SUNDAY) ?: return false

        if (cal.isShabbat && !cal.isErevShabbat) {
            if (nowMillis < tzeit) return false
            if (tomorrowCal.isYomTovAssurBemelacha) return false
            return nowMillis < dawn
        }

        // After tzeit Saturday the Hebrew day has rolled (often isShabbat=false) while
        // civil date is still Saturday — Melave Malka continues until Sunday dawn.
        if (cal.startedTonightAtTzeit && cal.date.dayOfWeek == DayOfWeek.SATURDAY) {
            if (tomorrowCal.isYomTovAssurBemelacha) return false
            val sundayDawn = z.alotHaShacharMillis ?: z.sunriseMillis ?: return false
            return nowMillis < sundayDawn
        }

        // Sunday morning before dawn: still Motzei. After Sunday tzeit, DayInfo keeps the
        // civil Sunday date but attaches Monday's zmanim — that is NOT Motzei Shabbat.
        if (cal.date.dayOfWeek == DayOfWeek.SUNDAY &&
            !cal.startedTonightAtTzeit &&
            nowMillis < dawn
        ) {
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
        } else if (cal.date.dayOfWeek == DayOfWeek.SUNDAY && !cal.startedTonightAtTzeit) {
            tzeit - 24 * 60 * 60 * 1000L
        } else {
            null
        }
    }

    fun endMillis(cal: DayInfo): Long? {
        val z = cal.zmanim ?: return null
        if (cal.date.dayOfWeek == DayOfWeek.SUNDAY && cal.startedTonightAtTzeit) return null
        return melaveMalkaEndMillis(z, cal.date.dayOfWeek == DayOfWeek.SUNDAY)
    }
}
