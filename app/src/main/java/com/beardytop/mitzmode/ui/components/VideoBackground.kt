package com.beardytop.mitzmode.ui.components

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import com.beardytop.mitzmode.util.VideoManager
import kotlinx.coroutines.delay

private const val TAG = "VideoBackground"

@Composable
fun VideoBackground(
    videoAsset: String,
    isPlaying: Boolean,
    onVideoReady: () -> Unit = {},
    onVideoComplete: () -> Unit = {}
) {
    val context = LocalContext.current
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    var hasError by remember { mutableStateOf(false) }
    var isPlayerReady by remember { mutableStateOf(false) }
    val videoManager = remember { VideoManager.getInstance(context) }
    
    // Check if device can handle video
    if (videoManager.isLowMemoryDevice()) {
        Log.d(TAG, "Skipping video background - low memory device")
        return
    }
    
    // Initialize ExoPlayer using VideoManager
    LaunchedEffect(videoAsset) {
        try {
            val player = videoManager.createBackgroundPlayer(
                videoAsset = videoAsset,
                onReady = {
                    isPlayerReady = true
                    onVideoReady()
                    // Start playback after a delay to ensure surface is ready
                    if (isPlaying) {
                        videoManager.startBackgroundPlayback()
                    }
                },
                onComplete = {
                    onVideoComplete()
                },
                onError = { error ->
                    Log.e(TAG, "Error initializing background video", error)
                    hasError = true
                }
            )
            exoPlayer = player
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create background player", e)
            hasError = true
        }
    }

    // Handle play/pause state changes
    LaunchedEffect(isPlaying, isPlayerReady) {
        if (isPlayerReady && exoPlayer != null) {
            // Add delay to ensure surface is ready
            delay(300)
            if (isPlaying) {
                videoManager.startBackgroundPlayback()
            } else {
                videoManager.pauseBackgroundPlayer()
            }
        }
    }

    // Only render if we have a player and no error
    if (!hasError && exoPlayer != null) {
        // Black background to provide letterboxing
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { context ->
                    videoManager.createTextureView(context, isBackground = true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(9f / 16f) // Portrait aspect ratio
            )
        }
    }
} 