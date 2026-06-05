package com.beardytop.beatzaddik.domain

internal fun shabbatMessage(): String =
    "Please put away your device and keep Shabbat. " +
        "Rest, pray, learn Torah, and enjoy time with family and community. " +
        "Melacha (forbidden work on Shabbat) includes most phone and device use — ask your rav if unsure."

internal fun yomTovMessage(holidayName: String): String =
    "Today is $holidayName (Yom Tov — a festival day). Please put away your device and keep the day holy. " +
        "Melacha (forbidden work) applies on Yom Tov similar to Shabbat — ask your rav if unsure."
