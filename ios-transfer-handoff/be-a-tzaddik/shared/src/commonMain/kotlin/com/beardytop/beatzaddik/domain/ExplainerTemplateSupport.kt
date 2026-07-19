package com.beardytop.beatzaddik.domain

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

/**
 * Template keys and runtime args for checklist explainers that vary by profile or calendar.
 * Catalog keys use `$placeholder` tokens; args supply English (or nested catalog keys).
 */
object ExplainerTemplateSupport {

    private val calendarService = JewishCalendarService()

    private val nusachNoteSlot: String get() = "\n\n" + "$" + "nusachNote"

    // ── Arba Minim ───────────────────────────────────────────────────────────────

    private const val ARBA_MINIM_DAYS_ISRAEL =
        "In Israel: lulav all 7 days; first day is the primary Torah obligation for men."

    private const val ARBA_MINIM_DAYS_DIASPORA =
        "In the Diaspora: men's primary Torah obligation is the first day of Sukkot (15 Tishrei); the mitzvah continues rabbinically on the second day of Yom Tov and Chol HaMoed."

    private val ARBA_MINIM_MEN_TEMPLATE = """
Arba Minim (ארבעה מינים) — the Four Species — are taken each day of Sukkot (except Shabbat).

The four:
• Lulav — closed palm branch (at least 3 tefachim)
• Etrog — citron, beautiful and mostly intact (pitom if present should be intact)
• Hadasim — three myrtle branches
• Aravot — two willow branches

How to observe (everyone):
• Assemble the lulav: Bind the lulav, hadassim, and aravot together (per your hoshanah holder / koisan). Ensure the central spine of the lulav extends upward at least one handbreadth (tefach — around 4 inches) higher than the tops of the myrtle and willow branches (Shulchan Arukh O.C. 650:1).
• Check kashrut before Yom Tov — especially etrog and that leaves are fresh enough.
• When: Daytime; many do it before Shacharit at home or in shul. Not on Shabbat.
• ${'$'}daysNote

If a species is missing or invalid, ask your rabbi — there are limited substitutions in pressing cases.

Men — Torah obligation:
• The first day of Sukkot worldwide (15 Tishrei) is the Torah-level day for men. In the Diaspora, the mitzvah continues rabbinically on the second day of Yom Tov and Chol HaMoed.
• Bracha: Men say Al netilat lulav every day when taking the lulav (except Shabbat). Say Shehecheyanu on the first day you take the lulav; if you missed the first day, say it on the first day you take (Mishnah Berurah 651:12).
• ${'$'}menWave

The ownership rule (lakhem — u'lekachtem lakhem):
• In Israel: the strict requirement to fully own your lulav and etrog set applies on the first day of Sukkot (15 Tishrei; Shulchan Arukh O.C. 658:3). If you do not own a set, ask the owner for matana al menat lehachzir (gift on condition of return) before you wave.
• In the Diaspora: the same lakhem ownership requirement applies on both Day 1 and Day 2 — you cannot simply borrow a synagogue set; it must be given to you as matana al menat lehachzir.
• In the Diaspora from the third day onward (the first day of Chol HaMoed): you may borrow a shared or synagogue set without a formal gift — the rabbinic continuation no longer carries the Torah ownership requirement (Peninei Halakha, Laws of Sukkot 13:13).
• In Israel on Chol HaMoed: borrowing without a gift is permitted — ownership was required only on the first day.
    """.trimIndent()

