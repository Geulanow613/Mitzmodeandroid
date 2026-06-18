package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

/**
 * Builds plain-English Sefirat HaOmer copy with tonight's count, next night, and local tzeit when available.
 */
object OmerCountText {

    fun buildTitle(day: Int): String =
        "Count the Omer — ${omerDaySummary(day)}"

    fun headerLabel(day: Int): String = omerCountSpeechPhrase(day).removeSuffix(".")

    fun statusChipLabel(day: Int): String = headerLabel(day)

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
    fun omerDaySummary(day: Int): String {
        require(day in 1..49)
        val dayWord = if (day == 1) "day" else "days"
        return if (day < 7) {
            "$day $dayWord of the Omer"
        } else {
            "$day days, which is ${weeksAndDays(day)} of the Omer"
        }
    }

    /** Spoken count text — days 1–6 mention days only; week phrase from day 7 onward (Peninei Halakha). */
    fun omerCountSpeechPhrase(day: Int): String =
        "Today is ${omerDaySummary(day)}."

    fun buildExplanation(cal: DayInfo, profile: UserProfile): String {
        val day = cal.omerDay ?: return ""
        val tz = cal.zmanim?.timezoneId ?: profile.timezoneId
        val tzeit = cal.zmanim?.tzeitMillis ?: cal.zmanim?.sunsetMillis
        val timePart = ZmanimFormatter.formatTime(tzeit, tz)?.let { " at $it" } ?: ""
        val tonight = cal.date.dayOfWeek.displayName()
        val tomorrow = cal.date.plus(1, DateTimeUnit.DAY)
        val tomorrowNight = tomorrow.dayOfWeek.displayName()

        val nusachWhen = when (profile.effectiveNusach()) {
            EffectiveNusach.CHABAD -> "Many in Chabad count after Maariv (Tehillat Hashem)."
            EffectiveNusach.SEFARD -> "Many Sephardim count after Maariv."
            EffectiveNusach.EDOT_HAMIZRACH -> "Many Edot HaMizrach kehillot count after Maariv."
            EffectiveNusach.ASHKENAZ -> "Many Ashkenazim count after Maariv."
        }

        val nextNightLine = if (day < 49) {
            "\n• ${tomorrowNight} night: you will count ${omerDaySummary(day + 1)}."
        } else {
            ""
        }

        val todaySummary = omerDaySummary(day)

        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.omerBasics(),
            """
Sefirat HaOmer links Pesach to Shavuot — counting each day from the Exodus toward receiving the Torah.

Today in the Omer: $todaySummary (day $day of 49).

Tonight's count:
• $tonight night — count $todaySummary after nightfall$timePart.
$nextNightLine

How to count:
• Stand and recite the blessing before counting if you are still saying it with a blessing (if you missed a day, ask your rabbi before continuing with a bracha).
• Say: "${omerCountSpeechPhrase(day)}"
• Count after nightfall (tzeit); complete before dawn. If you forgot at night, count the next day during the daytime without a bracha. If you do this before sunset, you can continue counting on subsequent nights with a bracha. You only lose the blessing permanently if you miss an entire 24-hour cycle (both night and the following day) — ask your rav.

$nusachWhen
        """.trim().replace("\n\n\n", "\n\n"),
        )
    }

    private fun DayOfWeek.displayName(): String =
        name.lowercase().replaceFirstChar { it.uppercase() }
}
