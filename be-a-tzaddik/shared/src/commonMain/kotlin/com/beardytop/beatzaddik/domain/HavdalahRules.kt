package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DayOfWeek

/**
 * When Havdalah appears on the checklist, and which ceremony text to show.
 * Israel vs Diaspora Yom Tov length is already reflected in [DayInfo.isYomTovAssurBemelacha]
 * via the profile's Israel toggle.
 */
object HavdalahRules {

    const val CHECKLIST_ITEM_ID = "havdalah"

    enum class Kind {
        MOTZEI_SHABBAT,
        MOTZEI_YOM_TOV,
        MOTZEI_YOM_KIPPUR,
        DELAYED_AFTER_TISHA_BEAV,
    }

    fun isActive(
        cal: DayInfo,
        yesterdayCal: DayInfo,
        tomorrowCal: DayInfo,
        nowMillis: Long,
    ): Boolean = kind(cal, yesterdayCal, tomorrowCal, nowMillis) != null

    fun kind(
        cal: DayInfo,
        yesterdayCal: DayInfo,
        tomorrowCal: DayInfo,
        nowMillis: Long,
    ): Kind? {
        if (isDelayedAfterTishaBeav(cal, yesterdayCal, nowMillis)) {
            return Kind.DELAYED_AFTER_TISHA_BEAV
        }
        if (isMotzeiYomKippur(cal, yesterdayCal, nowMillis)) {
            return Kind.MOTZEI_YOM_KIPPUR
        }
        // Chag ending Saturday night → full Motzei Shabbat Havdalah (spices + fire),
        // never the short weekday Motzei Yom Tov form.
        if (isYomTovEndingMotzeiShabbat(cal, yesterdayCal, tomorrowCal, nowMillis)) {
            if (isMotzeiShabbatIntoTishaBeav(cal, tomorrowCal)) return null
            return Kind.MOTZEI_SHABBAT
        }
        if (MotzeiShabbatWindow.isActive(cal, tomorrowCal, nowMillis)) {
            // Motzei Shabbat into Tisha B'Av: candle + Baruch ha'mavdil only that night;
            // wine + Hamavdil wait until Sunday night after the fast.
            if (isMotzeiShabbatIntoTishaBeav(cal, tomorrowCal)) return null
            return Kind.MOTZEI_SHABBAT
        }
        if (isMotzeiYomTov(cal, yesterdayCal, tomorrowCal, nowMillis)) {
            return Kind.MOTZEI_YOM_TOV
        }
        return null
    }

    /**
     * Last day of Yom Tov fell on Shabbat — Motzei is both Motzei Shabbat and Motzei Yom Tov.
     * Full Shabbat Havdalah applies (wine, spices, fire).
     */
    fun isYomTovEndingMotzeiShabbat(
        cal: DayInfo,
        yesterdayCal: DayInfo,
        tomorrowCal: DayInfo,
        nowMillis: Long,
    ): Boolean {
        // Still flowing into another Yom Tov day → Yaknehaz / bein kodesh l'kodesh, not this case.
        if ((cal.isShabbat || cal.date.dayOfWeek == DayOfWeek.SATURDAY) &&
            tomorrowCal.isYomTovAssurBemelacha
        ) {
            return false
        }
        // Sunday morning while Yom Tov is still in force (e.g. Diaspora day 2 on Sunday).
        if (cal.date.dayOfWeek == DayOfWeek.SUNDAY && cal.isYomTovAssurBemelacha) {
            return false
        }
        if (!isInMotzeiShabbatClockWindow(cal, nowMillis)) return false

        return when {
            // Saturday night: last day of chag was this Shabbat (still tagged or just rolled).
            cal.date.dayOfWeek == DayOfWeek.SATURDAY &&
                (cal.isYomTovAssurBemelacha ||
                    cal.yesterdayWasYomTovAssurBemelacha ||
                    yesterdayCal.isYomTovAssurBemelacha) -> true
            // Sunday before dawn: yesterday was Shabbat + last day of Yom Tov.
            cal.date.dayOfWeek == DayOfWeek.SUNDAY &&
                !cal.startedTonightAtTzeit &&
                yesterdayCal.date.dayOfWeek == DayOfWeek.SATURDAY &&
                yesterdayCal.isYomTovAssurBemelacha -> true
            else -> false
        }
    }