    private val ARBA_MINIM_FEMALE_TEMPLATE = """
Arba Minim (ארבעה מינים) — the Four Species — are taken each day of Sukkot (except Shabbat).

The four:
• Lulav — closed palm branch (at least 3 tefachim)
• Etrog — citron, beautiful and mostly intact (pitom if present should be intact)
• Hadasim — three myrtle branches
• Aravot — two willow branches

How to observe (everyone):
• Assemble the lulav: Bind the lulav, hadassim, and aravot together (per your hoshanah holder / koisan). Ensure the central spine of the lulav extends upward at least one handbreadth (tefach — around 4 inches) higher than the tops of the myrtle and willow branches (Shulchan Arukh O.C. 650:1).
• Check kashrut before Yom Tov — especially etrog and that leaves are fresh enough.
• When: Daytime; many do it before Shacharit at home or in shul. Not on Shabbat.
• ${'$'}daysNote

If a species is missing or invalid, ask your rabbi — there are limited substitutions in pressing cases.

Women — recommended mitzvah (not obligatory):
• Women are exempt from this time-bound commandment, but participating is highly popular in both Ashkenazi and Sephardi communities. This checklist marks it as recommended — not a strict daily obligation like men's first-day mitzvah.

Do women need their own set?
• No. Most women use a shared family set (usually owned by a husband or father) or a synagogue set.

The ownership rule (for men):
• In Israel: on the first day of Sukkot only, a man must own the set he waves (matana al menat lehachzir if needed). In the Diaspora, lakhem applies on Day 1 and Day 2; borrowing without a gift is permitted from the first day of Chol HaMoed (Day 3) onward.

Bracha:
• ${'$'}brachaLine

How to wave:
• ${'$'}waveLine
• Hold etrog in left, lulav (with hadassim and aravot) in right — or together per your siddur.

Each day of Sukkot (except Shabbat) you may fulfill this recommended mitzvah again.
    """.trimIndent()

    fun arbaMinimTemplate(female: Boolean): String =
        if (female) ARBA_MINIM_FEMALE_TEMPLATE else ARBA_MINIM_MEN_TEMPLATE

    fun arbaMinimArgs(profile: UserProfile, female: Boolean): Map<String, String> {
        val daysNote = if (profile.isInIsrael) ARBA_MINIM_DAYS_ISRAEL else ARBA_MINIM_DAYS_DIASPORA
        if (!female) {
            return mapOf(
                "daysNote" to daysNote,
                "menWave" to arbaMinimMenWaveLine(profile),
            )
        }
        val nusach = profile.effectiveNusach()
        val brachaLine = when (nusach) {
            EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                "Sephardi / Edot HaMizrach women: Generally wave the lulav without reciting the bracha (still a meaningful mitzvah)."
            EffectiveNusach.ASHKENAZ, EffectiveNusach.CHABAD ->
                "Ashkenazi women (and many Chabad women who follow this custom): Say Al netilat lulav, then wave in the same directions as men."
            EffectiveNusach.OTHER ->
                "Women's bracha practice for lulav varies by community — follow your minhag and rav."
        }
        val waveLine = when (nusach) {
            EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
                "Sephardi / Edot HaMizrach women: A simple shake of the lulav is sufficient — not required to wave in all six directions."
            EffectiveNusach.CHABAD ->
                "Chabad women: Wave right (south), left (north), forward (east), up, down, back (west) — the same Arizal sequence as men."
            EffectiveNusach.ASHKENAZ ->
                "Ashkenazi women: Wave east, south, west, north, up, down — the same as men."
            EffectiveNusach.OTHER ->
                "Wave directions and style vary by community — follow your siddur and minhag."
        }
        return mapOf(
            "daysNote" to daysNote,
            "brachaLine" to brachaLine,
            "waveLine" to waveLine,
        )
    }

    private fun arbaMinimMenWaveLine(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.CHABAD ->
            "Hold lulav in right hand, etrog in left with pitom down (oketz up) for the bracha. After Al netilat lulav, invert the etrog so pitom faces up, then wave three times in each direction: right (south), left (north), forward (east), up, down, back (west) — the Arizal sequence when facing east."
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
            "Hold lulav and etrog together with pitom down for the bracha. After the bracha, invert the etrog (pitom up), then wave right (south), left (north), forward (east), up, down, back (west) — the Arizal sequence when facing east. Hoshanot on Hoshana Raba in many kehillot."
        EffectiveNusach.ASHKENAZ ->
            "Hold lulav in right, etrog in left with pitom down for the bracha. After the bracha, turn the etrog pitom-up, then wave east, south, west, north, up, down (often two waves per direction)."
        EffectiveNusach.OTHER ->
            "Hold the lulav and etrog for the bracha per your siddur, then wave in the directions your community uses. Wave order and details vary — follow your minhag."
    }

