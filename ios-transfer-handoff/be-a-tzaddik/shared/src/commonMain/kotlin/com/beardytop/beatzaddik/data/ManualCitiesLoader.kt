package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.ManualCity
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class ManualCitiesFile(
    val version: Int = 1,
    val cities: List<ManualCityJson> = emptyList(),
)

@Serializable
private data class ManualCityJson(
    val id: String,
    val label: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneId: String,
)

object ManualCitiesLoader {
    private val json = Json { ignoreUnknownKeys = true }

    fun load(rawJson: String): List<ManualCity> {
        val file = json.decodeFromString<ManualCitiesFile>(rawJson)
        val seen = mutableSetOf<String>()
        return file.cities.map { row ->
            require(row.id !in seen) { "Duplicate manual city id: ${row.id}" }
            seen += row.id
            ManualCity(
                id = row.id,
                label = row.label,
                latitude = row.latitude,
                longitude = row.longitude,
                timezoneId = row.timezoneId,
            )
        }
    }
}
