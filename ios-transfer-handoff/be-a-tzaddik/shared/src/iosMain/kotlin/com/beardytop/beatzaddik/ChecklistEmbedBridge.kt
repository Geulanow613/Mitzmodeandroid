package com.beardytop.beatzaddik

import com.beardytop.beatzaddik.platform.PlatformLocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Shared deps cache for Mitz Mode iOS embed (parity with Android [com.beardytop.mitzmode.tzaddik.TzaddikBridge]).
 * Call [preloadChecklistDependencies] at app launch; checklist opens faster when warm.
 */
object ChecklistEmbedBridge {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val mutex = Mutex()
    private val _cachedDeps = MutableStateFlow<AppDependencies?>(null)
    val depsFlow: StateFlow<AppDependencies?> = _cachedDeps.asStateFlow()

    @Volatile
    var cachedDeps: AppDependencies?
        get() = _cachedDeps.value
        private set(value) {
            _cachedDeps.value = value
        }

    @Volatile
    private var preloadStarted = false

    fun preloadChecklistDependencies() {
        if (cachedDeps != null || preloadStarted) return
        preloadStarted = true
        scope.launch {
            runCatching { ensureDependencies() }
        }
    }

    suspend fun ensureDependencies(): AppDependencies {
        cachedDeps?.let { return it }
        return mutex.withLock {
            cachedDeps ?: AppDependencies.create(
                platformContext = null,
                locationService = PlatformLocationService(),
                embeddedMode = true,
            ).also { cachedDeps = it }
        }
    }
}
