package com.beardytop.beatzaddik.platform

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

@Serializable
private data class ElevationResponse(val elevation: List<Double> = emptyList())

actual suspend fun fetchElevationMeters(latitude: Double, longitude: Double): Double? =
    withContext(Dispatchers.IO) {
        runCatching {
            val query =
                "latitude=${"%.5f".format(latitude)}&longitude=${"%.5f".format(longitude)}"
            val url = URL("https://api.open-meteo.com/v1/elevation?$query")
            val conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 8_000
                readTimeout = 8_000
                requestMethod = "GET"
            }
            conn.inputStream.bufferedReader().use { reader ->
                val payload = Json.decodeFromString<ElevationResponse>(reader.readText())
                payload.elevation.firstOrNull()?.takeIf { it.isFinite() && it >= 0.0 }
            }
        }.getOrNull()
    }