    // ── Chol HaMoed ────────────────────────────────────────────────────────────

    private val CHOL_HAMOED_HONOR_TEMPLATE = """
Chol HaMoed (חול המועד) — the intermediate festival days — is not full Yom Tov, but it is not ordinary weekdays either.
${'$'}hoshanaRabaBlock

Spirit of the day:
• Simchat moed — joy of the festival; nicer meals, family time, Torah learning.
• Melacha — many labors are restricted (not as strict as Yom Tov). What is permitted depends on your situation — ask your rav.
• Food preparation for the day's meals is generally permitted (this is not the Yom Tov category of ochel nefesh — Chol HaMoed has its own, lighter rules).

What to do:
${'$'}hallelBlock
• Avoid treating it like a regular workday — schedule outings, learning, and visits that fit the moed.
${'$'}festivalLines

Work and restrictions:
• Avoid unnecessary work and home projects — many melachot are restricted on Chol HaMoed. Ask your rav what is permitted.
• Laundry: you are generally permitted only spot cleaning; ask your rav if you run out of clean clothes.
• Earning for the festival: if you lack money for the holiday, work to earn what you need may be permitted — ask your rav.
• Grooming: Shaving and haircuts are prohibited on Chol HaMoed (Shulchan Arukh O.C. 531) — except under very specific circumstances, such as one leaving prison (ask your rav).
    """.trimIndent()

    fun cholHamoedHonorTemplate(): String = CHOL_HAMOED_HONOR_TEMPLATE

