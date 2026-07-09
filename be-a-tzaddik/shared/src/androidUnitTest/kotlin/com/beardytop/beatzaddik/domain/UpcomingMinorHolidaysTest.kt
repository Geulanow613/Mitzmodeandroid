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
