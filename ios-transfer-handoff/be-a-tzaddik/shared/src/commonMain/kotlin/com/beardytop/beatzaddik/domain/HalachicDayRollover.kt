package com.beardytop.beatzaddik.domain

/**
 * The Hebrew calendar day begins at nightfall (tzeit hakochavim), not at civil midnight.
 * Between tonight's tzeit and civil midnight the halachic date is already "tomorrow" —
 * e.g. after tzeit on 17 Tammuz the date is 18 Tammuz and the fast has ended.
 *
 * Both calendar backends (Android KosherJava, iOS NSCalendar) use this rule to advance
 * the Hebrew date, holiday flags, and active seasons at tzeit. When no location (and
 * therefore no tzeit) is available, the civil day boundary is used as a fallback.
 */
object HalachicDayRollover {

    /** True when [nowMillis] is past tonight's tzeit — the next Hebrew day has begun. */
    fun hasRolledOver(nowMillis: Long, tzeitTonightMillis: Long?): Boolean =
        tzeitTonightMillis != null && nowMillis >= tzeitTonightMillis
}
