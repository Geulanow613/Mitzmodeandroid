package com.beardytop.beatzaddik.mitzmode

import com.beardytop.beatzaddik.data.MitzvotCountStore
import com.beardytop.beatzaddik.data.MitzvotGeneratorRepository
import com.beardytop.beatzaddik.domain.ChecklistMitzvahCounting
import com.beardytop.beatzaddik.domain.GeneratorMitzvah
import com.beardytop.beatzaddik.domain.MitzModeFeatures
import com.beardytop.beatzaddik.domain.MitzvahLevels
import com.beardytop.beatzaddik.platform.fetchMitzvotCloudJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Shared Mitz Mode session: generator + persisted count used by Accept and checklist checks.
 */
class UnifiedMitzModeSession(
    platformContext: Any?,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val countStore = MitzvotCountStore(platformContext)
    private val generator = MitzvotGeneratorRepository { etag -> fetchMitzvotCloudJson(etag) }
    private val mutex = Mutex()

    private val _completed = MutableStateFlow<List<GeneratorMitzvah>>(emptyList())
    val completed: StateFlow<List<GeneratorMitzvah>> = _completed.asStateFlow()

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    private val _current = MutableStateFlow<GeneratorMitzvah?>(null)
    val currentMitzvah: StateFlow<GeneratorMitzvah?> = _current.asStateFlow()

    private val _showFinalReward = MutableStateFlow(false)
    val showFinalReward: StateFlow<Boolean> = _showFinalReward.asStateFlow()

    private val _loaded = MutableStateFlow(false)
    val loaded: StateFlow<Boolean> = _loaded.asStateFlow()

    init {
        // Bundled list first so the center button works on the first tap.
        // Cloud refresh is separate and must not delay openGenerator().
        scope.launch {
            runCatching { generator.preload() }
            runCatching { generator.refreshFromCloud() }
        }
        scope.launch {
            mutex.withLock {
                val saved = runCatching { countStore.loadCompleted() }.getOrElse { emptyList() }
                _completed.value = saved
                _count.value = saved.size
                _loaded.value = true
            }
        }
    }

    fun openGenerator() {
        scope.launch {
            val next = generator.nextMitzvah() ?: return@launch
            withContext(Dispatchers.Main.immediate) {
                _current.value = next
            }
        }
    }

    fun nextMitzvah() {
        scope.launch {
            val next = generator.nextMitzvah() ?: return@launch
            withContext(Dispatchers.Main.immediate) {
                _current.value = next
            }
        }
    }

    fun dismissGenerator() {
        _current.value = null
    }

    fun acceptCurrent(onAccepted: (() -> Unit)? = null) {
        val mitzvah = _current.value ?: return
        scope.launch {
            mutex.withLock {
                val updated = _completed.value + mitzvah
                _completed.value = updated
                _count.value = updated.size
                runCatching { countStore.saveCompleted(updated) }
                maybeTriggerFinalReward(updated.size)
            }
            if (onAccepted != null) {
                withContext(Dispatchers.Main) { onAccepted() }
            }
        }
    }

    /**
     * Counts a checklist completion once per [dayKey] (halachic day / tzeit).
     * Uncheck → check again the same day does not change the count.
     */
    fun recordChecklistCheck(itemId: String, title: String, dayKey: String) {
        if (dayKey.isBlank()) return
        scope.launch {
            mutex.withLock {
                val entryId = ChecklistMitzvahCounting.entryId(itemId, dayKey)
                if (ChecklistMitzvahCounting.alreadyCounted(_completed.value.map { it.id }, itemId, dayKey)) {
                    return@withLock
                }
                val mitzvah = GeneratorMitzvah(id = entryId, text = title)
                val updated = _completed.value + mitzvah
                _completed.value = updated
                _count.value = updated.size
                runCatching { countStore.saveCompleted(updated) }
                maybeTriggerFinalReward(updated.size)
            }
        }
    }

    fun requestFinalRewardReplay() {
        if (MitzModeFeatures.finalRewardEnabled && _count.value >= MitzvahLevels.FINAL_REWARD_THRESHOLD) {
            _showFinalReward.value = true
        }
    }

    fun onFinalRewardComplete() {
        _showFinalReward.value = false
    }

    /**
     * Debug-only: pad or trim the persisted mitzvah list so Status / certificate
     * can be previewed at a target count (e.g. 100, 500, 1800).
     */
    fun debugSetCount(target: Int) {
        val safe = target.coerceAtLeast(0)
        scope.launch {
            mutex.withLock {
                val current = _completed.value
                val updated = when {
                    current.size == safe -> current
                    current.size > safe -> current.take(safe)
                    else -> current + List(safe - current.size) { i ->
                        GeneratorMitzvah(
                            id = "debug_pad_${safe}_${i}",
                            text = "Debug mitzvah ${current.size + i + 1}",
                        )
                    }
                }
                _completed.value = updated
                _count.value = updated.size
                runCatching { countStore.saveCompleted(updated) }
                if (safe >= MitzvahLevels.FINAL_REWARD_THRESHOLD) {
                    maybeTriggerFinalReward(safe)
                }
            }
        }
    }

    private fun maybeTriggerFinalReward(newCount: Int) {
        if (!MitzModeFeatures.finalRewardEnabled) return
        if (!MitzModeFeatures.milestoneVideosEnabled && newCount != MitzvahLevels.FINAL_REWARD_THRESHOLD) {
            // Only auto-play at exactly 1800 when milestones are off.
            if (newCount == MitzvahLevels.FINAL_REWARD_THRESHOLD) {
                _showFinalReward.value = true
            }
            return
        }
        if (newCount == MitzvahLevels.FINAL_REWARD_THRESHOLD) {
            _showFinalReward.value = true
        }
    }
}
