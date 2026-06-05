package com.beardytop.beatzaddik.domain

internal fun shabbatMessage(): String =
    "Please put away your device and keep Shabbat. " +
        "Rest, pray, learn Torah, and enjoy time with family and community. " +
        "Using electronics on Shabbat is forbidden by halacha (melacha)."

internal fun yomTovMessage(holidayName: String): String =
    "Today is $holidayName. Please put away your device and keep the day holy. " +
        "Using electronics on Yom Tov is forbidden by halacha (melacha)."
