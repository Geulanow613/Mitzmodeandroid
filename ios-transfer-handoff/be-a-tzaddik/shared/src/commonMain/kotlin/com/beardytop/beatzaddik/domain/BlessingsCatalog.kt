package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.domain.liturgy.BirkatHamazonText
import com.beardytop.beatzaddik.domain.liturgy.BrachotData
import com.beardytop.beatzaddik.domain.liturgy.TefilatHaderechData

data class BlessingsHubItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val body: String,
)

object BlessingsCatalog {
    val items: List<BlessingsHubItem> = listOf(
        BlessingsHubItem(
            id = "birkat_hamazon",
            title = "Birkat Hamazon",
            subtitle = "Grace after meals",
            body = BirkatHamazonText.sections.joinToString("\n\n") { section ->
                buildString {
                    append(section.title)
                    append("\n\n")
                    append(section.hebrew.trim())
                    section.english?.let {
                        append("\n\n")
                        append(it.trim())
                    }
                }
            },
        ),
        BlessingsHubItem(
            id = "tefilat_haderech",
            title = "Traveler's Prayer",
            subtitle = "Tefilat Haderech",
            body = TefilatHaderechData.sections.joinToString("\n\n") { section ->
                buildString {
                    append(section.title)
                    append("\n\n")
                    append(section.hebrew.trim())
                    section.english?.let {
                        append("\n\n")
                        append(it.trim())
                    }
                }
            },
        ),
        BlessingsHubItem(
            id = "brachot",
            title = "Blessings",
            subtitle = "Everyday brachot",
            body = BrachotData.sections.joinToString("\n\n") { section ->
                buildString {
                    append(section.title)
                    append("\n\n")
                    append(section.hebrew.trim())
                    section.english?.let {
                        append("\n\n")
                        append(it.trim())
                    }
                }
            },
        ),
    )
}
