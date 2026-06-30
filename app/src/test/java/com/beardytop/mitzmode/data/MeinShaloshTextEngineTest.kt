package com.beardytop.mitzmode.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MeinShaloshTextEngineTest {

    @Test
    fun mezonotOnly_hebrew_containsMichyaPhrases() {
        val text = MeinShaloshTextEngine.build(
            MeinShaloshSelection(hasMezonot = true),
            MeinShaloshLanguage.HEBREW
        )
        assertTrue(text.contains("עַל הַמִּחְיָה וְעַל הַכַּלְכָּלָה"))
        assertFalse(text.contains("עַל הַגֶּפֶן"))
        assertTrue(text.contains("עַל הָאָרֶץ וְעַל הַמִּחְיָה"))
    }

    @Test
    fun wineOnly_english_flowsAsOneParagraph() {
        val text = MeinShaloshTextEngine.build(
            MeinShaloshSelection(hasWine = true),
            MeinShaloshLanguage.ENGLISH
        )
        assertTrue(text.startsWith("Blessed are You"))
        assertTrue(text.contains("for the vine and the fruit of the vine"))
        assertTrue(text.contains("for the land and for the vine and the fruit of the vine"))
        assertTrue(text.contains("the vine and the fruit of the vine"))
        assertFalse(text.contains("sustenance"))
    }

    @Test
    fun allThree_english_usesOxfordComma() {
        val joined = MeinShaloshTextEngine.joinEnglishList(
            listOf("the sustenance and the nourishment", "the vine and the fruit of the vine", "the tree and the fruit of the tree")
        )
        assertTrue(joined.contains(", and the tree"))
    }

    @Test
    fun fruitOnly_hebrew_fullTreePhraseInThanksAndClosing() {
        val text = MeinShaloshTextEngine.build(
            MeinShaloshSelection(hasFruit = true),
            MeinShaloshLanguage.HEBREW
        )
        val full = "עַל הָעֵץ וְעַל פְּרִי הָעֵץ"
        assertTrue(text.contains(full))
        // Thanks and closing must use the full phrase, not עַל הָעֵץ alone
        assertFalse(text.contains("וְנוֹדֶה לְךָ עַל הָאָרֶץ וְעַל הָעֵץ."))
        assertTrue(text.contains("וְנוֹדֶה לְךָ עַל הָאָרֶץ וְ$full"))
        assertTrue(text.contains("בָּרוּךְ אַתָּה ה' עַל הָאָרֶץ וְ$full"))
    }

    @Test
    fun roshChodesh_insertsYaalehVeyavo() {
        val text = MeinShaloshTextEngine.build(
            MeinShaloshSelection(hasFruit = true, isRoshChodesh = true),
            MeinShaloshLanguage.HEBREW
        )
        assertTrue(text.contains("יַעֲלֶה וְיָבֹא"))
        assertTrue(text.contains("רֹאשׁ חֹדֶשׁ"))
    }

    @Test
    fun pesach_insertsYaalehVeyavo() {
        val text = MeinShaloshTextEngine.build(
            MeinShaloshSelection(hasWine = true, isPesach = true),
            MeinShaloshLanguage.HEBREW
        )
        assertTrue(text.contains("יַעֲלֶה וְיָבֹא"))
        assertTrue(text.contains("חַג הַמַּצּוֹת"))
    }

    @Test
    fun sukkot_insertsYaalehVeyavo() {
        val text = MeinShaloshTextEngine.build(
            MeinShaloshSelection(hasWine = true, isSukkot = true),
            MeinShaloshLanguage.ENGLISH
        )
        assertTrue(text.contains("Festival of Sukkot"))
    }
}
