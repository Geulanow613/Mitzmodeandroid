package com.beardytop.beatzaddik.domain

import kotlinx.serialization.Serializable

@Serializable
data class GeneratorMitzvah(
    val id: String,
    val text: String,
    val links: List<GeneratorMitzvahLink> = emptyList(),
)

@Serializable
data class GeneratorMitzvahLink(
    val displayText: String,
    val url: String,
)

@Serializable
data class GeneratorMitzvotList(
    val mitzvot: List<GeneratorMitzvah> = emptyList(),
    val version: Int = 1,
)
