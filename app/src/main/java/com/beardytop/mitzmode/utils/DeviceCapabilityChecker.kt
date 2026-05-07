package com.beardytop.mitzmode.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.util.Log

object DeviceCapabilityChecker {
    private const val TAG = "DeviceCapabilityChecker"
    
    fun canHandleVideoBackground(context: Context): Boolean {
        // TEMPORARILY DISABLE ALL VIDEO BACKGROUNDS to fix performance issues
        Log.w(TAG, "Video backgrounds temporarily disabled for performance optimization")
        return false
        
        /* Original logic kept for future reference:
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        
        // Get current memory usage
        val availableMemoryMB = memoryInfo.availMem / (1024 * 1024)
        val totalMemoryMB = memoryInfo.totalMem / (1024 * 1024)
        val usedMemoryMB = totalMemoryMB - availableMemoryMB
        val memoryUsagePercent = (usedMemoryMB.toFloat() / totalMemoryMB.toFloat()) * 100
        
        Log.d(TAG, "Memory stats - Available: ${availableMemoryMB}MB, Total: ${totalMemoryMB}MB, Used: ${usedMemoryMB}MB (${memoryUsagePercent.toInt()}%)")
        Log.d(TAG, "Low memory threshold: ${memoryInfo.threshold / (1024 * 1024)}MB")
        Log.d(TAG, "Is low memory: ${memoryInfo.lowMemory}")
        
        // Don't use video if:
        // 1. Device is currently in low memory state
        // 2. Less than 800MB available memory (increased from 500MB)
        // 3. Memory usage is above 70% (decreased from 80%)
        // 4. Device has less than 4GB total RAM (increased from 2GB)
        if (memoryInfo.lowMemory) {
            Log.w(TAG, "Device is in low memory state - disabling video")
            return false
        }
        
        if (availableMemoryMB < 800) {
            Log.w(TAG, "Less than 800MB available - disabling video")
            return false
        }
        
        if (memoryUsagePercent > 70) {
            Log.w(TAG, "Memory usage above 70% - disabling video")
            return false
        }
        
        if (totalMemoryMB < 4096) {
            Log.w(TAG, "Less than 4GB total RAM - disabling video")
            return false
        }
        
        // Only allow video on high-end devices with Android 12+ and 6GB+ RAM
        val canHandle = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && totalMemoryMB >= 6144 -> true // Android 12+ with 6GB+ RAM
            else -> false
        }
        
        Log.d(TAG, "Video background capability: $canHandle")
        return canHandle
        */
    }
    
    fun getCurrentMemoryUsage(context: Context): MemoryUsage {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        
        return MemoryUsage(
            availableMemoryMB = memoryInfo.availMem / (1024 * 1024),
            totalMemoryMB = memoryInfo.totalMem / (1024 * 1024),
            isLowMemory = memoryInfo.lowMemory,
            thresholdMB = memoryInfo.threshold / (1024 * 1024)
        )
    }
    
    data class MemoryUsage(
        val availableMemoryMB: Long,
        val totalMemoryMB: Long,
        val isLowMemory: Boolean,
        val thresholdMB: Long
    ) {
        val usedMemoryMB: Long get() = totalMemoryMB - availableMemoryMB
        val memoryUsagePercent: Float get() = (usedMemoryMB.toFloat() / totalMemoryMB.toFloat()) * 100
    }
} 