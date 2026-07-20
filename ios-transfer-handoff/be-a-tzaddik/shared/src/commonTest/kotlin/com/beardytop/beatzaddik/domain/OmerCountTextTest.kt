package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.domain.zmanim.SharedZmanimBuilder
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OmerCountTextTest {

    @Test
    fun tonightCountUsesNextDayNotCurrentDay() {
        val nusach = EffectiveNusach.ASHKENAZ
        val cal = sampleCal(omerDay = 3)
        val noon = LocalDateTime(2026, 6, 15, 12, 0)
            .toInstant(TimeZone.of("America/New_York")).toEpochMilliseconds()
        val args = OmerCountText.explanationArgs(cal, sampleProfile(), noon)
        assertEquals(OmerCountText.omerDaySummary(3, nusach), args["todaySummary"])
        assertEquals(OmerCountText.omerDaySummary(4, nusach), args["tonightSummary"])
        // Daytime: speech line is the day it currently is (last night's count), not tonight's.
        assertEquals(
            "Today is ${OmerCountText.omerDaySummary(3, nusach)}.",
            args["speechPhrase"],
        )
        val filled = ExplainerTemplateFill.fill(OmerCountText.explanationTemplate(), args)
        assertTrue(filled.contains("count ${OmerCountText.omerDaySummary(4, nusach)} after nightfall"))
        assertTrue(filled.contains("you should have counted ${OmerCountText.omerDaySummary(3, nusach)}"))
        assertTrue(filled.contains("If you forgot at night:"))
        assertTrue(filled.contains("without the blessing (no bracha)"))
    }

    @Test
    fun afterTzeitSpeechPhraseIsTonightCount() {
        val nusach = EffectiveNusach.ASHKENAZ
        val cal = sampleCal(omerDay = 4, startedTonightAtTzeit = true)
        val tenPm = LocalDateTime(2026, 6, 14, 22, 0)
            .toInstant(TimeZone.of("America/New_York")).toEpochMilliseconds()
        val args = OmerCountText.explanationArgs(cal, sampleProfile(), tenPm)
        assertEquals(OmerCountText.omerDaySummary(3, nusach), args["todaySummary"])
        assertEquals(OmerCountText.omerDaySummary(4, nusach), args["tonightSummary"])
        assertEquals(
            "Today is ${OmerCountText.omerDaySummary(4, nusach)}.",
            args["speechPhrase"],
        )
    }

    @Test
    fun checklistTitleMentionsLastNightAndTonight() {
        val cal = sampleCal(omerDay = 5) // daytime: last night was 5, tonight counts 6
        val noon = LocalDateTime(2026, 6, 15, 12, 0)
            .toInstant(TimeZone.of("America/New_York")).toEpochMilliseconds()
        val title = OmerCountText.buildTitle(cal, EffectiveNusach.ASHKENAZ, noon)
        assertTrue(title.contains("was day 5"), title)
        assertTrue(title.contains("count day 6"), title)
    }

    @Test
    fun checklistTitleDay49DaytimeSaysCountingComplete() {
        val cal = sampleCal(omerDay = 49, startedTonightAtTzeit = false)
        val noon = LocalDateTime(2026, 6, 15, 12, 0)
            .toInstant(TimeZone.of("America/New_York")).toEpochMilliseconds()
        val title = OmerCountText.buildTitle(cal, EffectiveNusach.ASHKENAZ, noon)
        assertTrue(title.contains("complete"), title)
        assertTrue(title.contains("day 49"), title)
        assertFalse(title.contains("count day 50"), title)
    }

    @Test
    fun omerCountStaysActiveDuringDaytime() {
        val tz = "America/New_York"
        val date = LocalDate(2026, 6, 15)
        val noon = LocalDateTime(2026, 6, 15, 12, 0).toInstant(TimeZone.of(tz)).toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(noon, tz, 40.7128, -74.0060)
        val item = ChecklistItemDef(
            id = OmerCountText.CHECKLIST_ITEM_ID,
            title = OmerCountText.buildTitle(12, EffectiveNusach.ASHKENAZ),
            section = "Sefirat HaOmer",
            timeOfDay = TimeOfDay.ANY,
            required = true,
            tzeitMitzvah = true,
            seasons = listOf("sefirah"),
        )
        val resolved = ChecklistItemResolver.resolve(
            item = item,
            profile = sampleProfile(),
            checked = false,
            nowMillis = noon,
            zmanim = zmanim,
            prayerDay = PrayerDayContext.from(
                sampleCal(omerDay = 12),
                EffectiveNusach.ASHKENAZ,
                40.7128,
            ),
        )
        assertEquals(ItemZmanAvailability.ACTIVE, resolved.zmanAvailability)
    }

    @Test
    fun omerCountResetsAtNextTzeit() {
        val tz = "America/New_York"
        val date = LocalDate(2026, 6, 15)
        val noon = LocalDateTime(2026, 6, 15, 12, 0).toInstant(TimeZone.of(tz)).toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(noon, tz, 40.7128, -74.0060)
        val yesterdayNoon = LocalDateTime(2026, 6, 14, 12, 0).toInstant(TimeZone.of(tz)).toEpochMilliseconds()
        val yesterdayZmanim = SharedZmanimBuilder.buildForLocation(yesterdayNoon, tz, 40.7128, -74.0060)
        val cal = sampleCal(omerDay = 12).copy(zmanim = zmanim)
        val yesterday = cal.copy(date = date.plus(-1, DateTimeUnit.DAY), zmanim = yesterdayZmanim)
        val storedKey = assertNotNull(TzeitDay.currentKey(noon, cal, yesterday))
        val tzeit = requireNotNull(OmerCountText.omerNightfallMillis(zmanim))
        val afterTzeit = tzeit + 60_000L
        val newKey = assertNotNull(TzeitDay.currentKey(afterTzeit, cal, yesterday))
        assertTrue(storedKey != newKey)

        fun showsChecked(nowMillis: Long): Boolean {
            val key = TzeitDay.currentKey(nowMillis, cal, yesterday)
            return key != null && storedKey == key
        }

        assertTrue(showsChecked(noon))
        assertFalse(showsChecked(afterTzeit))
    }

    @Test
    fun omerCountPriorityAfterTzeitNotBefore() {
        val tz = "America/New_York"
        val date = LocalDate(2026, 6, 15)
        val noon = LocalDateTime(2026, 6, 15, 12, 0).toInstant(TimeZone.of(tz)).toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(noon, tz, 40.7128, -74.0060)
        val cal = sampleCal(omerDay = 12).copy(zmanim = zmanim)
        val sunset = requireNotNull(zmanim.sunsetMillis)
        val tzeit = requireNotNull(OmerCountText.omerNightfallMillis(zmanim))

        assertFalse(OmerCountText.isOmerCountPriority(noon, cal))
        assertFalse(OmerCountText.isOmerCountPriority(sunset + 60_000L, cal))
        assertTrue(OmerCountText.isOmerCountPriority(tzeit + 60_000L, cal))
        assertTrue(
            "Sefirat HaOmer" in ChecklistSectionOrder.prioritizedPrepSections(
                cal = cal,
                tomorrowCal = cal,
                profile = sampleProfile(),
                nowMillis = tzeit + 60_000L,
            )
        )
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

    private fun sampleCal(omerDay: Int, startedTonightAtTzeit: Boolean = false): DayInfo {
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
            startedTonightAtTzeit = startedTonightAtTzeit,
            zmanim = zmanim,
        )
    }
}
