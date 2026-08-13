package com.beardytop.beatzaddik.domain.zmanim

import com.beardytop.beatzaddik.domain.ZmanimSnapshot
import com.kosherjava.zmanim.ComplexZmanimCalendar
import com.kosherjava.zmanim.util.GeoLocation
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Test
import java.io.File
import java.util.Calendar
import java.util.TimeZone as JavaTimeZone
import kotlin.math.abs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * iOS [SharedZmanimBuilder] should match Android KosherJava for the same lat/lon/tz/date.
 * Tolerance (~90s) is test slack only — not an expected user-visible offset.
 */
class SharedZmanimParityTest {

    private data class Case(val name: String, val lat: Double, val lon: Double, val tz: String, val date: LocalDate)

    private val spotCases = listOf(
        Case("NYC summer", 40.7128, -74.0060, "America/New_York", LocalDate(2026, 7, 8)),
        Case("NYC winter", 40.7128, -74.0060, "America/New_York", LocalDate(2026, 1, 15)),
        Case("San Diego summer", 32.7157, -117.1611, "America/Los_Angeles", LocalDate(2026, 6, 15)),
        Case("Jerusalem equinox", 31.7683, 35.2137, "Asia/Jerusalem", LocalDate(2026, 3, 20)),
    )

    /** Summer + winter — catches seasonal edge cases beyond one date. */
    private val catalogDates = listOf(
        LocalDate(2026, 6, 21),
        LocalDate(2026, 12, 21),
    )

    @Test
    fun spotCasesMatchKosherJavaWithinTolerance() {
        for (c in spotCases) {
            assertParity(c.name, c.lat, c.lon, c.tz, c.date)
        }
    }

    @Test
    fun allManualCatalogCitiesMatchKosherJavaWithinTolerance() {
        val cities = loadManualCities()
        assertTrue(cities.isNotEmpty(), "manual-cities.json should list preset cities")
        for (city in cities) {
            for (date in catalogDates) {
                assertParity("${city.id}@${date}", city.latitude, city.longitude, city.timezoneId, date)
            }
        }
    }

    private fun assertParity(name: String, lat: Double, lon: Double, tz: String, date: LocalDate) {
        val toleranceMs = 90_000L
        val noon = LocalDateTime(date.year, date.monthNumber, date.dayOfMonth, 12, 0)
            .toInstant(TimeZone.of(tz))
            .toEpochMilliseconds()
        val shared = SharedZmanimBuilder.buildForLocation(noon, tz, lat, lon)
        val kj = kosherJavaSnapshot(lat, lon, tz, date)

        compareField(name, "sunrise", shared.sunriseMillis, kj.sunriseMillis, toleranceMs)
        compareField(name, "sunset", shared.sunsetMillis, kj.sunsetMillis, toleranceMs)
        compareField(name, "tzeit", shared.tzeitMillis, kj.tzeitMillis, toleranceMs)
        compareField(name, "alot", shared.alotHaShacharMillis, kj.alotHaShacharMillis, toleranceMs)
        compareField(name, "misheyakir", shared.misheyakirMillis, kj.misheyakirMillis, toleranceMs)
        compareField(name, "sofShema", shared.sofZmanShemaMillis, kj.sofZmanShemaMillis, toleranceMs)
        compareField(name, "sofTefilla", shared.sofZmanTefillaMillis, kj.sofZmanTefillaMillis, toleranceMs)
        compareField(name, "chatzos", shared.chatzosMillis, kj.chatzosMillis, toleranceMs)
        compareField(name, "minchaGedola", shared.minchaGedolaMillis, kj.minchaGedolaMillis, toleranceMs)
        compareField(name, "plag", shared.plagHaminchaMillis, kj.plagHaminchaMillis, toleranceMs)
    }

    private fun compareField(case: String, field: String, shared: Long?, kj: Long?, toleranceMs: Long) {
        if (shared == null && kj == null) return // e.g. polar day/night — no astronomical time
        val a = assertNotNull(shared, "$case $field missing in SharedZmanimBuilder (KosherJava=$kj)")
        val b = assertNotNull(kj, "$case $field missing in KosherJava (shared=$shared)")
        val delta = abs(a - b)
        assertTrue(
            delta <= toleranceMs,
            "$case $field delta ${delta}ms > ${toleranceMs}ms (shared=$a kj=$b)",
        )
    }

    private fun kosherJavaSnapshot(lat: Double, lon: Double, tzId: String, date: LocalDate): ZmanimSnapshot {
        val geo = GeoLocation("test", lat, lon, 0.0, JavaTimeZone.getTimeZone(tzId))
        val cal = Calendar.getInstance(JavaTimeZone.getTimeZone(tzId)).apply {
            set(date.year, date.monthNumber - 1, date.dayOfMonth, 12, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val zc = ComplexZmanimCalendar(geo).apply { calendar = cal }
        val tomorrow = (cal.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, 1) }
        val zcTomorrow = ComplexZmanimCalendar(geo).apply { calendar = tomorrow }

        val chatzos = zc.chatzos?.time
        return ZmanimSnapshot(
            misheyakirMillis = zc.misheyakir10Point2Degrees?.time,
            sunriseMillis = zc.sunrise?.time,
            sofZmanShemaMillis = zc.sofZmanShmaGRA?.time,
            sofZmanTefillaMillis = zc.sofZmanTfilaGRA?.time,
            chatzosMillis = chatzos,
            minchaGedolaMillis = zc.minchaGedola?.time,
            plagHaminchaMillis = zc.plagHamincha?.time,
            sunsetMillis = zc.sunset?.time,
            tzeitMillis = zc.tzaisGeonim8Point5Degrees?.time,
            alotHaShacharMillis = zc.alosHashachar?.time ?: zc.alos16Point1Degrees?.time,
            nightObligationsEndMillis = zcTomorrow.alosHashachar?.time ?: zcTomorrow.alos16Point1Degrees?.time,
            timezoneId = tzId,
        )
    }

    private fun loadManualCities(): List<ManualCityRow> {
        val file = manualCitiesFile()
        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString<ManualCitiesFile>(file.readText()).cities
    }

    private fun manualCitiesFile(): File {
        val candidates = listOf(
            File("../data/manual-cities.json"),
            File("../../data/manual-cities.json"),
            File("be-a-tzaddik/data/manual-cities.json"),
        )
        return candidates.firstOrNull { it.isFile }
            ?: error("manual-cities.json not found (tried ${candidates.joinToString { it.path }})")
    }

    @Serializable
    private data class ManualCitiesFile(val cities: List<ManualCityRow> = emptyList())

    @Serializable
    private data class ManualCityRow(
        val id: String,
        val latitude: Double,
        val longitude: Double,
        val timezoneId: String,
    )
}
