package com.beardytop.beatzaddik.domain

/** Shared copy for startup dialog and About. */
object AppDisclaimer {
    const val TITLE = "Disclaimer"

    /** Headline shown above the disclaimer body. */
    const val WELCOME_HEADLINE = "A companion for your day"

    fun welcomeIntro(embeddedMode: Boolean): String =
        if (embeddedMode) {
            "The Daily Mitzvot Checklist helps you track standard daily mitzvot in a Torah-observant routine."
        } else {
            "Be a Tzaddik helps you track standard daily mitzvot in a Torah-observant routine."
        }

    /** Disclaimer body — first-launch dialog and About tab. */
    const val STARTUP_BODY =
        "This app is a learning companion, not a rabbi — it does not give halachic rulings.\n\n" +
            "This checklist does not contain all the mitzvot in the entire Torah. It covers standard daily mitzvot " +
            "that observant Jews commonly practice — waking, prayer, blessings, meals, Torah study, " +
            "Shabbat preparation, and similar foundations.\n\n" +
            "With your permission, the app uses GPS or a city you choose to calculate Jewish calendar times " +
            "and when you can fulfill different mitzvot throughout the day (for example morning, afternoon, " +
            "and evening prayer windows, candle lighting, and Shabbat-related times). Location is kept on " +
            "your device only for zmanim and the calendar.\n\n" +
            "If you are new to Judaism, take it slow and do what you can. Build steady habits without " +
            "overwhelm, and always ask an Orthodox rabbi you trust when something is unclear or when " +
            "your situation needs personal guidance."

    const val PRODUCER_INTRO = "Brought to you by"
    const val PRODUCER_NAME = "Beardy Top Productions"
    const val WEBSITE_URL = "https://www.beardy.top"
    const val WEBSITE_DISPLAY = "www.beardy.top"
}
