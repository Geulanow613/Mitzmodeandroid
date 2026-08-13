package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.domain.zmanim.SharedZmanimBuilder
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChecklistItemResolverTest {

    @Test
    fun cholHamoedItemsStayActiveBeforeDawn() {
        val tz = "America/New_York"
        val date = LocalDate(2026, 4, 15)
        val fiveAm = LocalDateTime(2026, 4, 15, 5, 7)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(fiveAm, tz, 40.7128, -74.0060)
        val item = ChecklistItemDef(
            id = "chol_hamoed_honor",
            title = "Honor Chol HaMoed",
            section = "Chol HaMoed",
            timeOfDay = TimeOfDay.DAY,
            required = false,
            situational = false,
            seasons = listOf("chol_hamoed_pesach"),
        )
        val resolved = ChecklistItemResolver.resolve(
            item = item,
            profile = UserProfile(
                timezoneId = tz,
                latitude = 40.7128,
                longitude = -74.0060,
                locationLabel = "New York",
            ),
            checked = false,
            nowMillis = fiveAm,
            zmanim = zmanim,
            prayerDay = PrayerDayContext.from(
                DayInfo(
                    date = date,
                    civilLabel = "",
                    hebrewLabel = "",
                    parsha = null,
                    statusChips = emptyList(),
                    isShabbat = false,
                    isErevShabbat = false,
                    isYomTov = false,
                    isShabbatOrYomTov = false,
                    activeTimeOfDay = TimeOfDay.NIGHT,
                    activePeriodLabel = "Night",
                    inactivePeriodHint = null,
                    activeSeasons = setOf("chol_hamoed_pesach", "pesach"),
                    hebrewYear = 5786,
                    hebrewMonth = HebrewCalendarEngine.NISSAN,
                    hebrewDay = 17,
                    zmanim = zmanim,
                ),
                EffectiveNusach.ASHKENAZ,
                40.7128,
            ),
        )
        assertEquals(ItemZmanAvailability.ACTIVE, resolved.zmanAvailability)
    }

    @Test
    fun minchaItemsUnlockAtMinchaGedolaNotMidday() {
        val tz = "America/New_York"
        val tenAm = LocalDateTime(2026, 4, 15, 10, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(tenAm, tz, 40.7128, -74.0060)
        val minchaGedola = requireNotNull(zmanim.minchaGedolaMillis)
        val chatzos = requireNotNull(zmanim.chatzosMillis)
        assertTrue(chatzos < minchaGedola, "Mincha Gedola is after halachic midday (chatzos)")

        val item = ChecklistItemDef(
            id = "mincha_shemoneh_esrei_tachanun",
            title = "Mincha (Afternoon Prayer)",
            section = "Afternoon Prayer",
            timeOfDay = TimeOfDay.AFTERNOON,
            required = true,
            situational = false,
        )
        val resolved = ChecklistItemResolver.resolve(
            item = item,
            profile = UserProfile(
                timezoneId = tz,
                latitude = 40.7128,
                longitude = -74.0060,
                locationLabel = "New York",
            ),
            checked = false,
            nowMillis = tenAm,
            zmanim = zmanim,
            prayerDay = PrayerDayContext.from(
                DayInfo(
                    date = LocalDate(2026, 4, 15),
                    civilLabel = "",
                    hebrewLabel = "",
                    parsha = null,
                    statusChips = emptyList(),
                    isShabbat = false,
                    isErevShabbat = false,
                    isYomTov = false,
                    isShabbatOrYomTov = false,
                    activeTimeOfDay = TimeOfDay.DAY,
                    activePeriodLabel = "Day",
                    inactivePeriodHint = null,
                    activeSeasons = emptySet(),
                    hebrewYear = 5786,
                    hebrewMonth = HebrewCalendarEngine.NISSAN,
                    hebrewDay = 17,
                    zmanim = zmanim,
                ),
                EffectiveNusach.ASHKENAZ,
                40.7128,
            ),
        )
        assertEquals(ItemZmanAvailability.UPCOMING, resolved.zmanAvailability)
        assertEquals(minchaGedola, resolved.zmanWindowStartMillis)
        assertEquals("Mincha Gedola", resolved.zmanAvailableAtLabel)
    }

    @Test
    fun shacharitPartsShareDawnWindowBeforeAlot() {
        val tz = "America/New_York"
        val fiveAm = LocalDateTime(2026, 4, 15, 3, 30)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(fiveAm, tz, 40.7128, -74.0060)
        val dawn = requireNotNull(zmanim.alotHaShacharMillis)
        assertTrue(fiveAm < dawn, "fixture should be before dawn")

        val prayerDay = PrayerDayContext.from(
            DayInfo(
                date = LocalDate(2026, 4, 15),
                civilLabel = "",
                hebrewLabel = "",
                parsha = null,
                statusChips = emptyList(),
                isShabbat = false,
                isErevShabbat = false,
                isYomTov = false,
                isShabbatOrYomTov = false,
                activeTimeOfDay = TimeOfDay.NIGHT,
                activePeriodLabel = "Night",
                inactivePeriodHint = null,
                activeSeasons = emptySet(),
                hebrewYear = 5786,
                hebrewMonth = HebrewCalendarEngine.NISSAN,
                hebrewDay = 17,
                zmanim = zmanim,
            ),
            EffectiveNusach.ASHKENAZ,
            40.7128,
        )
        val profile = UserProfile(
            timezoneId = tz,
            latitude = 40.7128,
            longitude = -74.0060,
            locationLabel = "New York",
        )

        fun resolve(id: String, title: String) = ChecklistItemResolver.resolve(
            item = ChecklistItemDef(
                id = id,
                title = title,
                section = "Morning Prayer (Shacharit)",
                timeOfDay = TimeOfDay.DAY,
                required = true,
                situational = false,
            ),
            profile = profile,
            checked = false,
            nowMillis = fiveAm,
            zmanim = zmanim,
            prayerDay = prayerDay,
        )

        val korbanot = resolve("ashkenaz_korbanot_before_shacharit", "Korbanot")
        val pesukei = resolve("minimum_pesukei_d_zimra", "Pesukei DeZimra")
        val shema = resolve("morning_shema_with_its_blessings", "Shema")

        assertEquals(ItemZmanAvailability.UPCOMING, korbanot.zmanAvailability)
        assertEquals(ItemZmanAvailability.UPCOMING, pesukei.zmanAvailability)
        assertEquals(ItemZmanAvailability.UPCOMING, shema.zmanAvailability)
        assertEquals(dawn, korbanot.zmanWindowStartMillis)
        assertEquals(dawn, pesukei.zmanWindowStartMillis)
        assertEquals(dawn, shema.zmanWindowStartMillis)
    }

    @Test
    fun torahStudyDay_staysActiveUntilSunset_notMinchaGedola() {
        val tz = "Asia/Jerusalem"
        val lat = 31.7683
        val lon = 35.2137
        val fourPm = LocalDateTime(2026, 7, 2, 16, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val zmanim = SharedZmanimBuilder.buildForLocation(fourPm, tz, lat, lon)
        val minchaGedola = requireNotNull(zmanim.minchaGedolaMillis)
        val sunset = requireNotNull(zmanim.sunsetMillis)
        assertTrue(fourPm > minchaGedola, "4pm should be after Mincha Gedola")
        assertTrue(fourPm < sunset, "4pm should be before sunset")

        val item = ChecklistItemDef(
            id = "torah_study_day",
            title = "Talmud Torah — daytime (Daily Torah Study)",
            section = "Torah Study",
            timeOfDay = TimeOfDay.DAY,
            required = true,
            situational = false,
            gender = "male",
        )
        val resolved = ChecklistItemResolver.resolve(
            item = item,
            profile = UserProfile(
                timezoneId = tz,
                latitude = lat,
                longitude = lon,
                locationLabel = "Jerusalem",
            ),
            checked = false,
            nowMillis = fourPm,
            zmanim = zmanim,
            prayerDay = PrayerDayContext.from(
                DayInfo(
                    date = LocalDate(2026, 7, 2),
                    civilLabel = "",
                    hebrewLabel = "",
                    parsha = null,
                    statusChips = emptyList(),
                    isShabbat = false,
                    isErevShabbat = false,
                    isYomTov = false,
                    isShabbatOrYomTov = false,
                    activeTimeOfDay = TimeOfDay.AFTERNOON,
                    activePeriodLabel = "Afternoon",
                    inactivePeriodHint = null,
                    activeSeasons = emptySet(),
                    hebrewYear = 5786,
                    hebrewMonth = HebrewCalendarEngine.TAMMUZ,
                    hebrewDay = 16,
                    zmanim = zmanim,
                ),
                EffectiveNusach.ASHKENAZ,
                lat,
            ),
        )
        assertEquals(ItemZmanAvailability.ACTIVE, resolved.zmanAvailability)
    }
}
