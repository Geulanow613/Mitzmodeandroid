package com.beardytop.beatzaddik.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsraelCustomsOverrideTest {

    @Test
    fun locationInIsrael_defaultsToIsraelCustoms() {
        val profile = UserProfile(
            latitude = 31.77,
            longitude = 35.23,
            timezoneId = "Asia/Jerusalem",
        )
        assertTrue(profile.locationSuggestsIsrael)
        assertTrue(profile.isInIsrael)
    }

    @Test
    fun forceChutz_overridesIsraelGps() {
        val profile = UserProfile(
            latitude = 31.77,
            longitude = 35.23,
            timezoneId = "Asia/Jerusalem",
            forceChutzLaAretzCustoms = true,
        )
        assertTrue(profile.locationSuggestsIsrael)
        assertFalse(profile.isInIsrael)
    }

    @Test
    fun outsideIsrael_canEnableIsraelCustoms() {
        val profile = UserProfile(
            latitude = 40.71,
            longitude = -74.0,
            timezoneId = "America/New_York",
            liveInIsrael = true,
        )
        assertFalse(profile.locationSuggestsIsrael)
        assertTrue(profile.isInIsrael)
    }

    @Test
    fun outsideIsrael_defaultsToChutz() {
        val profile = UserProfile(
            latitude = 40.71,
            longitude = -74.0,
            timezoneId = "America/New_York",
        )
        assertFalse(profile.locationSuggestsIsrael)
        assertFalse(profile.isInIsrael)
    }

    @Test
    fun forceChutz_blocksLiveInIsraelUntilCleared() {
        val stuck = UserProfile(
            latitude = 40.71,
            longitude = -74.0,
            timezoneId = "America/New_York",
            liveInIsrael = true,
            forceChutzLaAretzCustoms = true,
        )
        assertFalse(stuck.isInIsrael)

        val cleared = stuck.copy(forceChutzLaAretzCustoms = false)
        assertTrue(cleared.isInIsrael)
    }
}
