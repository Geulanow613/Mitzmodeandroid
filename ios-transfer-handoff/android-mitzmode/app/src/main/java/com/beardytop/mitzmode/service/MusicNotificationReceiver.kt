package com.beardytop.mitzmode.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MusicNotificationReceiver : BroadcastReceiver() {
    
    companion object {
        const val ACTION_PLAY_PAUSE = "com.beardytop.mitzmode.PLAY_PAUSE"
        const val ACTION_STOP = "com.beardytop.mitzmode.STOP"
        const val ACTION_CLOSE = "com.beardytop.mitzmode.CLOSE"
        private const val TAG = "MusicNotificationReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "=== BroadcastReceiver onReceive called! ===")
        Log.d(TAG, "Received action: ${intent.action}")
        Log.d(TAG, "Intent extras: ${intent.extras}")
        Log.d(TAG, "Intent package: ${intent.`package`}")
        Log.d(TAG, "Intent categories: ${intent.categories}")
        
        when (intent.action) {
            ACTION_PLAY_PAUSE -> Log.d(TAG, "Processing PLAY_PAUSE action")
            ACTION_STOP -> Log.d(TAG, "Processing STOP action")
            ACTION_CLOSE -> Log.d(TAG, "Processing CLOSE action")
            else -> Log.d(TAG, "Unknown action: ${intent.action}")
        }
        
        val serviceIntent = Intent(context, MusicPlayerService::class.java)
        serviceIntent.action = intent.action
        
        try {
            val result = context.startService(serviceIntent)
            Log.d(TAG, "Service command sent: ${intent.action}, result: $result")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to send service command", e)
        }
    }
} 