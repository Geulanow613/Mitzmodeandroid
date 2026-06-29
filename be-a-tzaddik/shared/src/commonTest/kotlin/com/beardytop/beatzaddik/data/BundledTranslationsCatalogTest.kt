package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.ui.translation.BundledTranslationLanguages
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BundledTranslationsCatalogTest {

  @Test
  fun bundledLanguagesAreOfflineSet() {
    assertEquals(setOf("he", "es", "fr", "ru"), BundledTranslationLanguages.codes)
  }

  @Test
  fun lookupReturnsMenuTranslation() {
    val raw = """
      {
        "version": 1,
        "language": "es",
        "entries": {
          "Settings": "Ajustes",
          "Enable Translation": "Activar traducción"
        }
      }
    """.trimIndent()
    BundledTranslationsCatalog.parseForTest(mapOf("es" to raw))
    assertEquals("Ajustes", BundledTranslationsCatalog.lookup("Settings", "es"))
    assertEquals(null, BundledTranslationsCatalog.lookup("Settings", "de"))
  }
}
