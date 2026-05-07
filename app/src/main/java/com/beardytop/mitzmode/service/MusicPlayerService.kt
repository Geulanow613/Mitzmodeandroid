package com.beardytop.mitzmode.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.beardytop.mitzmode.MainActivity
import com.beardytop.mitzmode.R

@AndroidEntryPoint
class MusicPlayerService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private var mediaSession: MediaSessionCompat? = null
    private val binder = MusicBinder()
    private var isForegroundStarted = false
    private var shouldAutoPlay = false
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()
    
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()
    
    companion object {
        private const val TAG = "MusicPlayerService"
        private const val SONG_ASSET = "song.mp3"
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "music_player_channel"
        var instance: MusicPlayerService? = null
            private set
    }
    
    inner class MusicBinder : Binder() {
        fun getService(): MusicPlayerService = this@MusicPlayerService
    }
    
    override fun onBind(intent: Intent?): IBinder = binder
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
        initMediaSession()
        // Preload the song so it's ready when user wants to play
        loadSong()
        Log.d(TAG, "MusicPlayerService created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand called with action: ${intent?.action}")
        when (intent?.action) {
            MusicNotificationReceiver.ACTION_PLAY_PAUSE -> {
                Log.d(TAG, "Handling PLAY_PAUSE action, current playing: ${_isPlaying.value}")
                if (_isPlaying.value) {
                    pause()
                } else {
                    play()
                }
            }
            MusicNotificationReceiver.ACTION_STOP -> {
                Log.d(TAG, "Handling STOP action")
                stop()
            }
            MusicNotificationReceiver.ACTION_CLOSE -> {
                Log.d(TAG, "Handling CLOSE action")
                stopPlayback()
                stopForeground(true)
                stopSelf()
            }
            else -> {
                Log.d(TAG, "Handling legacy command or null action")
                // Handle legacy command parameter
                intent?.getStringExtra("command")?.let { command ->
                    Log.d(TAG, "Legacy command: $command")
                    when (command) {
                        "play_pause" -> {
                            if (_isPlaying.value) pause() else play()
                        }
                        "stop" -> stop()
                        "close" -> {
                            stopPlayback()
                            stopForeground(true)
                            stopSelf()
                        }
                    }
                }
            }
        }
        return START_NOT_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        release()
        mediaSession?.release()
        instance = null
        Log.d(TAG, "MusicPlayerService destroyed")
    }
    
    fun loadSong() {
        try {
            Log.d(TAG, "Starting to load song: $SONG_ASSET")
            // Only reload if not already loaded
            if (_isLoaded.value) {
                Log.d(TAG, "Song already loaded, skipping reload")
                return
            }
            releasePrevious()
            
            val assetFileDescriptor = assets.openFd(SONG_ASSET)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                
                setOnPreparedListener {
                    _duration.value = duration
                    _isLoaded.value = true
                    Log.d(TAG, "Song loaded successfully, duration: ${duration}ms")
                    // If play was requested while loading, start playing now
                    if (shouldAutoPlay) {
                        shouldAutoPlay = false
                        play()
                    }
                }
                
                setOnCompletionListener {
                    _isPlaying.value = false
                    _currentPosition.value = 0
                    updateNotification()
                    updateMediaSession()
                    Log.d(TAG, "Song playback completed")
                }
                
                setOnErrorListener { mp, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
                    _isPlaying.value = false
                    _isLoaded.value = false
                    // Try to reload once
                    try {
                        mp.reset()
                        Log.w(TAG, "Attempting to reload song after error")
                    } catch (resetError: Exception) {
                        Log.e(TAG, "Failed to reset after error", resetError)
                    }
                    true // Indicate we handled the error
                }
                
                prepareAsync()
            }
            
            assetFileDescriptor.close()
            Log.d(TAG, "Song preparation started")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load song", e)
            _isLoaded.value = false
        }
    }
    
    fun play() {
        try {
            if (_isLoaded.value && mediaPlayer != null) {
                // Song is loaded and ready to play
                mediaPlayer?.let { player ->
                    if (!player.isPlaying) {
                        player.start()
                        _isPlaying.value = true
                        if (!isForegroundStarted) {
                            startForegroundService()
                            isForegroundStarted = true
                        }
                        updateNotification()
                        updateMediaSession()
                        Log.d(TAG, "Song playback started")
                    }
                }
            } else {
                // Song needs to be loaded first
                Log.w(TAG, "Song not loaded, loading now")
                shouldAutoPlay = true
                loadSong()
                // Play will be called automatically when song finishes loading
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start playback", e)
            _isPlaying.value = false
            // Try to reload the song if play fails
            loadSong()
        }
    }
    
    fun pause() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    _isPlaying.value = false
                    _currentPosition.value = player.currentPosition
                    updateNotification()
                    updateMediaSession()
                    Log.d(TAG, "Song playback paused at position: ${player.currentPosition}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to pause playback", e)
            _isPlaying.value = false
        }
    }
    
    fun stop() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                _isPlaying.value = false
                _currentPosition.value = 0
                updateNotification()
                updateMediaSession()
                Log.d(TAG, "Song playback stopped")
                
                // Reset the player to prepared state - but keep it simple
                try {
                    player.reset()
                    _isLoaded.value = false
                    // Don't immediately reload - let play() handle reloading when needed
                    Log.d(TAG, "MediaPlayer reset, ready for reload")
                } catch (resetException: Exception) {
                    Log.e(TAG, "Failed to reset player after stop", resetException)
                    // Release and create new player
                    releasePrevious()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop playback", e)
        }
    }
    
    fun seekTo(position: Int) {
        try {
            mediaPlayer?.let { player ->
                if (_isLoaded.value && position >= 0 && position <= _duration.value) {
                    player.seekTo(position)
                    _currentPosition.value = position
                    Log.d(TAG, "Seeked to position: $position")
                } else {
                    Log.w(TAG, "Cannot seek: loaded=${_isLoaded.value}, position=$position, duration=${_duration.value}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to seek to position: $position", e)
        }
    }
    
    fun getCurrentPosition(): Int {
        return try {
            mediaPlayer?.let { player ->
                if (_isLoaded.value) {
                    val position = player.currentPosition
                    _currentPosition.value = position
                    position
                } else {
                    0
                }
            } ?: 0
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get current position", e)
            0
        }
    }
    
    fun testNotificationAction() {
        // Test function to manually trigger notification action
        Log.d(TAG, "Testing notification action manually")
        val intent = Intent(this, MusicNotificationReceiver::class.java)
        intent.action = MusicNotificationReceiver.ACTION_PLAY_PAUSE
        sendBroadcast(intent)
    }
    
    fun release() {
        releasePrevious()
        _isLoaded.value = false
        _isPlaying.value = false
        _currentPosition.value = 0
        _duration.value = 0
        Log.d(TAG, "MusicPlayerService released")
    }
    
    private fun releasePrevious() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
                mediaPlayer = null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing previous MediaPlayer", e)
        }
    }
    
    private fun stopPlayback() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                _isPlaying.value = false
                _currentPosition.value = 0
                Log.d(TAG, "Playback stopped")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop playback", e)
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controls for music playback"
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun initMediaSession() {
        mediaSession = MediaSessionCompat(this, TAG).apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            
            // Set callback for media button events
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    Log.d(TAG, "MediaSession onPlay called")
                    play()
                }
                
                override fun onPause() {
                    Log.d(TAG, "MediaSession onPause called")
                    pause()
                }
                
                override fun onStop() {
                    Log.d(TAG, "MediaSession onStop called")
                    stop()
                }
            })
            
            isActive = true
        }
        Log.d(TAG, "MediaSession initialized: ${mediaSession?.isActive}")
    }
    
    private fun startForegroundService() {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private fun updateNotification() {
        if (_isPlaying.value || _isLoaded.value) {
            val notification = createNotification()
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val playPauseIntent = Intent(this, MusicNotificationReceiver::class.java).apply {
            action = MusicNotificationReceiver.ACTION_PLAY_PAUSE
        }
        val playPausePendingIntent = PendingIntent.getBroadcast(
            this, 0, playPauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val stopIntent = Intent(this, MusicNotificationReceiver::class.java).apply {
            action = MusicNotificationReceiver.ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            this, 1, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val closeIntent = Intent(this, MusicNotificationReceiver::class.java).apply {
            action = MusicNotificationReceiver.ACTION_CLOSE
        }
        val closePendingIntent = PendingIntent.getBroadcast(
            this, 2, closeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🎵 Official App Song")
            .setContentText(if (_isPlaying.value) "Playing" else "Paused")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setDeleteIntent(closePendingIntent)
            .addAction(
                if (_isPlaying.value) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play,
                if (_isPlaying.value) "Pause" else "Play",
                playPausePendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Stop",
                stopPendingIntent
            )
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1)
                    .setMediaSession(mediaSession?.sessionToken)
            )
            .setOngoing(_isPlaying.value)
            .setAutoCancel(false)
            .build()
            
        Log.d(TAG, "Created notification with ${notification.actions?.size} actions")
        return notification
    }
    
    private fun updateMediaSession() {
        val state = if (_isPlaying.value) {
            PlaybackStateCompat.STATE_PLAYING
        } else {
            PlaybackStateCompat.STATE_PAUSED
        }
        
        val playbackState = PlaybackStateCompat.Builder()
            .setState(state, _currentPosition.value.toLong(), 1.0f)
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_STOP
            )
            .build()
        
        mediaSession?.setPlaybackState(playbackState)
    }
} 