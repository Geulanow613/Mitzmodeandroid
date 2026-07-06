package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.data.ManualCitiesCatalog
import com.beardytop.beatzaddik.data.ManualCitiesLoader
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class LocationTimezoneTest {

    @Before
    fun loadCatalog() {
        val raw = manualCitiesFile().readText()
        val cities = ManualCitiesLoader.load(raw)
        ManualCitiesCatalog.initialize(cities)
        ManualCities.onCatalogLoaded(cities)
    }

    @Test
    fun mountainViewUsesPacificNotUtc() {
        // Android emulator default location (Google HQ area)
        val tz = LocationTimezone.resolve(37.3861, -122.0839, deviceFallback = "UTC")
        assertEquals("America/Los_Angeles", tz)
    }

    @Test
    fun jerusalemUsesIsraelTimezone() {
        val tz = LocationTimezone.resolve(31.7683, 35.2137, deviceFallback = "UTC")
        assertEquals("Asia/Jerusalem", tz)
    }

    @Test
    fun resolveForProfileUpdatesWrongGpsTimezone() {
        val profile = UserProfile(
            latitude = 37.3861,
            longitude = -122.0839,
            timezoneId = "UTC",
            useGps = true,
        )
        val fixed = LocationTimezone.resolveForProfile(profile, deviceFallback = "UTC")
        assertEquals("America/Los_Angeles", fixed.timezoneId)
        assertEquals(profile.latitude, fixed.latitude)
        assertEquals(profile.longitude, fixed.longitude)
    }

    @Test
    fun manualCityProfileIsUntouched() {
        val profile = UserProfile(
            latitude = 31.7683,
            longitude = 35.2137,
            timezoneId = "Asia/Jerusalem",
            useGps = false,
            manualCityId = "jlm",
        )
        assertEquals(profile, LocationTimezone.resolveForProfile(profile, deviceFallback = "UTC"))
    }

    private fun manualCitiesFile(): File {
        val candidates = listOf(
            File("src/commonMain/composeResources/files/manual-cities.json"),
            File("../data/manual-cities.json"),
            File("../../data/manual-cities.json"),
            File("be-a-tzaddik/data/manual-cities.json"),
        )
        return candidates.firstOrNull { it.isFile }
            ?: error("manual-cities.json not found (tried ${candidates.joinToString { it.path }})")
    }
}
