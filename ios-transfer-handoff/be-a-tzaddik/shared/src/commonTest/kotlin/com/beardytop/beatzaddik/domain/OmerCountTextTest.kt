package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.domain.zmanim.SharedZmanimBuilder
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OmerCountTextTest {

    @Test
    fun tonightCountUsesNextDayNotCurrentDay() {
        val nusach = EffectiveNusach.ASHKENAZ
        val args = OmerCountText.explanationArgs(sampleCal(omerDay = 3), sampleProfile())
        assertEquals(OmerCountText.omerDaySummary(3, nusach), args["todaySummary"])
        assertEquals(OmerCountText.omerDaySummary(4, nusach), args["tonightSummary"])
        assertEquals(
            "Today is ${OmerCountText.omerDaySummary(4, nusach)}.",
            args["speechPhrase"],
        )
        val filled = ExplainerTemplateFill.fill(OmerCountText.explanationTemplate(), args)
        assertTrue(filled.contains("count ${OmerCountText.omerDaySummary(4, nusach)} after nightfall"))
        assertTrue(!filled.contains("count ${OmerCountText.omerDaySummary(3, nusach)} after nightfall"))
    }

    @Test
    fun tzeitForOmerIsEveningNotDawn() {
        val tz = "America/New_York"
        val date = LocalDate(2026, 6, 15)
        val noon = LocalDateTime(2026, 6, 15, 12, 0).toInstant(TimeZone.of(tz)).toEpochMilliseconds()
        val z = SharedZmanimBuilder.buildForLocation(noon, tz, 40.7128, -74.0060)
        val tzeit = assertNotNull(OmerCountText.omerNightfallMillis(z))
        val dawn = assertNotNull(z.alotHaShacharMillis)
        val formattedTzeit = assertNotNull(ZmanimFormatter.formatTime(tzeit, tz))
        assertTrue(dawn < tzeit, "dawn should be before evening tzeit")
        assertTrue(
            formattedTzeit.contains("PM") || formattedTzeit.startsWith("19:") || formattedTzeit.startsWith("20:"),
            "expected evening tzeit, got $formattedTzeit",
        )
    }

    @Test
    fun omerNightfallRejectsDawnMillis() {
        val tz = "America/New_York"
        val date = LocalDate(2026, 6, 15)
        val noon = LocalDateTime(2026, 6, 15, 12, 0).toInstant(TimeZone.of(tz)).toEpochMilliseconds()
        val z = SharedZmanimBuilder.buildForLocation(noon, tz, 40.7128, -74.0060)
        val dawn = assertNotNull(z.nightObligationsEndMillis)
        val corrupted = z.copy(tzeitMillis = dawn)
        val fixed = assertNotNull(OmerCountText.omerNightfallMillis(corrupted))
        val sunset = assertNotNull(z.sunsetMillis)
        assertTrue(fixed > sunset)
        assertTrue(fixed < dawn)
    }

    private fun sampleProfile() = UserProfile(
        timezoneId = "America/New_York",
        latitude = 40.7128,
        longitude = -74.0060,
        locationLabel = "New York",
    )

    private fun sampleCal(omerDay: Int): DayInfo {
        val tz = "America/New_York"
        val date = LocalDate(2026, 6, 15)
        val noon = LocalDateTime(2026, 6, 15, 12, 0).toInstant(TimeZone.of(tz)).toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(noon, tz, 40.7128, -74.0060)
        return DayInfo(
            date = date,
            civilLabel = "Monday, June 15, 2026",
            hebrewLabel = "1 Tammuz 5786",
            parsha = null,
            statusChips = emptyList(),
            isShabbat = false,
            isErevShabbat = false,
            isYomTov = false,
            isShabbatOrYomTov = false,
            activeTimeOfDay = TimeOfDay.DAY,
            activePeriodLabel = "Daytime",
            inactivePeriodHint = null,
            omerDay = omerDay,
            isSefiratHaomer = true,
            zmanim = zmanim,
        )
    }
}
