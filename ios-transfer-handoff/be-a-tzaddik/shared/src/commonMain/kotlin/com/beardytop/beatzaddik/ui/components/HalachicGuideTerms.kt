package com.beardytop.beatzaddik.ui.components

import com.beardytop.beatzaddik.domain.HalachicTerm
import com.beardytop.beatzaddik.ui.screens.GuideTopic
import com.beardytop.beatzaddik.ui.screens.ShabbatGuideData

/** Shabbat Guide glossary topics as tappable term definitions. */
object HalachicGuideTerms {
    val terms: List<HalachicTerm> = ShabbatGuideData.glossary.map(::fromGuideTopic)

    fun termById(id: String): HalachicTerm? = terms.find { it.id == id }

    private fun fromGuideTopic(topic: GuideTopic): HalachicTerm {
        val short = topic.body.lines().firstOrNull { it.isNotBlank() }?.take(420)?.trim().orEmpty()
        val labels = buildList {
            add(topic.title)
            when (topic.id) {
                "shabbat_overview" -> addAll(listOf("Shabbat", "Shabbos"))
                "candle_lighting" -> addAll(listOf("Shabbat candles", "candle lighting"))
                "hallel" -> addAll(listOf("Hallel", "Full Hallel", "Half Hallel"))
                "yaaleh_vyavo" -> addAll(listOf("Yaaleh V'yavo", "Yaaleh V'Yavo", "V'yavo"))
                "rosh_chodesh" -> add("Rosh Chodesh")
                "yom_tov" -> addAll(listOf("Yom Tov", "Jewish Festivals"))
                "tashlumin" -> addAll(listOf("Tashlumin", "tashlumin", "Makeup Prayers"))
            }
            if (topic.title.contains("—")) {
                add(topic.title.substringAfter("—").trim())
            }
        }.filter { it.isNotBlank() }.distinctBy { it.lowercase() }
        return HalachicTerm(
            id = "guide_${topic.id}",
            title = labels.first(),
            definition = if (short.length < topic.body.length) "$short…" else short,
            matchLabels = labels,
        )
    }
}
