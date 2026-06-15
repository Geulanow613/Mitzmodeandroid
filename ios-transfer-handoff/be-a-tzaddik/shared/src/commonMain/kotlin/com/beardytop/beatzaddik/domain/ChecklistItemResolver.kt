package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.data.ChecklistLoader

object ChecklistItemResolver {

    fun resolve(
        item: ChecklistItemDef,
        profile: UserProfile,
        checked: Boolean,
        nowMillis: Long,
        zmanim: ZmanimSnapshot?,
        prayerDay: PrayerDayContext,
        upcomingShabbatParsha: String? = null
    ): ResolvedChecklistItem {
        val nusach = profile.effectiveNusach()
        val nusachTag = item.nusachTag ?: nusach.displayName()
        val titleSuffix = if (item.nusachOnly) " · $nusachTag" else ""

        // For weekly mitzvot (Shnayim Mikra), append the parsha name to the title
        // and inject a Chabad link for that parsha.
        val parshaInfo = if (item.weeklyMitzvah) ParshaData.forKey(upcomingShabbatParsha) else null
        val parshaTitle = parshaInfo?.let { " — Parshat ${it.displayName}" } ?: ""

        val baseLink = ChecklistLoader.pickLink(item, nusach)
        val resolvedLink = if (parshaInfo != null) {
            when (nusach) {
                EffectiveNusach.CHABAD -> ChecklistLink(
                    displayText = "Read Parshat ${parshaInfo.displayName} on Chabad",
                    url = parshaInfo.chabadUrl
                )
                else -> ChecklistLink(
                    displayText = "Sefaria — Parshat ${parshaInfo.displayName}",
                    url = parshaInfo.sefariaUrl
                )
            }
        } else baseLink

        val explanation = pickExplanation(item, profile).ifBlank {
            if (item.id.startsWith("custom_")) {
                "This is a personal goal or reminder you added. Check it off when you've done it."
            } else {
                "Confirm details with your rabbi."
            }
        }
        // Prepend parsha name to the explanation for weekly items.
        val displayExplanation = if (parshaInfo != null) {
            "This week's parsha: Parshat ${parshaInfo.displayName}\n\n$explanation"
        } else explanation

        val zman = when {
            ChecklistZmanEvaluator.appliesTo(item.id) ->
                ChecklistZmanEvaluator.evaluate(item.id, nowMillis, zmanim, prayerDay)
            !item.persistChecked && item.timeOfDay != TimeOfDay.ANY ->
                timeOfDayAvailability(item.timeOfDay, nowMillis, zmanim)
            else -> ItemZmanStatus()
        }
        val resourceLinks = buildResourceLinks(item, parshaInfo, nusach)

        return ResolvedChecklistItem(
            def = item,
            checked = checked,
            displayTitle = item.title + titleSuffix + parshaTitle,
            displayExplanation = displayExplanation,
            learnMoreUrl = resolvedLink?.url?.takeIf { isUsefulLink(it) },
            learnMoreLabel = resolvedLink?.displayText?.takeIf { resolvedLink.url.let { isUsefulLink(it) } },
            resourceLinks = resourceLinks,
            sectionLabel = sectionWithNusach(item.section, item, profile),
            zmanAvailability = zman.availability,
            zmanHint = zman.hint,
            zmanMakeupNote = zman.makeupNote,
            zmanWindowStartMillis = zman.windowStartMillis,
            zmanWindowEndMillis = zman.windowEndMillis,
            zmanAvailableAtLabel = zman.availableAtLabel
        )
    }

    private fun pickExplanation(item: ChecklistItemDef, profile: UserProfile): String {
        if (profile.gender == Gender.FEMALE && item.explanationFemale.isNotBlank()) {
            return item.explanationFemale
        }
        val nusachSpecific = when (profile.effectiveNusach()) {
            EffectiveNusach.ASHKENAZ -> item.explanationAshkenaz
            EffectiveNusach.SEFARD -> item.explanationSefard
            EffectiveNusach.EDOT_HAMIZRACH -> item.explanationEdotHamizrach.ifBlank {
                item.explanationSefard
            }
            EffectiveNusach.CHABAD -> item.explanationChabad
        }
        val base = item.explanation
        if (nusachSpecific.isNotBlank() && base.isNotBlank()) {
            return "$base\n\n$nusachSpecific"
        }
        if (nusachSpecific.isNotBlank()) return nusachSpecific
        if (base.isNotBlank()) return base
        return "This mitzvah is part of your daily checklist. Ask your rabbi for guidance on how to fulfill it in your situation."
    }

