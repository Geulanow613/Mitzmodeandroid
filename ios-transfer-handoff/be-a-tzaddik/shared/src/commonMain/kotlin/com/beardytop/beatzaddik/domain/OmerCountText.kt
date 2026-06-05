package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.plus

/**
 * Builds plain-English Sefirat HaOmer copy with tonight's count, next night, and local tzeit when available.
 */
object OmerCountText {

    fun buildTitle(day: Int): String =
        "Count the Omer — day $day of 49 (${weeksAndDays(day)})"

    fun statusChipLabel(day: Int): String =
        "Omer day $day — ${weeksAndDays(day)}"

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
            EffectiveNusach.SEFARD -> "Many Sefardim count after Maariv."
            EffectiveNusach.ASHKENAZ -> "Many Ashkenazim count after Maariv."
        }

        val nextNightLine = if (day < 49) {
            "\n• ${tomorrowNight} night: you will count day ${day + 1} (${weeksAndDays(day + 1)})."
        } else {
            ""
        }

        val weeksPhrase = weeksAndDays(day)

        return BeginnerHalachaGlossary.withKeyTerms(
            BeginnerHalachaGlossary.omerBasics(),
            """
Sefirat HaOmer links Pesach to Shavuot — counting each day from the Exodus toward receiving the Torah.

Today in the Omer: day $day of 49 — $weeksPhrase.

Tonight's count:
• $tonight night — count day $day ($weeksPhrase) after nightfall$timePart.
$nextNightLine

How to count:
• Stand and recite the blessing before counting if you are still saying it with a blessing (if you missed a day, ask your rabbi before continuing with a bracha).
• Say: "Today is $day ${if (day == 1) "day" else "days"} of the Omer, which is ${weeksAndDays(day)}."
• Count after nightfall (tzeit); complete before dawn. If you forgot at night, many poskim have you count the next day without a bracha — ask your rav.

$nusachWhen
        """.trim().replace("\n\n\n", "\n\n"),
        )
    }

    private fun DayOfWeek.displayName(): String =
        name.lowercase().replaceFirstChar { it.uppercase() }
}
