package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.ChecklistItemDef

object ChecklistCatalog {
    private var items: List<ChecklistItemDef> = emptyList()

    val all: List<ChecklistItemDef> get() = items

    fun titleForId(id: String): String? = items.find { it.id == id }?.title

    fun initialize(catalog: List<ChecklistItemDef>) {
        items = catalog
    }

    suspend fun loadFromAssets() {
        if (items.isNotEmpty()) return
        val main = loadChecklistJson()
        val extras = runCatching { loadNusachExtrasJson() }.getOrElse { """{"version":1,"items":[]}""" }
        items = ChecklistLoader.load(main, extras)
    }
}
