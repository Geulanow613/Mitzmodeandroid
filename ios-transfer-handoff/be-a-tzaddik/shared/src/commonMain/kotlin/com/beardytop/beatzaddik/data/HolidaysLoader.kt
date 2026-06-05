package com.beardytop.beatzaddik.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class HolidayOverlayEntry(
    val id: String,
    val name: String,
    val recurring: String? = null,
    val hebrewMonth: Int? = null,
    val hebrewDay: Int? = null,
    val prepMitzvot: List<String> = emptyList()
)

@Serializable
private data class HolidaysFile(val version: Int = 1, val upcoming: List<HolidayOverlayEntry> = emptyList())

object HolidaysLoader {
    private val json = Json { ignoreUnknownKeys = true }
    private var cache: List<HolidayOverlayEntry>? = null

    suspend fun load(): List<HolidayOverlayEntry> {
        cache?.let { return it }
        val parsed = json.decodeFromString<HolidaysFile>(loadHolidaysJson())
        cache = parsed.upcoming
        return parsed.upcoming
    }
}
