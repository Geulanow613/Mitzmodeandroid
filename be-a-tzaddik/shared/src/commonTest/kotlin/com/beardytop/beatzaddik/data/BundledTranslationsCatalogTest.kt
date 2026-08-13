package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.ui.translation.BundledTranslationLanguages
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BundledTranslationsCatalogTest {

  @Test
  fun bundledLanguagesAreOfflineSet() {
    // Store builds ship stubs; offline catalog is gated by MitzModeFeatures.bundledOfflineTranslationsEnabled.
    assertEquals(emptySet(), BundledTranslationLanguages.codes)
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

  @Test
  fun lookupNormalizesLiturgyKeyWhitespaceAndCase() {
    val raw = """
      {
        "version": 1,
        "language": "es",
        "entries": {
          "May it be Your will, Lord our God and God of our ancestors, that You lead us toward peace, guide our footsteps toward peace, and make us reach our desired destination for life, gladness, and peace.\n                May You rescue us from the hand of every foe, ambush along the way, and from all manner of punishments that assemble to come to earth.\n                May You send blessing in our handiwork, and grant us grace, kindness, and mercy in Your eyes and in the eyes of all who see us.\n                May You hear the voice of our supplication because You are God Who hears prayer and supplication.\n                Blessed are You, Lord, Who hears prayer.": "Que sea Tu voluntad (español)",
          "Blessed are You, Lord our God, King of the universe, Who brings forth bread from the earth.": "Bendito eres Tú, pan de la tierra."
        }
      }
    """.trimIndent()
    BundledTranslationsCatalog.parseForTest(mapOf("es" to raw))
    val tefilat = """
      May it be Your will, Lord our God and God of our ancestors, that You lead us toward peace, guide our footsteps toward peace, and make us reach our desired destination for life, gladness, and peace.
      May You rescue us from the hand of every foe, ambush along the way, and from all manner of punishments that assemble to come to earth.
      May You send blessing in our handiwork, and grant us grace, kindness, and mercy in Your eyes and in the eyes of all who see us.
      May You hear the voice of our supplication because You are God Who hears prayer and supplication.
      Blessed are You, Lord, Who hears prayer.
    """.trimIndent()
    assertEquals("Que sea Tu voluntad (español)", BundledTranslationsCatalog.lookup(tefilat, "es"))
    assertEquals(
      "Bendito eres Tú, pan de la tierra.",
      BundledTranslationsCatalog.lookup(
        "Blessed are You, Lord our God, King of the universe, who brings forth bread from the earth",
        "es",
      ),
    )
  }
}
