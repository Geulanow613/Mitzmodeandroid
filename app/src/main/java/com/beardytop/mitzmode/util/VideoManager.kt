package com.beardytop.mitzmode.util

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.Log
import android.view.Surface
import android.view.TextureView
import androidx.compose.ui.graphics.toArgb
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "VideoManager"

class VideoManager private constructor(context: Context) {
    private val contextRef = WeakReference(context)
    private var backgroundPlayer: ExoPlayer? = null
    private var rewardPlayer: ExoPlayer? = null
    private var isLowMemoryDevice = false
    private var backgroundPlayerReady = AtomicBoolean(false)
    private var rewardPlayerReady = AtomicBoolean(false)
    private var backgroundSurface: Surface? = null
    private var rewardSurface: Surface? = null
    
    companion object {
        @Volatile
        private var INSTANCE: VideoManager? = null
        
        fun getInstance(context: Context): VideoManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: VideoManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    init {
        checkDeviceCapabilities()
    }
    
    private fun checkDeviceCapabilities() {
        contextRef.get()?.let { context ->
            isLowMemoryDevice = !DeviceCapabilityChecker.canHandleVideoBackground(context)
            Log.d(TAG, "Device capability check - isLowMemoryDevice: $isLowMemoryDevice")
        }
    }
    
    fun createBackgroundPlayer(
        videoAsset: String,
        onReady: () -> Unit = {},
        onComplete: () -> Unit = {},
        onError: (PlaybackException) -> Unit = {}
    ): ExoPlayer? {
        if (isLowMemoryDevice) {
            Log.d(TAG, "Skipping background video creation - low memory device")
            return null
        }
        
        val context = contextRef.get() ?: return null
        
        // Release existing background player first
        releaseBackgroundPlayer()
        
        try {
            backgroundPlayer = ExoPlayer.Builder(context)
                .setHandleAudioBecomingNoisy(false)
                .build().apply {
                    
                    val dataSourceFactory = DefaultDataSource.Factory(context)
                    val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri("asset:///$videoAsset"))
                    
                    setMediaSource(source)
                    volume = 0f
                    repeatMode = Player.REPEAT_MODE_ONE
                    playWhenReady = false
                    // Set video scaling mode to fit with letterboxing (black bars) instead of stretching
                    videoScalingMode = androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                    
                    addListener(object : Player.Listener {
                        override fun onVideoSizeChanged(videoSize: VideoSize) {
                            Log.d(TAG, "Background video size changed: ${videoSize.width}x${videoSize.height}")
                        }
                        
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            when (playbackState) {
                                Player.STATE_READY -> {
                                    Log.d(TAG, "Background video ready")
                                    backgroundPlayerReady.set(true)
                                    onReady()
                                }
                                Player.STATE_ENDED -> {
                                    Log.d(TAG, "Background video ended - restarting")
                                    seekTo(0)
                                    if (playWhenReady) {
                                        play()
                                    }
                                    onComplete()
                                }
                                Player.STATE_BUFFERING -> {
                                    Log.d(TAG, "Background video buffering")
                                }
                                Player.STATE_IDLE -> {
                                    Log.d(TAG, "Background video idle")
                                }
                            }
                        }
                        
                        override fun onPlayerError(error: PlaybackException) {
                            Log.e(TAG, "Background video error: ${error.message}", error)
                            backgroundPlayerReady.set(false)
                            onError(error)
                        }
                    })
                    
                    prepare()
                }
            
            Log.d(TAG, "Background player created successfully")
            return backgroundPlayer
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create background player", e)
            return null
        }
    }
    
    fun createRewardPlayer(
        videoAsset: String,
        onComplete: () -> Unit = {},
        onError: (PlaybackException) -> Unit = {}
    ): ExoPlayer? {
        val context = contextRef.get() ?: return null
        
        // Release existing reward player first
        releaseRewardPlayer()
        
        try {
            rewardPlayer = ExoPlayer.Builder(context)
                .setHandleAudioBecomingNoisy(false)
                .build().apply {
                    
                    val dataSourceFactory = DefaultDataSource.Factory(context)
                    val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri("asset:///$videoAsset"))
                    
                    setMediaSource(source)
                    playWhenReady = false
                    // Set video scaling mode to fit with letterboxing (black bars) instead of stretching
                    videoScalingMode = androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                    
                    addListener(object : Player.Listener {
                        override fun onVideoSizeChanged(videoSize: VideoSize) {
                            Log.d(TAG, "Reward video size changed: ${videoSize.width}x${videoSize.height}")
                        }
                        
                        override fun onPlaybackStateChanged(state: Int) {
                            when (state) {
                                Player.STATE_READY -> {
                                    Log.d(TAG, "Reward video ready")
                                    rewardPlayerReady.set(true)
                                }
                                Player.STATE_ENDED -> {
                                    Log.d(TAG, "Reward video completed")
                                    onComplete()
                                }
                                Player.STATE_BUFFERING -> {
                                    Log.d(TAG, "Reward video buffering")
                                }
                            }
                        }
                        
                        override fun onPlayerError(error: PlaybackException) {
                            Log.e(TAG, "Reward video error: ${error.message}", error)
                            rewardPlayerReady.set(false)
                            onError(error)
                        }
                    })
                    
                    prepare()
                }
            
            Log.d(TAG, "Reward player created successfully")
            return rewardPlayer
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create reward player", e)
            return null
        }
    }
    
        fun createTextureView(context: Context, isBackground: Boolean = false): TextureView {
        return TextureView(context).apply {
            // For background videos, start invisible to prevent flicker
            // For reward videos, start visible since they have black background now
            alpha = if (isBackground) 0f else 1f
            
            surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                    Log.d(TAG, "Surface texture available: ${width}x${height}")
                    val videoSurface = Surface(surface)
                    
                    if (isBackground) {
                        backgroundSurface = videoSurface
                        backgroundPlayer?.setVideoSurface(videoSurface)
                        // Only fade in background videos
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(200L)
                            animate().alpha(1f).setDuration(300).start()
                        }
                    } else {
                        rewardSurface = videoSurface
                        rewardPlayer?.setVideoSurface(videoSurface)
                        // Reward videos stay visible - no fade needed with black background
                    }
                }
                
                override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                    Log.d(TAG, "Surface texture size changed: ${width}x${height}")
                    // Size changed - no action needed, let video maintain natural aspect ratio
                }
                
                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    Log.d(TAG, "Surface texture destroyed")
                    if (isBackground) {
                        backgroundSurface?.release()
                        backgroundSurface = null
                    } else {
                        rewardSurface?.release()
                        rewardSurface = null
                    }
                    return true
                }
                
                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                    // Video frame updated - no action needed
                }
            }
        }
    }
    
    fun startBackgroundPlayback() {
        backgroundPlayer?.let { player ->
            if (backgroundPlayerReady.get() && backgroundSurface != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    // Small delay to ensure surface is fully ready
                    delay(200)
                    player.playWhenReady = true
                    Log.d(TAG, "Background playback started")
                }
            }
        }
    }
    
    fun startRewardPlayback() {
        rewardPlayer?.let { player ->
            if (rewardPlayerReady.get() && rewardSurface != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    // Longer delay for reward videos to prevent flickering
                    delay(500)
                    player.playWhenReady = true
                    Log.d(TAG, "Reward playback started")
                }
            }
        }
    }
    
    fun pauseBackgroundPlayer() {
        backgroundPlayer?.let { player ->
            if (backgroundPlayerReady.get()) {
                player.pause()
            }
        }
    }
    
    fun resumeBackgroundPlayer() {
        backgroundPlayer?.let { player ->
            if (backgroundPlayerReady.get()) {
                player.play()
            }
        }
    }
    
    fun releaseBackgroundPlayer() {
        backgroundSurface?.release()
        backgroundSurface = null
        
        backgroundPlayer?.let { player ->
            try {
                player.release()
                Log.d(TAG, "Background player released")
            } catch (e: Exception) {
                Log.e(TAG, "Error releasing background player", e)
            }
        }
        backgroundPlayer = null
        backgroundPlayerReady.set(false)
    }
    
    fun releaseRewardPlayer() {
        rewardSurface?.release()
        rewardSurface = null
        
        rewardPlayer?.let { player ->
            try {
                player.release()
                Log.d(TAG, "Reward player released")
            } catch (e: Exception) {
                Log.e(TAG, "Error releasing reward player", e)
            }
        }
        rewardPlayer = null
        rewardPlayerReady.set(false)
    }
    
    fun releaseBackgroundResources() {
        Log.d(TAG, "Releasing background resources due to memory pressure")
        // Only release background player for moderate memory pressure
        releaseBackgroundPlayer()
    }
    
    fun releaseAll() {
        Log.d(TAG, "Releasing all players")
        releaseBackgroundPlayer()
        releaseRewardPlayer()
    }
    
    fun onLowMemory() {
        Log.w(TAG, "Low memory detected - releasing all players")
        isLowMemoryDevice = true
        releaseAll()
    }
    
    fun isLowMemoryDevice(): Boolean = isLowMemoryDevice
} 