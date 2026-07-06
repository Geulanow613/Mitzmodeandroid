package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.ManualCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ManualCitiesCatalog {
    private var cities: List<ManualCity> = emptyList()

    val all: List<ManualCity> get() = cities

    fun initialize(catalog: List<ManualCity>) {
        cities = catalog
    }

    suspend fun loadFromAssets() {
        if (cities.isNotEmpty()) return
        withContext(Dispatchers.Default) {
            if (cities.isNotEmpty()) return@withContext
            val raw = loadManualCitiesJson()
            cities = ManualCitiesLoader.load(raw)
        }
    }
}
