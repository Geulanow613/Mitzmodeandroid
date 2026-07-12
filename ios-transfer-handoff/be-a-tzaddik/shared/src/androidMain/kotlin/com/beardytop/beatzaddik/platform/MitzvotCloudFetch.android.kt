package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.data.MitzvotGeneratorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

private const val CLOUD_URL = "https://raw.githubusercontent.com/Geulanow613/mitzmode/main/mitzvotcloud.json"

actual suspend fun fetchMitzvotCloudJson(storedEtag: String?): MitzvotGeneratorRepository.CloudFetchResult =
    withContext(Dispatchers.IO) {
        runCatching {
            val connection = URL(CLOUD_URL).openConnection() as HttpURLConnection
            connection.connectTimeout = 4000
            connection.readTimeout = 10000
            connection.setRequestProperty("User-Agent", "MitzMode-Android-App")
            connection.setRequestProperty("Accept", "application/json")
            if (storedEtag != null) {
                connection.setRequestProperty("If-None-Match", storedEtag)
            }
            try {
                when (connection.responseCode) {
                    HttpURLConnection.HTTP_NOT_MODIFIED ->
                        MitzvotGeneratorRepository.CloudFetchResult(wasModified = false, etag = storedEtag)
                    HttpURLConnection.HTTP_OK -> {
                        val body = connection.inputStream.bufferedReader().use { it.readText() }
                        val etag = connection.getHeaderField("ETag")
                        MitzvotGeneratorRepository.CloudFetchResult(
                            json = body,
                            etag = etag,
                            wasModified = true,
                        )
                    }
                    else -> MitzvotGeneratorRepository.CloudFetchResult.Unchanged
                }
            } finally {
                connection.disconnect()
            }
        }.getOrElse { MitzvotGeneratorRepository.CloudFetchResult.Unchanged }
    }
