package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

/**
 * Builds plain-English Sefirat HaOmer copy with tonight's count, next night, and local tzeit when available.
 */
object OmerCountText {

    /** Ashkenaz: ba'omer (בעומר); Chabad, Sephardi, and Edot HaMizrach: la'omer (לעומר). */
    fun omerSuffixHe(nusach: EffectiveNusach): String = when (nusach) {
        EffectiveNusach.ASHKENAZ -> "בעומר"
        EffectiveNusach.CHABAD,
        EffectiveNusach.SEFARD,
        EffectiveNusach.EDOT_HAMIZRACH -> "לעומר"
    }

    fun buildTitle(day: Int, nusach: EffectiveNusach): String =
        "Count the Omer — ${omerDaySummary(day, nusach)}"

    fun localizedBuildTitle(day: Int, languageCode: String, nusach: EffectiveNusach): String =
        when (languageCode) {
            "he", "yi" -> "ספירת העומר — ${omerDaySummaryHe(day, nusach)}"
            else -> buildTitle(day, nusach)
        }

    fun headerLabel(day: Int, nusach: EffectiveNusach): String =
        omerCountSpeechPhrase(day, nusach).removeSuffix(".")

    /** Header line for calendar chip — localized for Hebrew/Yiddish with nusach-specific suffix. */
    fun localizedHeaderLabel(day: Int, languageCode: String, nusach: EffectiveNusach): String =
        when (languageCode) {
            "he", "yi" -> "היום ${omerDaySummaryHe(day, nusach)}"
            else -> headerLabel(day, nusach)
        }

    fun omerDaySummaryHe(day: Int, nusach: EffectiveNusach): String {
        val suffix = omerSuffixHe(nusach)
        require(day in 1..49)
        if (day < 7) {
            return if (day == 1) "יום אחד $suffix" else "$day ימים $suffix"
        }
        return "$day ימים, שהם ${weeksAndDaysHe(day)} $suffix"
    }

    /** Parse day 1–49 from checklist title keys like "Count the Omer — 8 days…". */
    fun dayFromCountTitle(titleKey: String): Int? =
        Regex("""Count the Omer — (\d+)""").find(titleKey)?.groupValues?.getOrNull(1)?.toIntOrNull()

    private fun weeksAndDaysHe(day: Int): String {
        val weeks = day / 7
        val rem = day % 7
        return when {
            rem == 0 -> when (weeks) {
                1 -> "שבוע אחד"
                2 -> "שבועיים"
                else -> "$weeks שבועות"
            }
            weeks == 1 && rem == 1 -> "שבוע ויום אחד"
            weeks == 1 -> "שבוע ו-$rem ימים"
            rem == 1 -> "$weeks שבועות ויום אחד"
            else -> "$weeks שבועות ו-$rem ימים"
        }
    }

    fun statusChipLabel(day: Int, nusach: EffectiveNusach): String = headerLabel(day, nusach)

    fun weeksAndDays(day: Int): String {
        require(day in 1..49)
        val weeks = day / 7
        val rem = day % 7
        return when {
            weeks == 0 -> if (rem == 1) "1 day" else "$rem days"
            rem == 0 -> if (weeks == 1) "1 week" else "$weeks weeks"
            weeks == 1 && rem == 1 -> "1 week and 1 day"
            weeks == 1 -> "1 week and $rem days"
            rem == 1 -> "$weeks weeks and 1 day"
            else -> "$weeks weeks and $rem days"
        }
    }

    /** e.g. "10 days, which is 1 week and 3 days of the Omer" (days 1–6: "3 days of the Omer"). */
    fun omerDaySummary(day: Int, nusach: EffectiveNusach): String {
        require(day in 1..49)
        val dayWord = if (day == 1) "day" else "days"
        return if (day < 7) {
            "$day $dayWord of the Omer"
        } else {
            "$day days, which is ${weeksAndDays(day)} of the Omer"
        }
    }

    /** Spoken count text — days 1–6 mention days only; week phrase from day 7 onward (Peninei Halakha). */
    fun omerCountSpeechPhrase(day: Int, nusach: EffectiveNusach): String =
        "Today is ${omerDaySummary(day, nusach)}."

    fun buildExplanation(cal: DayInfo, profile: UserProfile): String {
        val args = explanationArgs(cal, profile)
        return ExplainerTemplateFill.fill(explanationTemplate(), args)
    }

    fun explanationTemplate(): String = OMER_EXPLANATION_TEMPLATE

