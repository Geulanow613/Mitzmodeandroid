package com.beardytop.beatzaddik.domain

/**
 * Plain-English labels for the active checklist block vs. the "later" collapsed section.
 */
object ZmanPeriodLabels {

    data class Labels(
        val activeTitle: String,
        val activeSummary: String?,
        val laterSummary: String?
    )

    fun forPeriod(
        active: TimeOfDay,
        zmanim: ZmanimSnapshot,
        nusach: EffectiveNusach = EffectiveNusach.ASHKENAZ
    ): Labels {
        val tz = zmanim.timezoneId
        val morning = morningSummary(zmanim, tz)
        val afternoon = afternoonSummary(zmanim, tz, nusach)
        val evening = eveningSummary(zmanim, tz)
        val daytimeLater = listOfNotNull(afternoon, evening).joinToString(" · ").ifBlank { null }
        val fullDayLater = listOfNotNull(morning, afternoon).joinToString(" · ").ifBlank { null }
        return when (active) {
            TimeOfDay.DAY -> Labels(
                activeTitle = "Current Period: Daytime — morning",
                activeSummary = morning,
                laterSummary = daytimeLater
            )
            TimeOfDay.AFTERNOON -> Labels(
                activeTitle = "Current Period: Afternoon",
                activeSummary = afternoon,
                laterSummary = evening
            )
            TimeOfDay.NIGHT -> Labels(
                activeTitle = "Current Period: Night — evening & bedtime",
                activeSummary = evening,
                laterSummary = fullDayLater
            )
            TimeOfDay.ANY -> Labels(
                activeTitle = "Current Period: Daytime — morning",
                activeSummary = morning,
                laterSummary = evening
            )
        }
    }

    private fun morningSummary(z: ZmanimSnapshot, tz: String): String? =
        ZmanimFormatter.formatTime(z.sofZmanShemaMillis, tz)?.let {
            "Latest time for morning Shema: $it"
        }

    private fun afternoonSummary(z: ZmanimSnapshot, tz: String, nusach: EffectiveNusach): String? =
        listOfNotNull(
            minchaLine(z, tz),
            minchaEndLine(z, tz, nusach)
        ).joinToString(" · ").ifBlank { null }

    /** Chabad: until nightfall (tzeit). Ashkenaz / Sefard: until shortly after sunset. */
    private fun minchaEndLine(z: ZmanimSnapshot, tz: String, nusach: EffectiveNusach): String? =
        when (nusach) {
            EffectiveNusach.CHABAD -> {
                val time = ZmanimFormatter.formatTime(z.tzeitMillis, tz)
                    ?: ZmanimFormatter.formatTime(z.sunsetMillis, tz)
                if (time != null) "Until nightfall: $time" else "Until nightfall"
            }
            EffectiveNusach.ASHKENAZ, EffectiveNusach.SEFARD -> {
                val time = ZmanimFormatter.formatTime(z.sunsetMillis, tz)
                if (time != null) "Until shortly after sunset: $time"
                else "Until shortly after sunset"
            }
        }

    private fun eveningSummary(z: ZmanimSnapshot, tz: String): String {
        val sunset = ZmanimFormatter.formatTime(z.sunsetMillis, tz)
        return if (sunset != null) {
            "Evening prayers available after $sunset"
        } else {
            "Evening prayers available after sunset"
        }
    }

    private fun minchaLine(z: ZmanimSnapshot, tz: String): String? {
        val from = ZmanimFormatter.formatAfter(z.minchaGedolaMillis, tz)
            ?: ZmanimFormatter.formatTime(z.minchaGedolaMillis, tz)?.let { "from $it" }
        return from?.let { "Afternoon prayer (Mincha) $it" }
    }
}