    /** Saturday after tzeit through Sunday dawn — civil Motzei Shabbat clock. */
    private fun isInMotzeiShabbatClockWindow(cal: DayInfo, nowMillis: Long): Boolean {
        val z = cal.zmanim ?: return false
        if (!z.hasLocationTimes) return false
        val tzeit = z.tzeitMillis ?: return false
        val dawn = MotzeiShabbatWindow.melaveMalkaEndMillis(
            z, cal.date.dayOfWeek == DayOfWeek.SUNDAY,
        ) ?: return false

        if (cal.isShabbat && !cal.isErevShabbat) {
            return nowMillis >= tzeit && nowMillis < dawn
        }
        if (cal.startedTonightAtTzeit && cal.date.dayOfWeek == DayOfWeek.SATURDAY) {
            val sundayDawn = z.alotHaShacharMillis ?: z.sunriseMillis ?: return false
            return nowMillis < sundayDawn
        }
        if (cal.date.dayOfWeek == DayOfWeek.SUNDAY &&
            !cal.startedTonightAtTzeit &&
            nowMillis < dawn
        ) {
            return true
        }
        return false
    }

    private fun isMotzeiShabbatIntoTishaBeav(cal: DayInfo, tomorrowCal: DayInfo): Boolean {
        if (cal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV) return true
        if (tomorrowCal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV &&
            (cal.isShabbat || cal.date.dayOfWeek == DayOfWeek.SATURDAY)
        ) {
            return true
        }
        return false
    }

    fun title(kind: Kind): String = when (kind) {
        Kind.MOTZEI_SHABBAT -> "Havdalah (Motzei Shabbat)"
        Kind.MOTZEI_YOM_TOV -> "Havdalah (Motzei Yom Tov)"
        Kind.MOTZEI_YOM_KIPPUR -> "Havdalah (Motzei Yom Kippur)"
        Kind.DELAYED_AFTER_TISHA_BEAV -> "Havdalah (after Tisha B'Av)"
    }

    fun section(kind: Kind): String = when (kind) {
        Kind.MOTZEI_SHABBAT -> "Motzei Shabbat"
        Kind.MOTZEI_YOM_TOV -> "Motzei Yom Tov"
        Kind.MOTZEI_YOM_KIPPUR -> "Motzei Yom Kippur"
        Kind.DELAYED_AFTER_TISHA_BEAV -> "Motzei Tisha B'Av"
    }

    fun explanationTemplate(kind: Kind, yomKippurWasShabbat: Boolean): String = when (kind) {
        Kind.MOTZEI_SHABBAT -> MOTZEI_SHABBAT_BODY
        Kind.MOTZEI_YOM_TOV -> MOTZEI_YOM_TOV_BODY
        Kind.MOTZEI_YOM_KIPPUR -> if (yomKippurWasShabbat) {
            MOTZEI_YOM_KIPPUR_ON_SHABBAT_BODY
        } else {
            MOTZEI_YOM_KIPPUR_BODY
        }
        Kind.DELAYED_AFTER_TISHA_BEAV -> DELAYED_TISHA_BEAV_BODY
    }

    fun yomKippurWasShabbat(cal: DayInfo, yesterdayCal: DayInfo): Boolean {
        // Motzei: civil Saturday night after YK-on-Shabbat, or Sunday morning before dawn.
        if (cal.date.dayOfWeek == DayOfWeek.SATURDAY) return true
        if (cal.date.dayOfWeek == DayOfWeek.SUNDAY &&
            yesterdayCal.date.dayOfWeek == DayOfWeek.SATURDAY
        ) {
            return true
        }
        return false
    }

    private fun isMotzeiYomKippur(
        cal: DayInfo,
        yesterdayCal: DayInfo,
        nowMillis: Long,
    ): Boolean {
        val z = cal.zmanim ?: return false
        val tzeit = z.tzeitMillis ?: return false
        val dawn = windowEndMillis(z) ?: return false

        // Still tagged as Yom Kippur fast day after nightfall (before Hebrew rollover quirks).
        if (cal.fastDayIndex == HebrewCalendarEngine.YOM_KIPPUR && nowMillis >= tzeit) {
            return true
        }
        // After tzeit rollover: Hebrew day is 11 Tishrei.
        if (cal.startedTonightAtTzeit &&
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI &&
            cal.hebrewDay == 11 &&
            nowMillis < dawn
        ) {
            return true
        }
        // Next civil morning before dawn (11 Tishrei).
        if (!cal.startedTonightAtTzeit &&
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI &&
            cal.hebrewDay == 11 &&
            nowMillis < dawn &&
            (yesterdayCal.fastDayIndex == HebrewCalendarEngine.YOM_KIPPUR ||
                (yesterdayCal.hebrewMonth == HebrewCalendarEngine.TISHREI &&
                    yesterdayCal.hebrewDay == 10))
        ) {
            return true
        }
        return false
    }