    fun explanationArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val day = cal.omerDay ?: return emptyMap()
        val nusach = profile.effectiveNusach()
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeit = cal.zmanim?.let(::omerNightfallMillis)
        val timePart = ZmanimFormatter.formatTime(tzeit, tz)?.let {
            ExplainerTemplateFill.fill(OMER_TIME_PART, mapOf("time" to it))
        } ?: ""
        // After the tzeit rollover the DayInfo already describes the new Hebrew day:
        // tonight's (ongoing) count is `day` itself and the real civil evening is date - 1.
        val rolled = cal.startedTonightAtTzeit
        val tonightDate = if (rolled) cal.date.plus(-1, DateTimeUnit.DAY) else cal.date
        val tonight = tonightDate.dayOfWeek.displayName()
        val tomorrowNight = tonightDate.plus(1, DateTimeUnit.DAY).dayOfWeek.displayName()
        val todaySummary = omerDaySummary(day, nusach)
        val tonightCountDay = if (rolled) day else day + 1
        val tonightSummary = if (tonightCountDay <= 49) omerDaySummary(tonightCountDay, nusach) else ""
        val nextDaySummary = if (tonightCountDay < 49) omerDaySummary(tonightCountDay + 1, nusach) else ""
        val nextNightLine = if (tonightCountDay < 49) OMER_NEXT_NIGHT_LINE else ""
        val speechPhrase = if (tonightCountDay <= 49) {
            "Today is ${omerDaySummary(tonightCountDay, nusach)}."
        } else {
            ""
        }
        val nusachWhen = when (nusach) {
            EffectiveNusach.CHABAD -> "Many in Chabad count after Maariv (Tehillat Hashem)."
            EffectiveNusach.SEFARD -> "Many Sephardim count after Maariv."
            EffectiveNusach.EDOT_HAMIZRACH -> "Many Edot HaMizrach kehillot count after Maariv."
            EffectiveNusach.ASHKENAZ -> "Many Ashkenazim count after Maariv."
        }
        return mapOf(
            "day" to day.toString(),
            "nusach" to nusach.name,
            "todaySummary" to todaySummary,
            "tonightSummary" to tonightSummary,
            "tonight" to tonight,
            "tomorrowNight" to tomorrowNight,
            "timePart" to timePart,
            "nextDaySummary" to nextDaySummary,
            "nextNightLine" to nextNightLine,
            "speechPhrase" to speechPhrase,
            "nusachWhen" to nusachWhen,
        )
    }

    /**
     * Evening tzeit for tonight's count — rejects dawn/mis-ordered values that can appear when
     * zmanim data is partial or mis-ordered (e.g. tomorrow's alot hashachar).
     */
    internal fun omerNightfallMillis(zmanim: ZmanimSnapshot): Long? {
        val sunset = zmanim.sunsetMillis ?: return zmanim.tzeitMillis
        val candidate = zmanim.tzeitMillis ?: (sunset + OMER_TZEIT_AFTER_SUNSET_MS)
        val latestTzeit = sunset + OMER_MAX_TZEIT_AFTER_SUNSET_MS
        return when {
            candidate in sunset..latestTzeit -> candidate
            candidate > sunset -> sunset + OMER_TZEIT_AFTER_SUNSET_MS
            else -> sunset + OMER_TZEIT_AFTER_SUNSET_MS
        }
    }

    private const val OMER_TZEIT_AFTER_SUNSET_MS = 33 * 60 * 1000L
    private const val OMER_MAX_TZEIT_AFTER_SUNSET_MS = 120 * 60 * 1000L

    /** Nested template arg — catalog key; filled after main template lookup. */
    const val OMER_NEXT_NIGHT_LINE = "\n• ${'$'}tomorrowNight night: you will count ${'$'}nextDaySummary."

    /** Nested template arg for optional local tzeit time after "after nightfall". */
    private const val OMER_TIME_PART = " at ${'$'}time"

    private val OMER_EXPLANATION_TEMPLATE = """
Sefirat HaOmer links Pesach to Shavuot — counting each day from the Exodus toward receiving the Torah.

Today in the Omer: ${'$'}todaySummary (day ${'$'}day of 49).

Tonight's count:
• ${'$'}tonight night — count ${'$'}tonightSummary after nightfall${'$'}timePart.
${'$'}nextNightLine

How to count:
• Stand and recite the blessing before counting if you are still saying it with a blessing (if you missed a day, ask your rabbi before continuing with a bracha).
• Say: "${'$'}speechPhrase"
• Count after nightfall (tzeit); complete before dawn. If you forgot at night, count the next day during the daytime without a bracha. If you do this before sunset, you can continue counting on subsequent nights with a bracha. You only lose the blessing permanently if you miss an entire 24-hour cycle (both night and the following day) — ask your rav.

${'$'}nusachWhen
    """.trimIndent()

    private fun DayOfWeek.displayName(): String =
        name.lowercase().replaceFirstChar { it.uppercase() }
}