    /**
     * For non-prayer items, derive availability from [timeOfDay] and zmanim so that
     * daily items are locked (EXPIRED) once their window closes and show as dimmed-but-readable.
     */
    private fun timeOfDayAvailability(
        timeOfDay: TimeOfDay,
        nowMillis: Long,
        zmanim: ZmanimSnapshot?
    ): ItemZmanStatus {
        val z = zmanim ?: return ItemZmanStatus()
        if (!z.hasLocationTimes) return ItemZmanStatus()
        return when (timeOfDay) {
            TimeOfDay.DAY -> {
                val start = z.alotHaShacharMillis ?: z.sunriseMillis ?: return ItemZmanStatus()
                val end = ZmanPeriodLogic.afternoonStartMillis(z) ?: z.sunsetMillis ?: return ItemZmanStatus()
                when {
                    nowMillis < start -> ItemZmanStatus(
                        availability = ItemZmanAvailability.UPCOMING,
                        windowStartMillis = start,
                        availableAtLabel = "dawn"
                    )
                    nowMillis >= end -> ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED
                    )
                    else -> ItemZmanStatus()
                }
            }
            TimeOfDay.AFTERNOON -> {
                val start = ZmanPeriodLogic.afternoonStartMillis(z) ?: return ItemZmanStatus()
                val end = z.sunsetMillis ?: return ItemZmanStatus()
                when {
                    nowMillis < start -> ItemZmanStatus(
                        availability = ItemZmanAvailability.UPCOMING,
                        windowStartMillis = start,
                        availableAtLabel = "Mincha Gedola"
                    )
                    nowMillis >= end -> ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED
                    )
                    else -> ItemZmanStatus()
                }
            }
            TimeOfDay.NIGHT -> {
                val start = ZmanPeriodLogic.effectiveEveningStart(nowMillis, z)
                    ?: return ItemZmanStatus()
                val end = ZmanPeriodLogic.effectiveEveningEnd(nowMillis, z)
                    ?: return ItemZmanStatus()
                when {
                    nowMillis < start -> ItemZmanStatus(
                        availability = ItemZmanAvailability.UPCOMING,
                        windowStartMillis = start,
                        availableAtLabel = "nightfall"
                    )
                    nowMillis >= end -> ItemZmanStatus(
                        availability = ItemZmanAvailability.EXPIRED
                    )
                    else -> ItemZmanStatus()
                }
            }
            else -> ItemZmanStatus()
        }
    }

    private fun buildResourceLinks(
        item: ChecklistItemDef,
        parshaInfo: ParshaData.ParshaInfo?,
        nusach: EffectiveNusach,
    ): List<ChecklistLink> {
        val static = item.links
            .filter { isUsefulLink(it.url) && !isGenericTorahIndex(it.url) }
        if (parshaInfo == null) {
            return static.distinctBy { it.url.trim().lowercase() }
        }
        val weekly = buildList {
            add(
                ChecklistLink(
                    displayText = "Sefaria — Parshat ${parshaInfo.displayName}",
                    url = parshaInfo.sefariaUrl
                )
            )
            if (nusach == EffectiveNusach.CHABAD) {
                add(
                    ChecklistLink(
                        displayText = "Chabad — Parshat ${parshaInfo.displayName}",
                        url = parshaInfo.chabadUrl
                    )
                )
            }
            addAll(static)
        }
        return weekly.distinctBy { it.url.trim().lowercase() }
    }

    /** Generic Torah index pages are not useful for a specific weekly portion. */
    private fun isGenericTorahIndex(url: String): Boolean {
        val normalized = url.trimEnd('/').lowercase()
        return normalized.endsWith("sefaria.org/texts/torah")
    }

    /** Skip bare homepages that don't teach anything on their own. */
    private fun isUsefulLink(url: String): Boolean {
        val bare = url.trimEnd('/').lowercase()
        return bare !in setOf(
            "https://www.sefaria.org",
            "https://www.chabad.org",
            "https://www.chabad.org/library/article_cdo/aid/4687/jewish/Shabbat.htm"
        )
    }

    private fun sectionWithNusach(section: String, item: ChecklistItemDef, profile: UserProfile): String {
        if (item.nusachOnly) return normalizeSection(section)
        if (section.startsWith("Prayer")) {
            return "Prayer (${profile.effectiveNusach().displayName()})"
        }
        return normalizeSection(section)
    }

    /** Legacy JSON used "Shabbat" for anything mentioning Shabbat in the title. */
    private fun normalizeSection(section: String): String =
        if (section == "Shabbat") "Shabbat & Festivals" else section

    private fun EffectiveNusach.displayName() = displayLabel()
}
