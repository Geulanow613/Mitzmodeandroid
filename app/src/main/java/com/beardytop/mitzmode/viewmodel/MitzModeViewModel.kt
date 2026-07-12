package com.beardytop.mitzmode.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beardytop.mitzmode.data.Mitzvah
import com.beardytop.mitzmode.data.MitzvotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.google.gson.Gson
import com.beardytop.mitzmode.BuildConfig
import com.beardytop.mitzmode.util.MitzvahLevels
import com.beardytop.mitzmode.util.RewardVideoAssets
import com.beardytop.mitzmode.util.SentryUtil
import com.beardytop.mitzmode.util.VideoManager

@HiltViewModel
class MitzModeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mitzvotRepository: MitzvotRepository
) : ViewModel() {

    companion object {
        private const val TAG = "MitzModeViewModel"
        /** [showVideo] uses mitzmodenew{1..13}.mp4; this id maps to asset `finalreward.mp4` with audio. */
        const val FINAL_REWARD_VIDEO_ID = 100
        /** ExoPlayer volume when unmuted (1f = full; does not change system/media volume). */
        const val FINAL_REWARD_PLAYER_VOLUME_UNMUTED = 1f
    }
    private val prefs = context.getSharedPreferences("mitzvot_prefs", Context.MODE_PRIVATE)
    
    private val _currentMitzvah = MutableStateFlow<Mitzvah?>(null)
    val currentMitzvah: StateFlow<Mitzvah?> = _currentMitzvah.asStateFlow()
    
    private val _completedMitzvot = MutableStateFlow<List<Mitzvah>>(emptyList())
    val completedMitzvot: StateFlow<List<Mitzvah>> = _completedMitzvot.asStateFlow()
    
    private val _showVideo = MutableStateFlow<Int?>(null)
    val showVideo: StateFlow<Int?> = _showVideo.asStateFlow()
    
    private val _isIdle = MutableStateFlow(false)
    val isIdle: StateFlow<Boolean> = _isIdle.asStateFlow()
    
    private var idleJob: Job? = null
    private var prewarmJob: Job? = null
    
    private val _acceptedMitzvotCount = MutableStateFlow(0)
    val acceptedMitzvotCount: StateFlow<Int> = _acceptedMitzvotCount.asStateFlow()
    
    private var _showLevelUp = MutableStateFlow<String?>(null)
    val showLevelUp: StateFlow<String?> = _showLevelUp.asStateFlow()
    
    private val _showMusicReward = MutableStateFlow(false)
    val showMusicReward: StateFlow<Boolean> = _showMusicReward.asStateFlow()
    
    // Add loading state to prevent multiple operations
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // First-time app tour: show until user completes or skips
    private val _showTour = MutableStateFlow(!prefs.getBoolean("tour_completed", false))
    val showTour: StateFlow<Boolean> = _showTour.asStateFlow()

    private var uiReady = false

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loadSavedJob = async { loadSavedMitzvotAsync() }
                val preloadJob = async { preloadMitzvotInBackground() }
                loadSavedJob.await()
                preloadJob.await()
            } catch (e: Exception) {
                handleError(e, "ViewModel initialization")
            }
        }
        viewModelScope.launch {
            isIdle.collect { idle ->
                if (idle) {
                    scheduleRewardPrewarm(_completedMitzvot.value.size, delayMs = 3_000L)
                }
            }
        }
    }

    /** Called after the first frame is drawn — starts idle video prewarm timer. */
    fun onMainScreenReady() {
        if (uiReady) return
        uiReady = true
        startIdleTimer()
    }

    fun cancelRewardPrewarm() {
        prewarmJob?.cancel()
        prewarmJob = null
        VideoManager.getInstance(context).cancelIdlePrewarm()
    }
    
    private suspend fun loadSavedMitzvotAsync() = withContext(Dispatchers.IO) {
        try {
            val savedMitzvotSet = prefs.getStringSet("completed_mitzvot", emptySet()) ?: emptySet()
            
            // Parse JSON in background thread with timeout
            val parsedMitzvot = withTimeoutOrNull(5000) { // 5 second timeout
                savedMitzvotSet.mapNotNull { mitzvahJson ->
                    try {
                        Gson().fromJson(mitzvahJson, Mitzvah::class.java)
                    } catch (e: Exception) {
                        // Log parsing error but continue with other items
                        android.util.Log.w("MitzModeViewModel", "Failed to parse mitzvah JSON: $mitzvahJson", e)
                        null
                    }
                }
            } ?: emptyList()
            
            // Update UI state on main thread
            withContext(Dispatchers.Main) {
                _completedMitzvot.value = parsedMitzvot
            }
        } catch (e: Exception) {
            handleError(e, "loadSavedMitzvotAsync")
            // Ensure UI is updated even on error
            withContext(Dispatchers.Main) {
                _completedMitzvot.value = emptyList()
            }
        }
    }
    
    private suspend fun preloadMitzvotInBackground() {
        try {
            // Add timeout to prevent hanging
            withTimeoutOrNull(10000) { // 10 second timeout
                mitzvotRepository.preloadMitzvot()
            } ?: run {
                android.util.Log.w("MitzModeViewModel", "Preload mitzvot timed out after 10 seconds")
            }
        } catch (e: Exception) {
            handleError(e, "preloadMitzvotInBackground")
        }
    }
    
    fun onMitzvahButtonPressed() {
        // Prevent multiple simultaneous button presses
        if (_isLoading.value) {
            android.util.Log.d("MitzModeViewModel", "Button press ignored - already loading")
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Try to get a mitzvah immediately if available (non-blocking)
                val immediateMitzvah = mitzvotRepository.getRandomMitzvahIfAvailable()
                if (immediateMitzvah != null) {
                    _currentMitzvah.value = immediateMitzvah
                    resetIdleTimer()
                } else {
                    // If no mitzvah is immediately available, load them with timeout
                    val mitzvah = withTimeoutOrNull(5000) { // 5 second timeout
                        mitzvotRepository.getRandomMitzvah()
                    }
                    
                    if (mitzvah != null) {
                        _currentMitzvah.value = mitzvah
                        resetIdleTimer()
                    } else {
                        // Provide fallback if timeout occurs
                        _currentMitzvah.value = Mitzvah(
                            id = "timeout_fallback",
                            text = "Say a quick prayer for patience. Sometimes the best things take a moment to load! 🙏",
                            links = emptyList()
                        )
                        resetIdleTimer()
                    }
                }
            } catch (e: Exception) {
                handleError(e, "onMitzvahButtonPressed")
                // Provide fallback mitzvah if there's an error
                _currentMitzvah.value = Mitzvah(
                    id = "button_error_fallback",
                    text = "Thank G-d for the gift of this moment. Even when technology fails, gratitude always works! 🙏",
                    links = emptyList()
                )
                resetIdleTimer()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun startIdleTimer() {
        idleJob?.cancel()
        idleJob = viewModelScope.launch {
            delay(30000) // 30 seconds
            _isIdle.value = true
        }
    }
    
    private fun resetIdleTimer() {
        _isIdle.value = false
        startIdleTimer()
    }
    
    fun onChecklistMitzvahChecked(itemId: String, title: String, dayKey: String) {
        if (dayKey.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val entryId = "checklist_count_${itemId}_$dayKey"
                if (_completedMitzvot.value.any { it.id == entryId }) return@launch
                val mitzvah = Mitzvah(
                    id = entryId,
                    text = title,
                    links = emptyList(),
                )
                val updatedList = _completedMitzvot.value + mitzvah
                withContext(Dispatchers.Main) {
                    _completedMitzvot.value = updatedList
                }
                withTimeoutOrNull(3000) {
                    val jsonSet = updatedList.map { Gson().toJson(it) }.toSet()
                    prefs.edit().putStringSet("completed_mitzvot", jsonSet).apply()
                }
            } catch (e: Exception) {
                handleError(e, "onChecklistMitzvahChecked")
            }
        }
    }

    fun onVideoComplete() {
        _showVideo.value = null
        scheduleRewardPrewarm(_completedMitzvot.value.size, delayMs = 8_000L)
    }

    /** Replay `finalreward.mp4` from the certificate (no level-up rainbow overlay). */
    fun requestFinalRewardVideoReplay() {
        viewModelScope.launch(Dispatchers.Main) {
            _showLevelUp.value = null
            _showVideo.value = FINAL_REWARD_VIDEO_ID
        }
    }
    
    fun onLevelUpComplete() {
        _showLevelUp.value = null
    }
    
    fun showMusicReward() {
        _showMusicReward.value = true
    }
    
    fun onMusicRewardComplete() {
        _showMusicReward.value = false
    }
    
    fun startMusicService() {
        // Start the music service when the app is opened
        try {
            val intent = android.content.Intent(context, com.beardytop.mitzmode.service.MusicPlayerService::class.java)
            context.startService(intent)
        } catch (e: Exception) {
            handleError(e, "startMusicService")
        }
    }
    
    private fun handleError(e: Exception, context: String) {
        SentryUtil.logError(e, mapOf(
            "context" to context,
            "screen" to "main",
            "user_action" to "mitzvah_interaction"
        ))
    }
    
    private fun getCurrentLevel(count: Int): String = MitzvahLevels.forCount(count)
    
    private fun getVideoNumberForLevel(count: Int): Int =
        RewardVideoAssets.videoIdForMilestone(count)

    private fun scheduleRewardPrewarm(completedCount: Int, delayMs: Long = 20_000L) {
        val asset = RewardVideoAssets.assetForNextMilestone(completedCount) ?: return
        prewarmJob?.cancel()
        prewarmJob = viewModelScope.launch(Dispatchers.Main) {
            delay(delayMs)
            if (_showVideo.value != null) return@launch
            VideoManager.getInstance(context).prewarmRewardPlayer(asset)
        }
    }
    
    fun onMitzvahAccepted() {
        viewModelScope.launch(Dispatchers.IO) { // Move to background thread
            try {
                val mitzvah = currentMitzvah.value ?: return@launch
                
                // Get current count before adding new mitzvah
                val currentCount = _completedMitzvot.value.size
                if (BuildConfig.DEBUG) Log.d(TAG, "Current mitzvot count: $currentCount")

                // Calculate new count
                val newCount = currentCount + 1
                withContext(Dispatchers.Main) {
                    _acceptedMitzvotCount.value = newCount
                }
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "New mitzvot count will be: $newCount")
                    Log.d(TAG, "New level will be: ${getCurrentLevel(newCount)}")
                }

                // Check for milestone achievements
                val shouldShowVideo = when (newCount) {
                    1, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1800 -> {
                        if (BuildConfig.DEBUG) Log.d(TAG, "Milestone reached at count $newCount!")
                        true
                    }
                    else -> false
                }
                
                // Update completed mitzvot list FIRST
                val updatedList = _completedMitzvot.value + mitzvah
                
                // Update UI on main thread
                withContext(Dispatchers.Main) {
                    _completedMitzvot.value = updatedList
                }
                
                // Save to preferences in background with timeout
                withTimeoutOrNull(3000) { // 3 second timeout for SharedPreferences
                    try {
                        val jsonSet = updatedList.map { Gson().toJson(it) }.toSet()
                        prefs.edit().putStringSet("completed_mitzvot", jsonSet).apply()
                    } catch (e: Exception) {
                        android.util.Log.e("MitzModeViewModel", "Failed to save completed mitzvot", e)
                    }
                }
                
                // THEN check for video/level up AFTER updating the list
                if (shouldShowVideo) {
                    val newLevel = getCurrentLevel(newCount)
                    if (BuildConfig.DEBUG) {
                        Log.d(
                            TAG,
                            "Playing video ${getVideoNumberForLevel(newCount)} for level $newLevel"
                        )
                    }

                    withContext(Dispatchers.Main) {
                        cancelRewardPrewarm()
                        val videoId = getVideoNumberForLevel(newCount)
                        _showVideo.value = videoId
                        _showLevelUp.value =
                            if (videoId == FINAL_REWARD_VIDEO_ID) null else newLevel
                    }
                }
                
                if (BuildConfig.DEBUG) {
                    Log.d(
                        TAG,
                        "Mitzvah accepted! showVideo = ${_showVideo.value}, showLevelUp = ${_showLevelUp.value}"
                    )
                }
            } catch (e: Exception) {
                handleError(e, "onMitzvahAccepted")
            }
        }
    }
    
    fun clearCurrentMitzvah() {
        _currentMitzvah.value = null
    }

    fun completeTour() {
        _showTour.value = false
        prefs.edit().putBoolean("tour_completed", true).apply()
    }

    fun skipTour() {
        _showTour.value = false
        prefs.edit().putBoolean("tour_completed", true).apply()
    }

} 