    private fun isMotzeiYomTov(
        cal: DayInfo,
        yesterdayCal: DayInfo,
        tomorrowCal: DayInfo,
        nowMillis: Long,
    ): Boolean {
        // Never use short Motzei Yom Tov Havdalah on Motzei Shabbat (including when a chag ends then).
        if (MotzeiShabbatWindow.isActive(cal, tomorrowCal, nowMillis)) return false
        if (isYomTovEndingMotzeiShabbat(cal, yesterdayCal, tomorrowCal, nowMillis)) return false
        if (isInMotzeiShabbatClockWindow(cal, nowMillis)) return false
        // Yom Tov into Shabbat — no Havdalah; Shabbat continues.
        if (cal.isShabbat) return false

        val z = cal.zmanim ?: return false
        if (!z.hasLocationTimes) return false
        val dawn = windowEndMillis(z) ?: return false
        if (nowMillis >= dawn && !cal.startedTonightAtTzeit) return false

        // After tzeit: Hebrew day has left the last Yom Tov day.
        if (cal.startedTonightAtTzeit &&
            cal.yesterdayWasYomTovAssurBemelacha &&
            !cal.isYomTovAssurBemelacha &&
            cal.fastDayIndex != HebrewCalendarEngine.YOM_KIPPUR
        ) {
            return true
        }
        // Next morning before dawn: yesterday was the last Yom Tov day.
        if (!cal.startedTonightAtTzeit &&
            cal.yesterdayWasYomTovAssurBemelacha &&
            !cal.isYomTovAssurBemelacha &&
            yesterdayCal.isYomTovAssurBemelacha &&
            yesterdayCal.fastDayIndex != HebrewCalendarEngine.YOM_KIPPUR &&
            nowMillis < dawn
        ) {
            return true
        }
        return false
    }

    private fun isDelayedAfterTishaBeav(
        cal: DayInfo,
        yesterdayCal: DayInfo,
        nowMillis: Long,
    ): Boolean {
        val z = cal.zmanim ?: return false
        val tzeit = z.tzeitMillis ?: return false
        val dawn = windowEndMillis(z) ?: return false

        // Sunday Tisha B'Av (9 Av was Shabbat) — Havdalah after the fast ends at tzeit.
        if (cal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV &&
            cal.date.dayOfWeek == DayOfWeek.SUNDAY &&
            nowMillis >= tzeit
        ) {
            return true
        }
        // After rollover Sunday night → until dawn Monday.
        if (cal.startedTonightAtTzeit &&
            cal.date.dayOfWeek == DayOfWeek.SUNDAY &&
            yesterdayCal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV &&
            yesterdayCal.date.dayOfWeek == DayOfWeek.SUNDAY &&
            nowMillis < dawn
        ) {
            return true
        }
        if (!cal.startedTonightAtTzeit &&
            cal.date.dayOfWeek == DayOfWeek.MONDAY &&
            yesterdayCal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV &&
            yesterdayCal.date.dayOfWeek == DayOfWeek.SUNDAY &&
            nowMillis < dawn
        ) {
            return true
        }
        return false
    }

    /** End of Motzei window — dawn on the DayInfo's zmanim (next morning when rolled at tzeit). */
    fun windowEndMillis(z: ZmanimSnapshot): Long? =
        z.alotHaShacharMillis ?: z.sunriseMillis

    /**
     * Motzei window start = the nightfall that ended the holy day.
     *
     * After Jewish-day rollover at tzeit, [DayInfo.zmanim] `tzeitMillis` is the *next*
     * nightfall — subtract 24h (same pattern as Melave Malka).
     */
    fun windowStartMillis(cal: DayInfo): Long? {
        val z = cal.zmanim ?: return null
        val tzeit = z.tzeitMillis ?: return null
        val stillBeforeOutgoingTzeit = !cal.startedTonightAtTzeit && (
            (cal.isShabbat && !cal.isErevShabbat) ||
                cal.fastDayIndex == HebrewCalendarEngine.YOM_KIPPUR ||
                cal.fastDayIndex == HebrewCalendarEngine.TISHA_BEAV
            )
        return if (stillBeforeOutgoingTzeit) {
            tzeit
        } else {
            tzeit - 24 * 60 * 60 * 1000L
        }
    }

