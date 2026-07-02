package com.beardytop.mitzmode.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceAppLanguageTest {

    @Test
    fun spanishPhone_startsInSpanishWithTranslationOn() {
        val choice = DeviceAppLanguage.resolveFromDeviceLanguageTags(listOf("es", "en"))
        assertEquals("es", choice.languageCode)
        assertTrue(choice.translationEnabled)
    }

    @Test
    fun hebrewPhone_startsInHebrewWithTranslationOn() {
        val choice = DeviceAppLanguage.resolveFromDeviceLanguageTags(listOf("he"))
        assertEquals("he", choice.languageCode)
        assertTrue(choice.translationEnabled)
    }

    @Test
    fun legacyHebrewIwCode_mapsToHebrew() {
        assertEquals("he", DeviceAppLanguage.matchBundledLanguage("iw"))
    }

    @Test
    fun germanPhone_fallsBackToEnglish() {
        val choice = DeviceAppLanguage.resolveFromDeviceLanguageTags(listOf("de", "en"))
        assertEquals("en", choice.languageCode)
        assertFalse(choice.translationEnabled)
    }

    @Test
    fun unknownPhone_fallsBackToEnglish() {
        val choice = DeviceAppLanguage.resolveFromDeviceLanguageTags(listOf("pt", "ja"))
        assertEquals("en", choice.languageCode)
        assertFalse(choice.translationEnabled)
    }

    @Test
    fun englishPhone_startsInEnglish() {
        val choice = DeviceAppLanguage.resolveFromDeviceLanguageTags(listOf("en-US"))
        assertEquals("en", choice.languageCode)
        assertFalse(choice.translationEnabled)
    }

    @Test
    fun regionSuffixIgnored() {
        val choice = DeviceAppLanguage.resolveFromDeviceLanguageTags(listOf("fr-CA"))
        assertEquals("fr", choice.languageCode)
        assertTrue(choice.translationEnabled)
    }
}
