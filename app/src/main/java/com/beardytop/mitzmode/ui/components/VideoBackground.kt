package com.beardytop.mitzmode.ui.components

import android.graphics.Color as AndroidColor
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.beardytop.mitzmode.util.VideoManager

private const val TAG = "VideoBackground"

@Composable
fun VideoBackground(
    videoAsset: String,
    isPlaying: Boolean,
    onVideoReady: () -> Unit = {},
    onVideoComplete: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    var hasError by remember { mutableStateOf(false) }
    var isPlayerReady by remember { mutableStateOf(false) }
    var recreateKey by remember { mutableIntStateOf(0) }
    /** True after a real [ON_PAUSE]; avoids treating initial observer sync ON_RESUME as "recover from lost player". */
    var mayNeedResumeHeal by remember { mutableStateOf(false) }
    /** When false, [PlayerView] must not hold the player (surface detached for HWUI lifecycle). */
    var surfaceAttached by remember { mutableStateOf(true) }
    val videoManager = remember { VideoManager.getInstance(context) }

    if (videoManager.isLowMemoryDevice()) {
        Log.d(TAG, "Skipping video background - low memory device")
        return
    }

    DisposableEffect(lifecycleOwner, videoManager) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    mayNeedResumeHeal = true
                    surfaceAttached = false
                    videoManager.detachBackgroundSurface()
                }
                Lifecycle.Event.ON_STOP -> {
                    surfaceAttached = false
                    videoManager.detachBackgroundSurface()
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (videoManager.isLowMemoryDevice()) return@LifecycleEventObserver
                    surfaceAttached = true
                    if (videoManager.hasBackgroundPlayer()) {
                        // Playback resumes after PlayerView reconnects in [AndroidView.update].
                    } else if (mayNeedResumeHeal) {
                        // Returned to foreground without a player (e.g. critical trim) — rebuild once.
                        exoPlayer = null
                        isPlayerReady = false
                        hasError = false
                        recreateKey++
                    }
                    mayNeedResumeHeal = false
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(videoAsset, recreateKey) {
        isPlayerReady = false
        try {
            val player = videoManager.createBackgroundPlayer(
                videoAsset = videoAsset,
                onReady = {
                    isPlayerReady = true
                    onVideoReady()
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

    LaunchedEffect(isPlaying, isPlayerReady, surfaceAttached) {
        if (!isPlayerReady || exoPlayer == null || !surfaceAttached) return@LaunchedEffect
        if (isPlaying) {
            videoManager.startBackgroundPlayback()
        } else {
            videoManager.pauseBackgroundPlayer()
        }
    }

    if (!hasError && exoPlayer != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        useController = false
                        setShowBuffering(PlayerView.SHOW_BUFFERING_NEVER)
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        setBackgroundColor(AndroidColor.BLACK)
                        setShutterBackgroundColor(AndroidColor.BLACK)
                    }
                },
                update = { playerView ->
                    if (!surfaceAttached || exoPlayer == null) {
                        if (playerView.player != null) {
                            playerView.player = null
                        }
                        return@AndroidView
                    }
                    if (playerView.player !== exoPlayer) {
                        playerView.player = exoPlayer
                    }
                    videoManager.prepareBackgroundPlayer()
                    if (isPlaying && isPlayerReady) {
                        videoManager.startBackgroundPlayback()
                    }
                },
                onRelease = { playerView ->
                    playerView.player = null
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
