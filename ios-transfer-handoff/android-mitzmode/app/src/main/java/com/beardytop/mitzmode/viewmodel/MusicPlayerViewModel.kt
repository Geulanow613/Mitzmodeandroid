package com.beardytop.mitzmode.viewmodel

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beardytop.mitzmode.service.MusicPlayerService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private var musicService: MusicPlayerService? = null
    private var bound = false
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()
    
    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()
    
    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()
    
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MusicPlayerService.MusicBinder
            musicService = binder.getService()
            bound = true
            
            // Observe service state changes
            viewModelScope.launch {
                musicService?.isPlaying?.collect { _isPlaying.value = it }
            }
            viewModelScope.launch {
                musicService?.isLoaded?.collect { _isLoaded.value = it }
            }
            viewModelScope.launch {
                musicService?.currentPosition?.collect { _currentPosition.value = it }
            }
            viewModelScope.launch {
                musicService?.duration?.collect { _duration.value = it }
            }
        }
        
        override fun onServiceDisconnected(arg0: ComponentName) {
            bound = false
            musicService = null
        }
    }
    
    init {
        startAndBindService()
    }
    
    private fun startAndBindService() {
        val intent = Intent(context, MusicPlayerService::class.java)
        context.startService(intent)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
    
    fun loadSong() {
        musicService?.loadSong()
    }
    
    fun play() {
        musicService?.play()
    }
    
    fun pause() {
        musicService?.pause()
    }
    
    fun stop() {
        musicService?.stop()
    }
    
    fun seekTo(position: Int) {
        musicService?.seekTo(position)
    }
    
    fun updateCurrentPosition() {
        musicService?.getCurrentPosition()
    }
    
    override fun onCleared() {
        super.onCleared()
        if (bound) {
            context.unbindService(connection)
            bound = false
        }
    }
} 