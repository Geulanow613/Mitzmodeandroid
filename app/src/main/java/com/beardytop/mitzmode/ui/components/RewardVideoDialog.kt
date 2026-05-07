package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.media3.exoplayer.ExoPlayer
import com.beardytop.mitzmode.util.VideoManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RewardVideoDialog(
    videoNumber: Int,
    onDismiss: () -> Unit,
    levelName: String? = null // Add level name parameter
) {
    val context = LocalContext.current
    val videoManager = remember { VideoManager.getInstance(context) }
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    var isPlayerReady by remember { mutableStateOf(false) }

    LaunchedEffect(videoNumber) {
        val player = videoManager.createRewardPlayer(
            videoAsset = "mitzmodenew$videoNumber.mp4",
            onComplete = onDismiss,
            onError = { error ->
                // Handle error gracefully
                onDismiss()
            }
        )
        exoPlayer = player
        
        // Wait for player to be ready before starting playback
        player?.let { p ->
            // Check if already ready
            if (p.playbackState == androidx.media3.common.Player.STATE_READY) {
                isPlayerReady = true
                // Add delay to ensure surface is ready
                delay(400)
                videoManager.startRewardPlayback()
            } else {
                // Listen for ready state
                p.addListener(object : androidx.media3.common.Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == androidx.media3.common.Player.STATE_READY && !isPlayerReady) {
                            isPlayerReady = true
                            // Start playback after surface is ready
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(400)
                                videoManager.startRewardPlayback()
                            }
                        }
                    }
                })
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            // VideoManager handles cleanup
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            securePolicy = SecureFlagPolicy.SecureOn
        )
    ) {
        // Black background to provide letterboxing
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            exoPlayer?.let { player ->
                // Video with proper aspect ratio centered in black background
                AndroidView(
                    factory = { context ->
                        videoManager.createTextureView(context, isBackground = false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f) // Portrait aspect ratio
                )
            }
            
            // Add level up animation overlay ON TOP of everything
            levelName?.let { level ->
                LevelUpOverlay(level = level)
            }
        }
    }
}

@Composable
private fun LevelUpOverlay(level: String) {
    var currentColorIndex by remember { mutableStateOf(0) }
    val rainbowColors = listOf(
        Color(0xFFFF0000), // Red
        Color(0xFFFF7F00), // Orange
        Color(0xFFFFFF00), // Yellow
        Color(0xFF00FF00), // Green
        Color(0xFF0000FF), // Blue
        Color(0xFF4B0082), // Indigo
        Color(0xFF9400D3)  // Violet
    )
    
    val textColor = rainbowColors[currentColorIndex]
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(100) // Change color every 100ms
            currentColorIndex = (currentColorIndex + 1) % rainbowColors.size
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TranslatableText(
                text = "New Level Achieved!",
                style = MaterialTheme.typography.headlineLarge,
                color = textColor,
                fontSize = 36.sp,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = level,
                style = MaterialTheme.typography.headlineMedium,
                color = textColor,
                fontSize = 32.sp,
                textAlign = TextAlign.Center
            )
        }
    }
} 