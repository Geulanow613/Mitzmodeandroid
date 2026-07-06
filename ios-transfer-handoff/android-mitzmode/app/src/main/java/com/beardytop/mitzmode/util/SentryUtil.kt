package com.beardytop.mitzmode.util

import io.sentry.Sentry
import android.util.Log
import android.os.Looper
import android.os.Handler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

object SentryUtil {
    private const val TAG = "SentryUtil"
    private var anrWatchdog: Job? = null
    private val isMainThreadResponsive = AtomicBoolean(true)
    
    fun logError(throwable: Throwable, tags: Map<String, String> = emptyMap()) {
        try {
            // Always log to Android Log first
            Log.e(TAG, "Error: ${throwable.message}", throwable)
            
            // Then try to send to Sentry
            Sentry.withScope { scope ->
                tags.forEach { (key, value) ->
                    scope.setTag(key, value)
                }
                scope.setTag("error_type", throwable::class.java.simpleName)
                
                // Add additional context for ANR-related errors
                if (throwable.message?.contains("ANR", ignoreCase = true) == true ||
                    throwable is java.util.concurrent.TimeoutException) {
                    scope.setTag("anr_related", "true")
                    scope.setExtra("main_thread_responsive", isMainThreadResponsive.get().toString())
                    scope.setExtra("current_thread", Thread.currentThread().name)
                }
                
                Sentry.captureException(throwable)
            }
        } catch (e: Exception) {
            // Fallback to Android logging if Sentry fails
            Log.e(TAG, "Failed to log error to Sentry", e)
            Log.e(TAG, "Original error: ${throwable.message}", throwable)
        }
    }
    
    fun logMessage(message: String, level: String = "info", tags: Map<String, String> = emptyMap()) {
        try {
            Log.i(TAG, message)
            
            Sentry.withScope { scope ->
                tags.forEach { (key, value) ->
                    scope.setTag(key, value)
                }
                when (level.lowercase()) {
                    "error" -> Sentry.captureMessage(message, io.sentry.SentryLevel.ERROR)
                    "warning" -> Sentry.captureMessage(message, io.sentry.SentryLevel.WARNING)
                    "info" -> Sentry.captureMessage(message, io.sentry.SentryLevel.INFO)
                    "debug" -> Sentry.captureMessage(message, io.sentry.SentryLevel.DEBUG)
                    else -> Sentry.captureMessage(message)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to log message to Sentry: $message", e)
        }
    }
    
    /**
     * Start ANR detection watchdog
     * This monitors main thread responsiveness and logs potential ANR situations
     */
    fun startAnrWatchdog(startupGracePeriodMs: Long = 0L) {
        stopAnrWatchdog() // Stop any existing watchdog
        
        anrWatchdog = CoroutineScope(Dispatchers.Default).launch {
            if (startupGracePeriodMs > 0) {
                delay(startupGracePeriodMs)
            }
            while (true) {
                delay(2000) // Check every 2 seconds
                
                val startTime = System.currentTimeMillis()
                isMainThreadResponsive.set(false)
                
                // Post a task to main thread to check if it's responsive
                Handler(Looper.getMainLooper()).post {
                    isMainThreadResponsive.set(true)
                }
                
                // Wait a bit and check if main thread responded
                delay(1000) // Wait 1 second for main thread to respond
                
                if (!isMainThreadResponsive.get()) {
                    val blockedTime = System.currentTimeMillis() - startTime
                    Log.w(TAG, "Main thread appears blocked for ${blockedTime}ms")
                    
                    if (blockedTime > 3000) { // More than 3 seconds
                        logMessage(
                            "Potential ANR detected - main thread blocked for ${blockedTime}ms",
                            "warning",
                            mapOf(
                                "anr_detection" to "true",
                                "blocked_time_ms" to blockedTime.toString(),
                                "detection_method" to "watchdog"
                            )
                        )
                    }
                }
            }
        }
        
        Log.d(TAG, "ANR watchdog started")
    }
    
    /**
     * Stop ANR detection watchdog
     */
    fun stopAnrWatchdog() {
        anrWatchdog?.cancel()
        anrWatchdog = null
        Log.d(TAG, "ANR watchdog stopped")
    }
    
    /**
     * Log performance metrics to help identify slow operations
     */
    fun logPerformanceMetric(operationName: String, durationMs: Long, tags: Map<String, String> = emptyMap()) {
        if (durationMs > 100) { // Only log operations taking more than 100ms
            val allTags = tags + mapOf(
                "operation" to operationName,
                "duration_ms" to durationMs.toString(),
                "performance_metric" to "true"
            )
            
            val level = when {
                durationMs > 1000 -> "warning"
                durationMs > 500 -> "info"
                else -> "debug"
            }
            
            logMessage("Operation '$operationName' took ${durationMs}ms", level, allTags)
        }
    }
    
    /**
     * Wrapper for timing operations
     */
    inline fun <T> timeOperation(operationName: String, tags: Map<String, String> = emptyMap(), operation: () -> T): T {
        val startTime = System.currentTimeMillis()
        return try {
            operation()
        } finally {
            val duration = System.currentTimeMillis() - startTime
            logPerformanceMetric(operationName, duration, tags)
        }
    }
} 