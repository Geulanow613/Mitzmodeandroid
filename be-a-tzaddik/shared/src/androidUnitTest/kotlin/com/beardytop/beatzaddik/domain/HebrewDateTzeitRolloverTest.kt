package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * The Hebrew day begins at nightfall (tzeit), not civil midnight.
 *
 * Anchor case (user-reported): 17 Tammuz 5786 = Thursday July 2, 2026 in Jerusalem.
 * Tzeit (8.5°) is ~8:30 PM; after that it is halachically 18 Tammuz and the fast is over.
 */
class HebrewDateTzeitRolloverTest {

    private val jerusalem = UserProfile(
        timezoneId = "Asia/Jerusalem",
        latitude = 31.7683,
        longitude = 35.2137,
        locationLabel = "Jerusalem, Israel",
        manualCityId = "jlm",
    )

    private val backend = createJewishCalendarBackend()

    @Test
    fun fastDay_afternoon_is17Tammuz_withFastActive() {
        val info = dayInfo(LocalDate(2026, 7, 2), hour = 14)
        assertEquals(17, info.hebrewDay)
        assertEquals(HebrewCalendarEngine.TAMMUZ, info.hebrewMonth)
        assertTrue("fast_day" in info.activeSeasons)
        assertEquals(HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ, info.fastDayIndex)
        assertFalse(info.startedTonightAtTzeit)
    }

    @Test
    fun fastDay_10pm_afterTzeit_is18Tammuz_fastOver() {
        val info = dayInfo(LocalDate(2026, 7, 2), hour = 22)
        assertEquals(18, info.hebrewDay)
        assertEquals(HebrewCalendarEngine.TAMMUZ, info.hebrewMonth)
        assertFalse("fast_day" in info.activeSeasons)
        assertNull(info.fastDayIndex)
        assertTrue(info.startedTonightAtTzeit)
        assertTrue(info.hebrewLabel.startsWith("18 Tammuz"))
        // Civil date shown to the user is still Thursday July 2.
        assertTrue(info.civilLabel.contains("July 2"), "civilLabel was ${info.civilLabel}")
        assertEquals(TimeOfDay.NIGHT, info.activeTimeOfDay)
    }

    @Test
    fun erevFast_night_afterTzeit_is17Tammuz_fastUpcomingAtDawn() {
        // Wednesday July 1 at 10 PM — halachically 17 Tammuz has begun; fast starts at dawn.
        val info = dayInfo(LocalDate(2026, 7, 1), hour = 22)
        assertEquals(17, info.hebrewDay)
        assertTrue("fast_day" in info.activeSeasons)
        assertEquals(HebrewCalendarEngine.SEVENTEEN_OF_TAMMUZ, info.fastDayIndex)
        assertTrue(info.startedTonightAtTzeit)
        // The zmanim describe the fast's civil daytime (Thursday): dawn ~4:10 AM, tzeit ~8:30 PM.
        val dawn = info.zmanim?.alotHaShacharMillis
        assertNotNull(dawn)
        assertTrue(dawn > millis(LocalDate(2026, 7, 1), 22))
    }

    @Test
    fun beforeTzeit_evening_stillSameHebrewDay() {
        // 7 PM is after sunset but before tzeit — the Hebrew date must not advance yet.
        val info = dayInfo(LocalDate(2026, 7, 2), hour = 19)
        assertEquals(17, info.hebrewDay)
        assertFalse(info.startedTonightAtTzeit)
    }

    @Test
    fun noLocation_noTzeit_fallsBackToCivilDay() {
        val noLocation = UserProfile(timezoneId = "Asia/Jerusalem")
        val info = backend.dayInfoAt(millis(LocalDate(2026, 7, 2), 22), noLocation)
        assertEquals(17, info.hebrewDay)
        assertFalse(info.startedTonightAtTzeit)
    }

    @Test
    fun fridayNight_afterTzeit_becomesShabbat() {
        // Friday July 3, 2026 at 10 PM Jerusalem — halachic Shabbat (and civil Saturday's Hebrew date).
        val info = dayInfo(LocalDate(2026, 7, 3), hour = 22)
        assertTrue(info.isShabbat)
        assertTrue(info.startedTonightAtTzeit)
        assertFalse(info.isErevShabbat)
    }

    @Test
    fun thursdayNight_afterTzeit_isNotErevShabbat() {
        // Thursday July 2, 2026 after tzeit — Hebrew Friday has begun, but tonight is Motzei→Friday,
        // not erev Shabbat night (Shabbat starts Friday night).
        val info = dayInfo(LocalDate(2026, 7, 2), hour = 22)
        assertTrue(info.startedTonightAtTzeit)
        assertFalse(info.isErevShabbat)
        assertFalse(TonightHolyDayRules.tonightBeginsShabbat(info))
        assertNull(MaarivInAppRules.blockedTonightLabel(info, null, inIsrael = true))
    }

    @Test
    fun saturdayNight_afterTzeit_isMotzeiShabbat_notShabbat() {
        val info = dayInfo(LocalDate(2026, 7, 4), hour = 22)
        assertFalse(info.isShabbat)
        assertTrue(info.startedTonightAtTzeit)
        assertFalse(HolyDayPhoneRules.shouldHideChecklist(jerusalem, info, millis(LocalDate(2026, 7, 4), 22)))
    }

    private fun dayInfo(date: LocalDate, hour: Int): DayInfo =
        backend.dayInfoAt(millis(date, hour), jerusalem)

    private fun millis(date: LocalDate, hour: Int): Long =
        LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, hour, 0)
            .toInstant(TimeZone.of(jerusalem.timezoneId))
            .toEpochMilliseconds()
}
