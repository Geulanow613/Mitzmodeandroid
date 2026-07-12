package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.GeneratorMitzvah
import com.beardytop.beatzaddik.domain.GeneratorMitzvotList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.random.Random

/**
 * Shuffle-without-replacement mitzvah generator (parity with Android MitzvotRepository).
 * Bundled JSON is ready immediately; optional cloud list merges via [refreshFromCloud].
 */
class MitzvotGeneratorRepository(
    private val fetchCloudJson: suspend (storedEtag: String?) -> CloudFetchResult = { CloudFetchResult.Unchanged },
) {
    data class CloudFetchResult(
        val json: String? = null,
        val etag: String? = null,
        val wasModified: Boolean = false,
    ) {
        companion object {
            val Unchanged = CloudFetchResult(wasModified = false)
        }
    }

    private val mutex = Mutex()
    private val json = Json { ignoreUnknownKeys = true }
    private var pool: List<GeneratorMitzvah>? = null
    private val shuffleDeck = mutableListOf<GeneratorMitzvah>()
    private var shuffleIndex = 0
    private var lastIssuedId: String? = null
    private var cloudEtag: String? = null

    /** Load bundled list only — never blocks on network. */
    suspend fun preload() {
        ensureLocalPool()
    }

    /** Optional cloud merge; call from a background job, never from the tap path. */
    suspend fun refreshFromCloud() = withContext(Dispatchers.IO) {
        ensureLocalPool()
        runCatching {
            val result = fetchCloudJson(cloudEtag)
            if (!result.wasModified || result.json.isNullOrBlank()) return@runCatching
            cloudEtag = result.etag
            val cloud = json.decodeFromString<GeneratorMitzvotList>(result.json).mitzvot
            mutex.withLock {
                val local = pool.orEmpty()
                val combined = (local + cloud).distinctBy { it.id }
                if (combined.size != local.size) {
                    pool = combined
                    resetShuffle(combined)
                }
            }
        }
    }

    suspend fun nextMitzvah(): GeneratorMitzvah? = withContext(Dispatchers.IO) {
        ensureLocalPool()
        mutex.withLock {
            val p = pool ?: return@withLock null
            if (p.isEmpty()) return@withLock null
            nextShuffledLocked()
        }
    }

    private suspend fun ensureLocalPool() = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (pool != null) return@withLock
            val local = runCatching {
                json.decodeFromString<GeneratorMitzvotList>(MitzvotListAssets.loadBundledJson()).mitzvot
            }.getOrElse { emptyList() }
            pool = local
            resetShuffle(local)
        }
    }

    private fun resetShuffle(source: List<GeneratorMitzvah>) {
        shuffleDeck.clear()
        shuffleDeck.addAll(source.shuffled(Random.Default))
        shuffleIndex = 0
    }

    private fun nextShuffledLocked(): GeneratorMitzvah {
        if (shuffleDeck.isEmpty()) {
            val p = pool.orEmpty()
            resetShuffle(p)
        }
        if (shuffleIndex >= shuffleDeck.size) {
            resetShuffle(shuffleDeck.toList())
        }
        var next = shuffleDeck[shuffleIndex++]
        if (next.id == lastIssuedId && shuffleDeck.size > 1) {
            if (shuffleIndex >= shuffleDeck.size) {
                resetShuffle(shuffleDeck.toList())
            }
            next = shuffleDeck[shuffleIndex++]
        }
        lastIssuedId = next.id
        return next
    }
}
