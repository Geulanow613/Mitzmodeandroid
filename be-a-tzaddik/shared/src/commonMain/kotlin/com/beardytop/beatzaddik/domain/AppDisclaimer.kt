package com.beardytop.beatzaddik.domain

/** Shared copy for startup dialog and About. */
object AppDisclaimer {
    const val TITLE = "Disclaimer"

    /** Embedded Mitz Mode checklist title (splash, onboarding, About). */
    const val EMBEDDED_APP_TITLE = "the Holy Light Checklist"

    /** Headline shown above the disclaimer body. */
    const val WELCOME_HEADLINE = "A companion for your day"

    fun welcomeIntro(embeddedMode: Boolean): String =
        if (embeddedMode) {
            "The Holy Light Checklist helps you track standard daily mitzvot in a Torah-observant routine."
        } else {
            "Be a Tzaddik helps you track standard daily mitzvot in a Torah-observant routine."
        }

    /** Disclaimer body — first-launch dialog and About tab. */
    const val STARTUP_BODY =
        "This app is a learning companion, not a rabbi — it does not give halachic rulings, but we try to provide accurate information.\n\n" +
            "This checklist does not contain all the mitzvot in the entire Torah. It covers standard daily mitzvot " +
            "that observant Jews commonly practice — waking, prayer, blessings, meals, Torah study, " +
            "Shabbat preparation, and similar foundations.\n\n" +
            "It is highly recommended to get a quality siddur (prayerbook). You can order one from any online Jewish bookstore, " +
            "and there are also siddur apps. Many Jewish texts (including the Chumash) are available for free online.\n\n" +
            "With your permission, the app uses GPS or a city you choose to calculate Jewish calendar times " +
            "and when you can fulfill different mitzvot throughout the day (for example morning, afternoon, " +
            "and evening prayer windows, candle lighting, and Shabbat-related times). Location is kept on " +
            "your device only for zmanim and the calendar.\n\n" +
            "If you are new to Judaism, take it slow. Build steady habits without " +
            "stress, and do what you can. And always ask an Orthodox rabbi you trust when something is unclear or when " +
            "your situation needs personal guidance."

    const val PRODUCER_INTRO = "Brought to you by"
    const val PRODUCER_NAME = "Beardy Top Productions"
    const val WEBSITE_URL = "https://www.beardy.top"
    const val WEBSITE_DISPLAY = "www.beardy.top"

    const val FEEDBACK_EMAIL = "beardytopproductions@gmail.com"
    const val FEEDBACK_EMAIL_MAILTO = "mailto:beardytopproductions@gmail.com"

    const val FEEDBACK_ABOUT_PROMPT = "Found an error or have suggestions? Email"

    const val TRANSLATION_NOTICE_TITLE = "Translation Notice"

    const val TRANSLATION_NOTICE_DISCLAIMER =
        "Translations in this app are translated from English using machine-assisted methods and may not be perfect. " +
            "Please send any errors you find to:"

    const val TRANSLATION_LINKS_NOTE =
        "(Most URL links still lead to English-based websites.)"
}
