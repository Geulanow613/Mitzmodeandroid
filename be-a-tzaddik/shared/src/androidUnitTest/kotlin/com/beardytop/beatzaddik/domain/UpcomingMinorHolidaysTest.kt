package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.ui.screens.ShabbatGuideData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UpcomingMinorHolidaysTest {

    private val mountainView = UserProfile(
        timezoneId = "America/Los_Angeles",
        latitude = 37.3861,
        longitude = -122.0839,
        locationLabel = "Mountain View",
    )

    private val backend = createJewishCalendarBackend()

    @Test
    fun erevTishaBeAv_showsBothTishaBeAvAndTuBeAv() {
        val date = LocalDate(2026, 7, 22)
        val info = backend.dayInfoAt(millis(date, 9), mountainView)
        assertEquals(8, info.hebrewDay)
        assertEquals(HebrewCalendarEngine.AV, info.hebrewMonth)
        assertTrue("erev_tisha_beav" in info.activeSeasons)

        val holidays = JewishCalendarService(backend).upcomingHolidays(
            nowEpochMillis = millis(date, 9),
            profile = mountainView,
        )
        val names = holidays.map { it.name }
        assertTrue("Tisha B'Av" in names, "expected Tisha B'Av, got $names")
        assertTrue("Tu B'Av" in names, "expected Tu B'Av, got $names")

        val tuBeav = holidays.first { it.name == "Tu B'Av" }
        assertTrue(tuBeav.daysAway in 5..7)

        val topic = ShabbatGuideData.guideTopicForUpcoming("Tu B'Av", tuBeav.hint)
        assertEquals("tu_beav", topic.id)
        assertTrue(topic.body.contains("Mishnah (Taanit 4:8)"))
    }

    @Test
    fun erevPurimWeek_fastOfEstherOnceAndBeforePurim() {
        // Friday 27 Feb 2026 = 10 Adar 5786 (Day of Esther scenario / erev Shabbat morning).
        val date = LocalDate(2026, 2, 27)
        val holidays = JewishCalendarService(backend).upcomingHolidays(
            nowEpochMillis = millis(date, 9),
            profile = mountainView,
        )
        val names = holidays.map { it.name }
        val fastNames = names.filter { it.startsWith("Fast of Esther") }
        assertEquals(1, fastNames.size, "expected one Fast of Esther, got $names")

        val fastIdx = names.indexOfFirst { it.startsWith("Fast of Esther") }
        val purimIdx = names.indexOfFirst { it.contains("Purim") && !it.contains("Katan") }
        assertTrue(fastIdx >= 0 && purimIdx >= 0, "expected fast and Purim in $names")
        assertTrue(
            fastIdx < purimIdx,
            "Fast of Esther should list before Purim when both are imminent; got $names",
        )
        assertEquals(holidays[fastIdx].daysAway, holidays[purimIdx].daysAway)
    }

    @Test
    fun commemoractiveMinors_mapToLongGuideExplainers() {
        val expected = mapOf(
            "Tu B'Shvat" to "tu_bshvat",
            "Pesach Sheni" to "pesach_sheni",
            "Lag BaOmer" to "lag_baomer",
            "Tu B'Av" to "tu_beav",
        )
        expected.forEach { (name, id) ->
            val topic = assertNotNull(ShabbatGuideData.guideTopicForUpcoming(name))
            assertEquals(id, topic.id)
            assertTrue(topic.body.length > 200, "$name should use the long explainer")
        }
    }

    private fun millis(date: LocalDate, hour: Int): Long =
        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, hour, 0)
            .toInstant(TimeZone.of(mountainView.timezoneId))
            .toEpochMilliseconds()
}
