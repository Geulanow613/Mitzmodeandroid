package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.TextEncodingFixes
import com.beardytop.beatzaddik.domain.ChecklistItemDef
import com.beardytop.beatzaddik.domain.ChecklistLink
import com.beardytop.beatzaddik.domain.EffectiveNusach
import com.beardytop.beatzaddik.domain.TimeOfDay
import com.beardytop.beatzaddik.domain.UserProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class ChecklistFile(val version: Int = 1, val items: List<ChecklistItemJson> = emptyList())

@Serializable
private data class ChecklistItemJson(
    val id: String,
    val title: String,
    val section: String,
    @SerialName("timeOfDay") val timeOfDay: String = "any",
    val required: Boolean = true,
    val situational: Boolean = false,
    val gender: String? = null,
    val married: Boolean? = null,
    val hasChildren: Boolean? = null,
    val explanation: String = "",
    val explanationFemale: String = "",
    val explanationAshkenaz: String = "",
    val explanationSefard: String = "",
    val explanationEdotHamizrach: String = "",
    val explanationChabad: String = "",
    val links: List<ChecklistLinkJson> = emptyList(),
    val hideOnShabbat: Boolean = false,
    val shabbatOnly: Boolean = false,
    val shabbatEveOnly: Boolean = false,
    val motzeiShabbatOnly: Boolean = false,
    val sortOrder: Int = 0,
    val persistChecked: Boolean = false,
    val nusach: List<String>? = null,
    val nusachOnly: Boolean = false,
    val nusachTag: String? = null,
    val seasons: List<String>? = null,
    val weeklyMitzvah: Boolean = false,
    val monthlyMitzvah: Boolean = false
)

@Serializable
private data class ChecklistLinkJson(
    val displayText: String,
    val url: String,
    val nusach: String? = null
)

object ChecklistLoader {
    private val json = Json { ignoreUnknownKeys = true }
    private var cache: List<ChecklistItemDef>? = null
    private var cacheVersion: Int = -1

    fun load(rawJson: String, extrasJson: String? = null): List<ChecklistItemDef> {
        val file = json.decodeFromString<ChecklistFile>(rawJson)
        if (cache != null && cacheVersion == file.version) return cache!!
        val main = file.items.map { it.toDef() }
        val extras = extrasJson?.let { json.decodeFromString<ChecklistFile>(it).items.map { e -> e.toDef() } }
            ?: emptyList()
        val items = main + extras
        cache = items
        cacheVersion = file.version
        return items
    }

    fun pickLink(item: ChecklistItemDef, effective: EffectiveNusach): ChecklistLink? {
        if (item.links.isEmpty()) return null
        val key = when (effective) {
            EffectiveNusach.CHABAD -> "chabad"
            EffectiveNusach.SEFARD -> "sefard"
            EffectiveNusach.EDOT_HAMIZRACH -> "sefard"
            EffectiveNusach.ASHKENAZ -> "ashkenaz"
            EffectiveNusach.OTHER -> null
        }
        if (key != null) {
            item.links.firstOrNull { it.nusach == key }?.let { return it }
        }
        return item.links.firstOrNull { it.nusach == null || it.nusach == "default" }
            ?: item.links.first()
    }

    fun defaultUrl(profile: UserProfile): String = when (profile.effectiveNusach()) {
        EffectiveNusach.CHABAD -> "https://www.chabad.org"
        EffectiveNusach.SEFARD,
        EffectiveNusach.EDOT_HAMIZRACH,
        EffectiveNusach.ASHKENAZ,
        EffectiveNusach.OTHER -> "https://www.sefaria.org"
    }

    private fun ChecklistItemJson.toDef() = ChecklistItemDef(
        id = id,
        title = TextEncodingFixes.sanitizeDisplayTitle(title),
        section = TextEncodingFixes.repairMojibake(section),
        timeOfDay = when (timeOfDay.lowercase()) {
            "day" -> TimeOfDay.DAY
            "afternoon" -> TimeOfDay.AFTERNOON
            "night" -> TimeOfDay.NIGHT
            else -> TimeOfDay.ANY
        },
        required = required,
        situational = situational,
        gender = gender,
        married = married,
        hasChildren = hasChildren,
        explanation = TextEncodingFixes.repairMojibake(explanation),
        explanationFemale = TextEncodingFixes.repairMojibake(explanationFemale),
        explanationAshkenaz = TextEncodingFixes.repairMojibake(explanationAshkenaz),
        explanationSefard = TextEncodingFixes.repairMojibake(explanationSefard),
        explanationEdotHamizrach = TextEncodingFixes.repairMojibake(explanationEdotHamizrach),
        explanationChabad = TextEncodingFixes.repairMojibake(explanationChabad),
        links = links.map {
            ChecklistLink(
                TextEncodingFixes.repairMojibake(it.displayText),
                it.url,
                it.nusach,
            )
        },
        hideOnShabbat = hideOnShabbat,
        shabbatOnly = shabbatOnly,
        shabbatEveOnly = shabbatEveOnly,
        motzeiShabbatOnly = motzeiShabbatOnly,
        sortOrder = sortOrder,
        persistChecked = persistChecked,
        nusach = nusach,
        nusachOnly = nusachOnly,
        nusachTag = nusachTag,
        seasons = seasons,
        weeklyMitzvah = weeklyMitzvah,
        monthlyMitzvah = monthlyMitzvah
    )
}
