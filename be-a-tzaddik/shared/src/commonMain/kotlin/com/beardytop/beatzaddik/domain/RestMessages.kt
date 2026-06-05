package com.beardytop.beatzaddik.domain

internal const val SHABBAT_REST_TITLE = "It's Shabbat Now"

internal fun shabbatMessage(): String =
    "Please put away your device and keep Shabbat. " +
        "Rest, pray, learn Torah, and enjoy time with family and community. " +
        "Melacha (forbidden work on Shabbat) includes most phone and device use — ask your rav if unsure."

internal fun shabbatApproachingMessage(): String =
    "Shabbat is about to begin. Please finish what you are doing, turn off your phone, " +
        "and prepare to welcome the holy day.\n\n" +
        "Our Sages teach that Shabbat is a taste of the World to Come, and one who guards Shabbat " +
        "according to its laws receives forgiveness and a reward beyond measure (Shabbat 25b; Shabbat 118a)."

internal fun yomTovMessage(holidayName: String): String =
    "Today is $holidayName (Yom Tov — a festival day). Please put away your device and keep the day holy. " +
        "Melacha (forbidden work) applies on Yom Tov similar to Shabbat — ask your rav if unsure."

internal fun yomTovApproachingMessage(holidayName: String): String =
    "$holidayName is about to begin. Please finish what you are doing, turn off your phone, " +
        "and prepare to welcome this Yom Tov with joy.\n\n" +
        "Our Sages teach that rejoicing on Yom Tov is itself a mitzvah — may your observance " +
        "bring blessing and divine reward to you and your household."
