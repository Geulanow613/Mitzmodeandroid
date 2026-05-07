package com.beardytop.mitzmode.viewmodel

import android.content.Context
import android.content.Intent
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
import com.beardytop.mitzmode.util.SentryUtil

@HiltViewModel
class MitzModeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mitzvotRepository: MitzvotRepository
) : ViewModel() {
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
    
    init {
        viewModelScope.launch {
            try {
                // Use parallel execution to avoid blocking
                val loadSavedJob = async(Dispatchers.IO) { loadSavedMitzvotAsync() }
                val preloadJob = async(Dispatchers.IO) { preloadMitzvotInBackground() }
                
                // Start idle timer immediately (non-blocking)
                startIdleTimer()
                
                // Wait for data loading in background
                loadSavedJob.await()
                preloadJob.await()
            } catch (e: Exception) {
                handleError(e, "ViewModel initialization")
            }
        }
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
    
    fun onVideoComplete() {
        _showVideo.value = null
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
    
    private fun getCurrentLevel(count: Int): String {
        println("DEBUG: Calculating level for count: $count")
        val level = when (count) {
            in 1..9 -> "Beginner"
            in 10..49 -> "Ba'al Teshuva"
            in 50..99 -> "Master Cholent Chef"
            in 100..199 -> "Aspiring Kiddush Maker"
            in 200..299 -> "Assistant Gabbai"
            in 300..399 -> "Guy who hands out candy at shul"
            in 400..499 -> "Western Wall Reveler"
            in 500..599 -> "Sofer"
            in 600..699 -> "Tzaddik"
            in 700..799 -> "Living Sefer Torah"
            in 800..899 -> "Eliyahu HaNavi"
            in 900..999 -> "King David"
            in 1000..Int.MAX_VALUE -> "Moshiach!!!"
            else -> "Beginner"  // Default case, should never happen
        }
        println("DEBUG: Level calculated as: $level")
        return level
    }
    
    private fun getVideoNumberForLevel(count: Int): Int {
        return when (count) {
            1 -> 1  // Beginner video at first mitzvah
            10 -> 2  // Ba'al Teshuva video at 10th mitzvah
            50 -> 3  // Master Cholent Chef at 50th mitzvah
            100 -> 4  // Aspiring Kiddush Maker at 100th mitzvah
            200 -> 5  // Assistant Gabbai at 200th mitzvah
            300 -> 6  // Guy who hands out candy at shul at 300th mitzvah
            400 -> 7  // Western Wall Reveler at 400th mitzvah
            500 -> 8  // Sofer at 500th mitzvah
            600 -> 9  // Tzaddik at 600th mitzvah
            700 -> 10 // Living Sefer Torah at 700th mitzvah
            800 -> 11 // Eliyahu HaNavi at 800th mitzvah
            900 -> 12 // King David at 900th mitzvah
            1000 -> 13 // Moshiach!!! at 1000th mitzvah
            else -> 0  // No video for non-milestone numbers
        }
    }
    
    fun onMitzvahAccepted() {
        viewModelScope.launch(Dispatchers.IO) { // Move to background thread
            try {
                val mitzvah = currentMitzvah.value ?: return@launch
                
                // Get current count before adding new mitzvah
                val currentCount = _completedMitzvot.value.size
                println("DEBUG: Current mitzvot count: $currentCount")
                
                // Calculate new count
                val newCount = currentCount + 1
                println("DEBUG: New mitzvot count will be: $newCount")
                println("DEBUG: New level will be: ${getCurrentLevel(newCount)}")
                
                // Check for milestone achievements
                val shouldShowVideo = when (newCount) {
                    1, 10, 50, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 -> {
                        println("DEBUG: Milestone reached at count $newCount!")
                        true
                    }
                    else -> false
                }
                
                // Update completed mitzvot list FIRST
                val updatedList = _completedMitzvot.value + mitzvah
                
                // Update UI state on main thread
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
                    println("DEBUG: Playing video ${getVideoNumberForLevel(newCount)} for level $newLevel")
                    
                    // Update UI on main thread
                    withContext(Dispatchers.Main) {
                        _showVideo.value = getVideoNumberForLevel(newCount)
                        _showLevelUp.value = newLevel // Show level up simultaneously with video
                    }
                }
                
                println("DEBUG: Mitzvah accepted! showVideo = ${_showVideo.value}, showLevelUp = ${_showLevelUp.value}")
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