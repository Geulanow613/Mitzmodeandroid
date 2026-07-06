package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.data.ManualCitiesCatalog
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Preset cities for manual location (onboarding + settings).
 * Loaded from [composeResources/files/manual-cities.json] at app startup.
 */
object ManualCities {
    /** Older app builds used short ids; resolve them for saved profiles. */
    private val legacyIdAliases = mapOf(
        "nyc" to "new_york",
        "chi" to "chicago",
        "lon" to "london",
        "la" to "los_angeles",
        "sf" to "san_francisco",
        "jerusalem" to "jlm",
        "tel_aviv" to "tlv",
    )

    val all: List<ManualCity> get() = ManualCitiesCatalog.all

    /** Shown before the user types a search query (picker performance). */
    private val featuredCityIds = listOf(
        "jlm", "new_york", "brooklyn", "los_angeles", "chicago", "miami", "toronto", "montreal",
        "london", "paris", "melbourne", "sf", "boston", "philadelphia", "atlanta", "seattle",
        "denver", "houston", "dallas", "phoenix", "detroit", "cleveland", "baltimore", "tlv",
        "haifa", "teaneck", "lakewood", "monsey", "five_towns", "bnei_brak",
    )

    private var timezoneGrid: Map<Pair<Int, Int>, List<ManualCity>> = emptyMap()

    internal fun onCatalogLoaded(catalog: List<ManualCity>) {
        val grid = mutableMapOf<Pair<Int, Int>, MutableList<ManualCity>>()
        for (city in catalog) {
            val key = city.latitude.toInt() to city.longitude.toInt()
            grid.getOrPut(key) { mutableListOf() }.add(city)
        }
        timezoneGrid = grid
    }

    fun featuredForPicker(): List<ManualCity> =
        featuredCityIds.mapNotNull { byId(it) }

    fun byId(id: String): ManualCity? {
        val resolved = legacyIdAliases[id] ?: id
        return all.firstOrNull { it.id == resolved }
    }

    /**
     * Nearest bundled city's IANA timezone for [latitude]/[longitude].
     *
     * Only the timezone id is returned — callers must still pass actual GPS coordinates into
     * zmanim math. No distance cap: timezone regions are large; the nearest catalog point is
     * a better guess than the device/emulator clock (often UTC).
     */
    fun nearestTimezoneId(latitude: Double, longitude: Double): String? =
        nearestCity(latitude, longitude)?.timezoneId

    /** Nearest catalog city within [maxKm], or null if none close enough. */
    fun nearestCity(latitude: Double, longitude: Double, maxKm: Double = Double.MAX_VALUE): ManualCity? {
        if (all.isEmpty()) return null
        var bestCity: ManualCity? = null
        var bestDistKm = Double.MAX_VALUE
        val baseLat = latitude.toInt()
        val baseLon = longitude.toInt()
        for (dlat in -1..1) {
            for (dlon in -1..1) {
                for (city in timezoneGrid[baseLat + dlat to baseLon + dlon].orEmpty()) {
                    val distKm = haversineKm(latitude, longitude, city.latitude, city.longitude)
                    if (distKm < bestDistKm) {
                        bestDistKm = distKm
                        bestCity = city
                    }
                }
            }
        }
        if (bestCity == null || bestDistKm > maxKm) return null
        return bestCity
    }

    /** Nearest city with bundled elevation within [maxKm]. */
    fun nearestCityElevation(latitude: Double, longitude: Double, maxKm: Double): Double? {
        if (all.isEmpty()) return null
        var bestElev: Double? = null
        var bestDistKm = Double.MAX_VALUE
        val baseLat = latitude.toInt()
        val baseLon = longitude.toInt()
        for (dlat in -1..1) {
            for (dlon in -1..1) {
                for (city in timezoneGrid[baseLat + dlat to baseLon + dlon].orEmpty()) {
                    val elev = city.elevationMeters ?: continue
                    if (elev <= 0.0) continue
                    val distKm = haversineKm(latitude, longitude, city.latitude, city.longitude)
                    if (distKm < bestDistKm) {
                        bestDistKm = distKm
                        bestElev = elev
                    }
                }
            }
        }
        if (bestElev == null || bestDistKm > maxKm) return null
        return bestElev
    }

    private fun haversineKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6_371.0
        val dLat = toRadians(lat2 - lat1)
        val dLon = toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) +
            cos(toRadians(lat1)) * cos(toRadians(lat2)) * sin(dLon / 2).pow(2.0)
        return r * 2 * atan2(sqrt(a), sqrt(1 - a))
    }

    private fun toRadians(degrees: Double): Double = degrees * PI / 180.0
}
