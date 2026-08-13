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
    private var capabilitiesChecked = false
    private var isLowMemoryDevice = false
    private var backgroundPlayerReady = AtomicBoolean(false)
    private var rewardPlayerReady = AtomicBoolean(false)
    private var rewardSurface: Surface? = null
    /** Surface arrived before [rewardPlayer]; wired when the player is created. */
    private var pendingRewardSurfaceTexture: SurfaceTexture? = null
    private var pendingRewardSurfaceWidth: Int = 0
    private var pendingRewardSurfaceHeight: Int = 0
    private var backgroundSurface: Surface? = null
    /** Ensures we only call [Player.prepare] once per background player instance (after surface wiring). */
    private var backgroundPrepareStarted = false
    /** Reward clip: [Player.prepare] starts immediately; surface attach completes wiring. */
    private var rewardPrepareStarted = false
    /** User requested play; applied once surface exists and player is [Player.STATE_READY]. */
    private var rewardPlayPending = false
    private var prewarmedAsset: String? = null
    private var rewardOnComplete: (() -> Unit)? = null
    private var rewardOnError: ((PlaybackException) -> Unit)? = null
    /** When true, reward prewarm is suppressed so the embedded checklist can use the codec/GC budget. */
    private var checklistOpen = false
    
    companion object {
        @Volatile
        private var INSTANCE: VideoManager? = null
        
        fun getInstance(context: Context): VideoManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: VideoManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private fun ensureDeviceCapabilitiesChecked() {
        if (capabilitiesChecked) return
        capabilitiesChecked = true
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
        ensureDeviceCapabilitiesChecked()
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
                    // Do not prepare() here — video surface must attach first (see prepareBackgroundPlayer).
                }
            
            Log.d(TAG, "Background player created successfully")
            return backgroundPlayer
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create background player", e)
            return null
        }
    }
    
    /**
     * Call after the background [TextureView] surface is wired via [setVideoSurface]
     * so decoding starts only once a surface exists.
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

        if (rewardPlayer != null && prewarmedAsset == videoAsset && !rewardPlayPending) {
            bindRewardCallbacks(onComplete, onError)
            volume?.let { rewardPlayer?.volume = it }
            prewarmedAsset = null
            rewardPlayer?.seekTo(0)
            attachPendingRewardSurfaceIfAny()
            Log.d(TAG, "Reusing prewarmed reward player for $videoAsset")
            return rewardPlayer
        }

        val savedPendingTexture = pendingRewardSurfaceTexture
        val savedPendingW = pendingRewardSurfaceWidth
        val savedPendingH = pendingRewardSurfaceHeight

        // Release existing reward player first
        releaseRewardPlayer()
        pendingRewardSurfaceTexture = savedPendingTexture
        pendingRewardSurfaceWidth = savedPendingW
        pendingRewardSurfaceHeight = savedPendingH
        prewarmedAsset = null
        bindRewardCallbacks(onComplete, onError)

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
                    videoScalingMode = androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT

                    addListener(rewardPlayerListener)
                }

            startRewardPrepare()
            attachPendingRewardSurfaceIfAny()
            Log.d(TAG, "Reward player created successfully for $videoAsset")
            return rewardPlayer

        } catch (e: Exception) {
            Log.e(TAG, "Failed to create reward player", e)
            return null
        }
    }

    fun setChecklistOpen(open: Boolean) {
        checklistOpen = open
        if (open) cancelIdlePrewarm()
    }

    /** Drop a prewarmed (not playing) reward player to free codec and memory. */
    fun cancelIdlePrewarm() {
        if (rewardPlayPending) return
        if (rewardPlayer == null && prewarmedAsset == null) return
        releaseRewardPlayer()
        Log.d(TAG, "Cancelled idle reward prewarm")
    }

    /**
     * Demux/decode the next likely reward clip while the user is still on the home screen.
     * Safe to call from the main thread when idle.
     */
    fun prewarmRewardPlayer(videoAsset: String) {
        ensureDeviceCapabilitiesChecked()
        if (isLowMemoryDevice) return
        if (checklistOpen) return
        if (rewardPlayPending) return
        if (prewarmedAsset == videoAsset && rewardPlayer != null) return

        releaseRewardPlayer()
        bindRewardCallbacks({}, {})
        val context = contextRef.get() ?: return

        try {
            rewardPlayer = ExoPlayer.Builder(context)
                .setHandleAudioBecomingNoisy(false)
                .build().apply {
                    val dataSourceFactory = DefaultDataSource.Factory(context)
                    val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri("asset:///$videoAsset"))
                    setMediaSource(source)
                    playWhenReady = false
                    videoScalingMode = androidx.media3.common.C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                    addListener(rewardPlayerListener)
                }
            prewarmedAsset = videoAsset
            startRewardPrepare()
            Log.d(TAG, "Prewarming reward player for $videoAsset")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to prewarm reward player", e)
            releaseRewardPlayer()
        }
    }

    private val rewardPlayerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            when (state) {
                Player.STATE_READY -> {
                    Log.d(TAG, "Reward video ready")
                    rewardPlayerReady.set(true)
                    tryStartRewardPlayback()
                }
                Player.STATE_ENDED -> {
                    Log.d(TAG, "Reward video completed")
                    rewardOnComplete?.invoke()
                }
                Player.STATE_BUFFERING -> {
                    Log.d(TAG, "Reward video buffering")
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.e(TAG, "Reward video error: ${error.message}", error)
            rewardPlayerReady.set(false)
            rewardOnError?.invoke(error)
        }
    }

    private fun bindRewardCallbacks(
        onComplete: () -> Unit,
        onError: (PlaybackException) -> Unit,
    ) {
        rewardOnComplete = onComplete
        rewardOnError = onError
    }

    private fun startRewardPrepare() {
        rewardPlayer?.let { player ->
            if (rewardPrepareStarted) return
            if (player.playbackState != Player.STATE_IDLE) return
            rewardPrepareStarted = true
            player.prepare()
            Log.d(TAG, "Reward player prepare()")
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

    /** TextureView for looping home background — avoids PlayerView / exo_player_control_view inflation. */
    fun createBackgroundTextureView(context: Context): TextureView {
        return TextureView(context).apply {
            alpha = 1f
            surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                    attachBackgroundSurface(surface)
                }

                override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                    Log.d(TAG, "Background surface texture size changed: ${width}x${height}")
                }

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    Log.d(TAG, "Background surface texture destroyed")
                    try {
                        backgroundPlayer?.clearVideoSurface()
                    } catch (e: Exception) {
                        Log.e(TAG, "Error clearing background video surface", e)
                    }
                    backgroundSurface?.release()
                    backgroundSurface = null
                    return true
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
            }
        }
    }

    fun attachBackgroundSurface(surfaceTexture: SurfaceTexture) {
        backgroundSurface?.release()
        val videoSurface = Surface(surfaceTexture)
        backgroundSurface = videoSurface
        val player = backgroundPlayer ?: return
        player.setVideoSurface(videoSurface)
        prepareBackgroundPlayer()
    }

    /** Call when the reward [TextureView] is on screen — covers surface-ready-before-player races. */
    fun bindRewardTextureView(textureView: TextureView) {
        val surfaceTexture = textureView.surfaceTexture ?: return
        val width = textureView.width.takeIf { it > 0 } ?: pendingRewardSurfaceWidth
        val height = textureView.height.takeIf { it > 0 } ?: pendingRewardSurfaceHeight
        attachRewardSurface(
            surfaceTexture,
            width.coerceAtLeast(1),
            height.coerceAtLeast(1),
        )
    }

    private fun attachRewardSurface(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        if (width > 0 && height > 0) {
            surfaceTexture.setDefaultBufferSize(width, height)
        }
        Log.d(TAG, "Surface texture available: ${width}x${height}")
        if (rewardPlayer == null) {
            pendingRewardSurfaceTexture = surfaceTexture
            pendingRewardSurfaceWidth = width
            pendingRewardSurfaceHeight = height
            Log.d(TAG, "Reward surface pending — player not ready yet")
            return
        }
        applyRewardSurface(surfaceTexture)
    }

    private fun attachPendingRewardSurfaceIfAny() {
        val surfaceTexture = pendingRewardSurfaceTexture ?: return
        pendingRewardSurfaceTexture = null
        applyRewardSurface(surfaceTexture)
    }

    private fun applyRewardSurface(surfaceTexture: SurfaceTexture) {
        rewardSurface?.release()
        val videoSurface = Surface(surfaceTexture)
        rewardSurface = videoSurface
        val player = rewardPlayer ?: return
        player.setVideoSurface(videoSurface)
        if (!rewardPrepareStarted && player.playbackState == Player.STATE_IDLE) {
            startRewardPrepare()
        }
        tryStartRewardPlayback()
    }

    private fun clearPendingRewardSurface() {
        pendingRewardSurfaceTexture = null
        pendingRewardSurfaceWidth = 0
        pendingRewardSurfaceHeight = 0
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
     * instance so [resumeBackgroundPlayer] can reconnect via the background [TextureView].
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
        backgroundSurface?.release()
        backgroundSurface = null
    }
    
    fun releaseRewardPlayer() {
        rewardPlayPending = false
        rewardPrepareStarted = false
        prewarmedAsset = null
        rewardOnComplete = null
        rewardOnError = null
        clearPendingRewardSurface()
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
    
    fun isLowMemoryDevice(): Boolean {
        ensureDeviceCapabilitiesChecked()
        return isLowMemoryDevice
    }

    /** Used to detect when the UI still holds a player ref but [releaseBackgroundPlayer] already ran. */
    fun hasBackgroundPlayer(): Boolean = backgroundPlayer != null
} 