    fun cholHamoedHonorArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val isPesach = "chol_hamoed_pesach" in cal.activeSeasons
        val isSukkot = "chol_hamoed_sukkot" in cal.activeSeasons
        val hallelBlock = buildString {
            appendLine("Davening — Yaaleh V'yavo and Hallel:")
            if (isSukkot) {
                appendLine("• Sukkot (every day of the festival, including Chol HaMoed): Full Hallel.")
            }
            if (isPesach) {
                if (profile.isInIsrael) {
                    appendLine(
                        "• Pesach Chol HaMoed: Half Hallel only (Full Hallel on the first day of Pesach; Half Hallel from the second day onward, including Chol HaMoed and the last days). Hallel blessings: Ashkenazic custom permits a blessing over Partial Hallel; Sephardic custom strictly prohibits it (Shulchan Arukh O.C. 422:2).",
                    )
                } else {
                    appendLine(
                        "• Pesach Chol HaMoed: Half Hallel only (Full Hallel on the first two days of Yom Tov; Half Hallel from Chol HaMoed onward, including the final Yom Tov days). Hallel blessings: Ashkenazic custom permits a blessing over Partial Hallel; Sephardic custom strictly prohibits it (Shulchan Arukh O.C. 422:2).",
                    )
                }
            }
        }.trim()
        val festivalLines = buildString {
            if (isPesach) appendLine("• On Pesach Chol HaMoed: no chametz; matzah and KP food only.")
            if (isSukkot) appendLine("• On Sukkot Chol HaMoed: lulav (not on Shabbat), meals in sukkah when applicable.")
        }.trim()
        val hoshanaRabaBlock = if (
            isSukkot &&
            cal.hebrewMonth == HebrewCalendarEngine.TISHREI &&
            cal.hebrewDay == 20
        ) {
            """

Hoshana Raba prep (tomorrow — 21 Tishrei):
• Buy or prepare a dedicated bundle of five fresh willow branches (aravot) for tomorrow morning — separate from your daily lulav set.
• At the conclusion of synagogue services tomorrow, many communities beat aravot against the ground (often about five times in Ashkenaz and Chabad custom; form varies — follow your siddur). Hoshana Raba is the final sealing of judgment from Yom Kippur."""
        } else {
            ""
        }
        return mapOf(
            "hallelBlock" to hallelBlock,
            "festivalLines" to festivalLines,
            "hoshanaRabaBlock" to hoshanaRabaBlock,
        )
    }

    private val CHOL_HAMOED_WINE_TEMPLATE = """
Drinking wine on Chol HaMoed is part of simchat ha'moed — rejoicing on the festival (Peninei Halakha 12-10-03). The Talmud teaches that there is no joy without wine; men especially mark simchat Yom Tov with wine, and everyone should enjoy nicer meals than on weekdays.

This is not like the four cups at the Seder or Shabbat-day Kiddush — Torah-level obligations where many communities require a stricter revi'it (~5.1 fl oz / 150 ml per Chazon Ish). For this optional Chol HaMoed custom, many poskim encourage at least a lenient revi'it of wine (~2.9 fl oz / 86 ml per Rav Chaim Na'eh; Peninei Halakha) at a festive Chol HaMoed meal. Confirm with your rav.

Wine vs grape juice (a real debate — ask your rav):
• Strict view: Many authorities (e.g. Rabbeinu Ephraim, Shulchan Aruch HaRav, Rav Elyashiv; Peninei Halakha 12-01-09) hold that simcha requires wine's intoxicating, joy-bringing quality — so grape juice does not fulfill this mitzvah the same way.
• Lenient view: Others (e.g. Rav Shlomo Zalman Auerbach, Rav Yaakov Kamenetsky) treat grape juice like wine for this purpose — drinking a revi'it of grape juice can fulfill the mitzvah per their psak.

If you do not drink wine:
• Fulfilling the mitzvah of festival joy (simchat ha'moed) is traditionally linked to wine and meat. If you avoid alcohol and grape products entirely, focus on meat, clothing, and other festive delicacies to fulfill the mitzvah of rejoicing.

How:
• Ideally with bread and enjoyable food at a Chol HaMoed meal — Peninei Halakha encourages two proper meals each day when possible, but bread and extra food are not strict obligations on Chol HaMoed.
${'$'}kashrutLine
• Each day of the moed counts separately.

If you avoid alcohol for health or preference, ask your rav which option fits you: grape juice per the lenient view, or emphasizing meat and festive food.
    """.trimIndent()

    fun cholHamoedWineTemplate(): String = CHOL_HAMOED_WINE_TEMPLATE

    fun cholHamoedWineArgs(cal: DayInfo): Map<String, String> {
        val isPesach = "chol_hamoed_pesach" in cal.activeSeasons
        val isSukkot = "chol_hamoed_sukkot" in cal.activeSeasons
        val kashrutLine = when {
            isPesach -> "• Use kosher-for-Passover wine only."
            isSukkot -> "• Use wine that meets your year-round kashrut needs."
            else -> "• Use wine that meets your festival kashrut needs."
        }
        return mapOf("kashrutLine" to kashrutLine)
    }

    private val CHOL_HAMOED_SUKKAH_TEMPLATE = """
Dwelling in the sukkah (סוכה) — eating bread and mezonot meals there — is a central mitzvah of Sukkot. Tradition teaches that the Shechinah (Divine Presence) rests in a kosher sukkah; we leave our permanent homes to dwell in a temporary booth, trusting in Hashem.

How to observe:
• Meals involving bread or significant baked goods (mezonot) must be eaten in the sukkah. Men recite leishev baSukkah when eating a kebeitzah (approx. 2 fl oz / the volume of an egg) or more of bread or cake.
• Spend meaningful time beyond meals: learning, singing, resting — the more you "live" in the sukkah, the more you fulfill the spirit of the mitzvah.
• Decorate the sukkah — fruits, lights, children's art — to increase simcha (joy).
• Welcome guests and invoke the Ushpizin — mystical "guests" (Abraham, Isaac, Jacob, and others) invited into the sukkah each night in Kabbalistic custom.

Rain or heavy discomfort may exempt you from eating in the sukkah — ask your rav. On Shabbat during Chol HaMoed, lulav is not taken but sukkah meals still apply when not raining.
    """.trimIndent()

    private val CHOL_HAMOED_SUKKAH_TEMPLATE_FEMALE = """
Dwelling in the sukkah (סוכה) is a cherished Sukkot mitzvah. Tradition teaches that the Shechinah (Divine Presence) rests in a kosher sukkah — a temporary booth that recalls the clouds of glory in the desert.

For women: eating meals in the sukkah is a recommended mitzvah but not obligatory like it is for men.
${'$'}leishevLine

How to observe:
• Meals involving bread or significant baked goods (mezonot) should be eaten in the sukkah when you can. The blessing leishev baSukkah applies when eating a kebeitzah (approx. 2 fl oz / the volume of an egg) or more of bread or cake.
• Spend time learning, singing, and resting there, not only a quick bite.
• Help decorate the sukkah and welcome guests; many families invite the Ushpizin — mystical "guests" (Abraham, Isaac, Jacob, and others) — each night.
• Rain or heavy discomfort may exempt you — follow halacha for your situation.
    """.trimIndent()

    fun cholHamoedSukkahTemplate(female: Boolean): String =
        if (female) CHOL_HAMOED_SUKKAH_TEMPLATE_FEMALE else CHOL_HAMOED_SUKKAH_TEMPLATE

    fun cholHamoedSukkahArgs(profile: UserProfile, female: Boolean): Map<String, String> =
        if (!female) emptyMap() else mapOf("leishevLine" to womenLeishevBasukkahLine(profile))

    private fun womenLeishevBasukkahLine(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
            "Sephardic women generally eat bread and mezonot meals in the sukkah without reciting leishev baSukkah."
        EffectiveNusach.ASHKENAZ, EffectiveNusach.CHABAD ->
            "Ashkenazi women who eat bread in the sukkah say leishev baSukkah."
        EffectiveNusach.OTHER ->
            "Whether women recite leishev baSukkah varies by community — follow your minhag."
    }

    private fun womenFirstNightLeishevNote(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.SEFARD, EffectiveNusach.EDOT_HAMIZRACH ->
            "Sephardic women generally do not recite leishev baSukkah."
        EffectiveNusach.ASHKENAZ, EffectiveNusach.CHABAD ->
            "Ashkenazi women who eat bread in the sukkah say leishev baSukkah."
        EffectiveNusach.OTHER ->
            "Whether women recite leishev baSukkah varies by community — follow your minhag."
    }

    // ── Festival week prep ───────────────────────────────────────────────────────

    private val SHAVUOT_WEEK_PREP_TEMPLATE = """
The week before Shavuot is for practical preparation — Shavuot celebrates Matan Torah (receiving the Torah at Sinai).

Food & home:
• Dairy is a cherished Shavuot minhag (cheesecake, blintzes, lasagna, ice cream) — plan menus and shop early. A festive meat meal with wine remains standard practice to fulfill the primary mitzvah of Simchat Yom Tov (Shulchan Arukh O.C. 529:2); many families have a dairy kiddush or snack, then a full meat Yom Tov meal.
• Some decorate with flowers and greenery (minhag of a garden around Sinai).
• Stock wine, grape juice, and Yom Tov staples for festive meals${'$'}diasporaFoodNote.

Torah & tefillah:
• Confirm shul times for Maariv, Shacharit, and Musaf; many communities have all-night learning (Tikkun Leil Shavuot) — find a program or plan a home study session.
• Choose texts to learn: Ruth (read on Shavuot), Megillat Rut customs, Pirkei Avot, or a topic your family enjoys.
• Review Akdamut / Yizkor customs if your community observes them on the second day (Diaspora).

Joy & family (simchat Yom Tov):
• Plan festive meat meals with wine — the core Simchat Yom Tov obligation — alongside any dairy minhag.
• Gifts for wife and children (clothes, treats) l'fi mamono — associate the day with joy.

Practical:
• On Erev Shavuot: daytime preparations can continue up until nightfall (tzeit), as the holiday begins later than usual. Reheating and cooking for the night meal may also be done on the holiday itself after tzeit, strictly from a pre-existing flame. Set up a blech or hot plate if needed. You cannot light candles or make Kiddush until tzeit.
• Turn off devices before Yom Tov — this app is for prep, not use on chag.
    """.trimIndent()

    private val SUKKOT_WEEK_PREP_TEMPLATE = """
The week before Sukkot (Tishrei 8–13) is for building joy and getting the mitzvot ready — many begin the sukkah right after Yom Kippur.

Sukkah:
• Build or repair your sukkah — walls, frame, and schach (plant covering, not metal or solid roof).
• Choose a spot with more shade than sun under the schach; avoid blocking by trees or porch roofs.
• Set up table, chairs, and decorations; plan to eat (and sleep, when possible) in the sukkah all seven days.

Arba Minim (Four Species):
• Order or buy a kosher set: lulav, etrog, hadassim (myrtle), aravot (willow).
• Check the etrog (pitom intact if present) and that leaves are fresh — buy early before stores sell out.
• Get a lulav holder (hoshanah holder / koisan) and an etrog box.

Meals & Yom Tov:
• Plan menus for seven days of festive meals in and out of the sukkah.
• Wine, grape juice, challah, and Yom Tov groceries — ${'$'}yomTovDaysNote.
• First night: Kiddush and bread in the sukkah; men say leishev basukkah before eating bread. ${'$'}womenLeishevNote If it rains on the first night: Ashkenaz — wait up to one hour, then eat a kezayit in the sukkah without leishev basukkah and finish indoors (Rema O.C. 639:5); Sephardic — if rain spoils the meal, eat the full meal indoors from the start (Shulchan Arukh), but many Sephardic decisors (including Yalkut Yosef) hold it is proper at the end of dinner to walk out to the sukkah and eat a kezayit of bread without leishev basukkah.

Joy & family:
• Plan festive meals, guests, and children in the sukkah.
• Gifts for wife and children l'fi mamono — simchat Yom Tov.
• Simchat Yom Tov — plan treats for children, festive clothing, and meals that bring household joy.

Practical:
• Hoshana Raba prep (21 Tishrei): Buy or prepare a dedicated bundle of five fresh willow branches (aravot) for Hoshana Raba morning — separate from your daily lulav set. At the conclusion of synagogue services, many communities beat aravot against the ground (often about five times in Ashkenaz and Chabad custom; form varies — follow your siddur).
• Confirm shul times for Hallel and Hoshanot throughout Sukkot, and for hakafot on Hoshana Raba.
• Turn off devices before Yom Tov — this app is for prep, not use on chag.
    """.trimIndent()

    private val PESACH_WEEK_PREP_TEMPLATE = """
Pesach begins in about a week — use this time for practical preparation (not spring cleaning every closet, but removing chametz where it matters).

Focus areas:
• Kitchen and dining — where chametz is eaten: counters, stove, microwave, toaster, fridge, pantry.
• Cars, offices, bags, coat pockets — crumbs and snack wrappers.
• Sell or use up chametz food you will not keep; plan Pesach menus and shopping.
• Haggadahs, matzah, wine, seder plate — order before prices rise and stores sell out.

Kashering vessels for Pesach:
• Year-round pots, pans, dishes, and utensils absorb chametz from hot foods and long use. For Pesach you need either kashered vessels or a separate Pesach set.
• Common methods (each item has rules — ask your rabbi): hagalah (immersion in boiling water), irui (pouring boiling water on surfaces), libun (heat for metal that cannot be kashered by hagalah).
• Start early — kashering the whole kitchen takes time. Many families use only their Pesach dishes and keep year-round dishes sealed away for the week.

Pesach dishes vs selling chametz dishes:
• Unpack or buy a dedicated Pesach set; label milchig/fleishig if needed.
• Many people include the chametz residue and absorbed flavor within their year-round dishes and pots in the sale, locking them away securely. The physical dishes themselves are not sold, avoiding the halachic requirement to re-immerse them in a Mikveh (tevilat kelim) after Pesach (Shulchan Arukh Y.D. 120). Rabbis structure the contract accordingly — list items clearly and do not use sealed dishes until buy-back.

Mechirat chametz (plan this week):
• Authorize the sale with your rabbi or community several days before Erev Pesach — most rabbis stop accepting sale forms by the night before Erev Pesach, even though the sale takes effect on Erev Pesach morning. Many accept online forms through your local rabbi or kashrut council. The sale must be valid halachically; follow your rabbi's instructions on what to include and when it takes effect.
• Selling transfers ownership so chametz you are not destroying can sit sealed in your home without belonging to you during Pesach.

After Pesach — chametz owned by a Jew during Pesach:
• Chametz that belonged to a Jew over Pesach becomes forbidden (chametz she'avar alav haPesach) — you may not own it, eat it, or otherwise benefit from it even after Pesach ends.
• That is why the sale must be real, and why you must not use sold chametz during Pesach. Ask your rav about repurchase timing and about store-bought chametz after Pesach if you are unsure.

Halachic mindset:
• You need not make the home sterile — remove edible chametz and clean places where chametz was brought.
• Bedikat chametz and biur happen on Erev Pesach — this week sets you up to succeed.

Children & family: assign zones, check school bags, plan chametz finish-up meals before Pesach.
${'$'}shabbatSchedule
    """.trimIndent()

    fun shavuotWeekPrepTemplate(): String = SHAVUOT_WEEK_PREP_TEMPLATE

    fun shavuotWeekPrepArgs(profile: UserProfile): Map<String, String> = mapOf(
        "diasporaFoodNote" to if (!profile.isInIsrael) {
            " — in the Diaspora, prepare for two days of Yom Tov"
        } else {
            ""
        },
    )

    fun sukkotWeekPrepTemplate(): String = SUKKOT_WEEK_PREP_TEMPLATE

    fun sukkotWeekPrepArgs(profile: UserProfile): Map<String, String> = mapOf(
        "yomTovDaysNote" to if (profile.isInIsrael) {
            "one day of Yom Tov at the start"
        } else {
            "two days of Yom Tov at the start in the Diaspora"
        },
        "womenLeishevNote" to womenFirstNightLeishevNote(profile),
    )

    fun pesachWeekPrepTemplate(): String = PESACH_WEEK_PREP_TEMPLATE

    fun pesachWeekPrepArgs(cal: DayInfo, profile: UserProfile): Map<String, String> {
        val shabbatSchedule = buildString {
            ErevPesachPrepText.erevPesachShabbatScheduleBlock(cal)?.let { append(it) }
            YomTovShabbatPrepText.scheduleBlock(cal, profile, "Pesach")?.let {
                if (isNotEmpty()) append("\n\n")
                append(it)
            }
        }
        return mapOf("shabbatSchedule" to shabbatSchedule.let { if (it.isNotEmpty()) "\n\n$it" else "" })
    }

    // ── Rosh Chodesh / nusach suffix ───────────────────────────────────────────

    fun withNusachNote(template: String, nusachNote: String): String =
        if (nusachNote.isBlank()) template else template + nusachNoteSlot

    fun nusachNoteArgs(nusachNote: String): Map<String, String> =
        mapOf("nusachNote" to nusachNote)

    fun tomorrowCal(cal: DayInfo, profile: UserProfile): DayInfo =
        calendarService.dayInfoForDate(cal.date.plus(1, DateTimeUnit.DAY), profile)
}
