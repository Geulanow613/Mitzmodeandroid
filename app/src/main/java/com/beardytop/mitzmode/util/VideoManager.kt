package com.beardytop.mitzmode.util

import android.content.Context
import android.graphics.SurfaceTexture
import android.util.Log
import android.view.Surface
import android.view.TextureView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
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
    private var rewardSurface: Surface? = null
    /** Ensures we only call [Player.prepare] once per background player instance (after surface wiring). */
    private var backgroundPrepareStarted = false
    /** Reward clip: [Player.prepare] only after [TextureView] surface exists (avoids decoder surface churn / black flash). */
    private var rewardPrepareStarted = false
    /** User requested play; applied once surface exists and player is [Player.STATE_READY]. */
    private var rewardPlayPending = false
    
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
                    videoScalingMode = androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT

                    addListener(object : Player.Listener {
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
                    // Do not prepare() here — PlayerView must attach the surface first (see prepareBackgroundPlayer).
                }
            
            Log.d(TAG, "Background player created successfully")
            return backgroundPlayer
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create background player", e)
            return null
        }
    }
    
    /**
     * Call from [androidx.media3.ui.PlayerView] after [androidx.media3.ui.PlayerView.setPlayer]
     * so the video surface exists before decoding starts (avoids black flashes / surface generation churn).
     */
    fun prepareBackgroundPlayer() {
        backgroundPlayer?.let { player ->
            if (backgroundPrepareStarted) return
            if (player.playbackState != Player.STATE_IDLE) return
            backgroundPrepareStarted = true
            player.prepare()
            Log.d(TAG, "Background player prepare() after surface attach")
        }
    }
    
    fun createRewardPlayer(
        videoAsset: String,
        onComplete: () -> Unit = {},
        onError: (PlaybackException) -> Unit = {},
        /** If non-null, sets [ExoPlayer.volume] (use `1f` when the asset should be heard). */
        volume: Float? = null
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
                    volume?.let { this.volume = it }
                    // Set video scaling mode to fit with letterboxing (black bars) instead of stretching
                    videoScalingMode = androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                    
                    addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(state: Int) {
                            when (state) {
                                Player.STATE_READY -> {
                                    Log.d(TAG, "Reward video ready")
                                    rewardPlayerReady.set(true)
                                    tryStartRewardPlayback()
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
                    // prepare() runs in [attachRewardSurface] after setVideoSurface (see background player pattern).
                }
            
            Log.d(TAG, "Reward player created successfully")
            return rewardPlayer
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create reward player", e)
            return null
        }
    }
    
    /** TextureView + Surface for reward / overlay clips only (not main background). */
    fun createTextureView(context: Context): TextureView {
        return TextureView(context).apply {
            alpha = 1f
            surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                    attachRewardSurface(surface, width, height)
                }

                override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                    Log.d(TAG, "Surface texture size changed: ${width}x${height}")
                }

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    Log.d(TAG, "Surface texture destroyed")
                    try {
                        rewardPlayer?.clearVideoSurface()
                    } catch (e: Exception) {
                        Log.e(TAG, "Error clearing reward video surface", e)
                    }
                    rewardSurface?.release()
                    rewardSurface = null
                    return true
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
            }
        }
    }

    private fun attachRewardSurface(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        Log.d(TAG, "Surface texture available: ${width}x${height}")
        rewardSurface?.release()
        val videoSurface = Surface(surfaceTexture)
        rewardSurface = videoSurface
        val player = rewardPlayer ?: return
        player.setVideoSurface(videoSurface)
        if (!rewardPrepareStarted && player.playbackState == Player.STATE_IDLE) {
            rewardPrepareStarted = true
            player.prepare()
        }
        tryStartRewardPlayback()
    }

    private fun tryStartRewardPlayback() {
        val player = rewardPlayer ?: return
        if (!rewardPlayPending) return
        if (!rewardPlayerReady.get() || rewardSurface == null) return
        rewardPlayPending = false
        player.playWhenReady = true
        Log.d(TAG, "Reward playback started")
    }
    
    fun startBackgroundPlayback() {
        val player = backgroundPlayer ?: return
        if (!backgroundPlayerReady.get()) return
        player.play()
        Log.d(TAG, "Background playback started")
    }
    
    fun startRewardPlayback() {
        rewardPlayPending = true
        tryStartRewardPlayback()
    }

    /** Live volume for the reward clip (mute UI, etc.). */
    fun setRewardPlayerVolume(volume: Float) {
        rewardPlayer?.volume = volume.coerceIn(0f, 1f)
    }
    
    fun pauseBackgroundPlayer() {
        backgroundPlayer?.let { player ->
            if (backgroundPlayerReady.get()) {
                player.pause()
            }
        }
    }

    /**
     * Pause decoding and drop the video surface before HWUI pauses the view hierarchy
     * (e.g. [LicenseActivity] overlay, [android.app.Activity.onStop]). Keeps the player
     * instance so [resumeBackgroundPlayer] can reconnect via [androidx.media3.ui.PlayerView].
     */
    fun detachBackgroundSurface() {
        backgroundPlayer?.let { player ->
            try {
                if (backgroundPlayerReady.get()) {
                    player.pause()
                }
                player.clearVideoSurface()
                Log.d(TAG, "Background player surface detached")
            } catch (e: Exception) {
                Log.e(TAG, "Error detaching background surface", e)
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
        backgroundPrepareStarted = false
    }
    
    fun releaseRewardPlayer() {
        rewardPlayPending = false
        rewardPrepareStarted = false
        rewardPlayer?.let { player ->
            try {
                player.clearVideoSurface()
            } catch (e: Exception) {
                Log.e(TAG, "Error clearing reward video surface before release", e)
            }
        }
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
        releaseAll()
    }
    
    fun isLowMemoryDevice(): Boolean = isLowMemoryDevice

    /** Used to detect when the UI still holds a player ref but [releaseBackgroundPlayer] already ran. */
    fun hasBackgroundPlayer(): Boolean = backgroundPlayer != null
} 