    private val MOTZEI_SHABBAT_BODY = """
Havdalah (הַבְדָּלָה — "separation") ends Shabbat and returns you to the weekday. Men and women are equally obligated — hear it or recite it yourself.

When:
After nightfall (tzeit hakochavim) Saturday night. Ideal window: Motzei Shabbat from tzeit through dawn Sunday. Do not do melacha after Shabbat ends until you have heard Havdalah or said "Baruch HaMavdil bein Kodesh l'chol."

Full Motzei Shabbat order (follow your siddur):
1. Wine or grape juice — Borei pri hagafen (cup holds at least a revi'it; drink maleh lugmov)
2. Besamim (fragrant spices) — Borei minei besamim
3. Fire — multi-wick candle or two flames held together; Borei me'orei ha'esh
4. Hamavdil — the Havdalah blessing separating holy from mundane

Why spices (besamim)?
Shabbat brings a neshama yeteira (extra soul). When Shabbat ends, that extra soul departs. Smelling spices comforts the soul at that transition.

Why the candle — and looking at your nails?
The fire blessing recalls that Hashem gave Adam HaRishon the wisdom to make fire on the first Motzei Shabbat. Looking at your fingernails in the light is a widespread custom with several meanings:
• To test the flame: you make the blessing only when the light is strong enough to be useful — distinguishing nails from skin needs that kind of light.
• Continuous growth: nails keep growing — a reminder to seek growth, blessing, and prosperity in the new week.
• Adam and Eve: some mystical teachings say the first humans were covered in luminous, fingernail-like "garments of light"; looking at the nails recalls that.
• Light and darkness: many fold the fingers over the thumbs and open them under the candle so light reflects off the nails — illustrating separation of light from darkness. (Some prefer not to look at the thumbs — follow your minhag.)

If you could not make Havdalah Saturday night:
You may still recite Havdalah over wine through Tuesday — but omit spices and the fire blessing (wine + Hamavdil only).

Special calendars:
• When Yom Tov ends Motzei Shabbat (the last day of the chag was Shabbat): still do this full Motzei Shabbat Havdalah with spices and fire — not the short weekday Motzei Yom Tov version.
• Motzei Shabbat into Yom Tov: Havdalah is inside Kiddush (Yaknehaz) — no separate Motzei Shabbat Havdalah with spices.
• Motzei Shabbat into Tisha B'Av: say Baruch ha'mavdil and the candle blessing after Maariv; delay wine + Hamavdil until after the fast (see Sunday night).
    """.trimIndent()

    private val MOTZEI_YOM_TOV_BODY = """
Havdalah after Yom Tov on a weekday (Motzei Yom Tov) — men and women are equally obligated.

This is shorter than Motzei Shabbat Havdalah:
• Wine or grape juice — Borei pri hagafen
• Hamavdil — with the weekday Havdalah text (bein kodesh l'chol)
• No besamim (spices) — the neshama yeteira / spice custom is for Motzei Shabbat
• No fire blessing — the Motzei Shabbat candle (Adam HaRishon / nails) is not part of ordinary Motzei Yom Tov

When:
After nightfall (tzeit) when the last day of Yom Tov ends on a weekday — one day in Israel for most festivals, two days in the Diaspora (your Israel customs setting already drives the calendar). Do not do melacha until you have heard Havdalah or said Baruch HaMavdil.

If Yom Tov ended on Friday into Shabbat: there is no Havdalah then — Shabbat is a higher holiness. When Shabbat ends Saturday night, make full Motzei Shabbat Havdalah (wine, spices, and fire) — including when that Motzei Shabbat is also the end of the chag.

If you missed it tonight: ask your rav; many allow wine + Hamavdil into the following days (no spices or fire).
    """.trimIndent()

    private val MOTZEI_YOM_KIPPUR_BODY = """
Havdalah after Yom Kippur — men and women are equally obligated. Do this after nightfall before or with your break-fast meal.

Order:
• Wine or grape juice — Borei pri hagafen
• Fire — Borei me'orei ha'esh over a ner she-shavat (a flame that burned throughout Yom Kippur, such as a 48-hour candle lit before the fast). You may look at the light; this is not the Motzei Shabbat Adam HaRishon / fingernail custom in the same way.
• Hamavdil
• No besamim (spices) on a weekday Motzei Yom Kippur — spices are for the departure of the Shabbat extra soul

Then eat a proper Motzei Yom Kippur meal (see your checklist).
    """.trimIndent()

    private val MOTZEI_YOM_KIPPUR_ON_SHABBAT_BODY = """
Yom Kippur fell on Shabbat — tonight is both Motzei Shabbat and Motzei Yom Kippur. Men and women are equally obligated.

Order (full Motzei Shabbat style, with a Yom Kippur flame):
• Wine or grape juice — Borei pri hagafen
• Besamim (spices) — include them as on a regular Motzei Shabbat (neshama yeteira)
• Fire — Borei me'orei ha'esh over a ner she-shavat that burned throughout Yom Kippur (and Shabbat). Looking at the nails / Adam HaRishon Motzei Shabbat customs apply here as on a regular Motzei Shabbat.
• Hamavdil

Then eat a proper Motzei Yom Kippur meal (see your checklist).
    """.trimIndent()

    private val DELAYED_TISHA_BEAV_BODY = """
Havdalah was delayed because Tisha B'Av began Motzei Shabbat. Men and women are equally obligated.

Tonight after the fast ends (tzeit Sunday night):
• Wine or grape juice — Borei pri hagafen
• Hamavdil only
• No besamim (spices)
• No fire blessing — you already said Borei me'orei ha'esh Motzei Shabbat after Maariv when the fast began

This is not a full Motzei Shabbat Havdalah. Do not wait until Monday for wine + Hamavdil once the fast has ended tonight.
    """.trimIndent()
}
