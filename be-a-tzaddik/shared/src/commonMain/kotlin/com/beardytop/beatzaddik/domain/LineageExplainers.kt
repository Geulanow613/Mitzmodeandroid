package com.beardytop.beatzaddik.domain

/**
 * Short "?" tip copy for male lineage profile questions (onboarding / Settings).
 * Full mitzvah explainers live on the checklist item definitions.
 */
object LineageExplainers {
    const val KOHEN = "Kohanim are male descendants of Aaron the Kohen (the first High Priest). " +
        "If you are unsure of your family status, ask your father or grandfather — " +
        "or your rabbi — about your family history."

    const val LEVI = "Levi'im are male descendants of the Tribe of Levi (but not of Aaron — those are Kohanim). " +
        "If you are unsure, ask your father or grandfather — or your rabbi — about your family history."

    const val FIRSTBORN = "A firstborn son here means a Jewish woman's first child who is a boy — " +
        "the first to \"open the womb.\" If you are unsure, ask your parents about your family history."

    /** Father may have a son who needs Pidyon HaBen — no \"ask someone\" (you know how he was born). */
    const val HAS_FIRSTBORN_SON = "A firstborn son means a Jewish woman's first child who is a boy — " +
        "the first to \"open the womb.\""

    const val PIDYON_HABEN_PROFILE_NOTE =
        "The bottom 2 options are used to determine Redemption of the Firstborn son mitzvot (Pidyon HaBen)."

    const val PROFILE_PRIVACY_NOTE =
        "No information provided is shared with us or anyone else. It stays solely on your device."

    const val GENDER_PROFILE_DESCRIPTION =
        "Determines which mitzvot appear in your daily checklist. $PROFILE_PRIVACY_NOTE"
}

/** Stable checklist ids for lineage / lifelong male mitzvot. */
object LineageChecklistIds {
    const val KOHANIM_LAWS = "observe_laws_of_kohanim"
    const val LEVI_DUTIES = "levi_duties"
    const val BRIT_MILAH = "brit_milah"
    const val PIDYON_HABEN = "pidyon_haben"

    val HIDEABLE_FOREVER: Set<String> = setOf(PIDYON_HABEN)
}
