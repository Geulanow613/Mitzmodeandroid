package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.ManualCity

object ManualCitiesCatalog {
    private var cities: List<ManualCity> = emptyList()

    val all: List<ManualCity> get() = cities

    fun initialize(catalog: List<ManualCity>) {
        cities = catalog
    }

    suspend fun loadFromAssets() {
        if (cities.isNotEmpty()) return
        val raw = loadManualCitiesJson()
        cities = ManualCitiesLoader.load(raw)
    }
}
