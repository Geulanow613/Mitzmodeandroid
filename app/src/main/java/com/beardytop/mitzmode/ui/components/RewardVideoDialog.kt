package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import com.beardytop.mitzmode.viewmodel.MitzModeViewModel
import kotlinx.coroutines.delay

@Composable
fun RewardVideoDialog(
    videoNumber: Int,
    onDismiss: () -> Unit,
    levelName: String? = null // Add level name parameter
) {
    val context = LocalContext.current
    val videoManager = remember { VideoManager.getInstance(context) }
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    var muted by remember(videoNumber) { mutableStateOf(false) }
    val isFinalReward = videoNumber == MitzModeViewModel.FINAL_REWARD_VIDEO_ID
    val normalVolume = remember { 1f }

    LaunchedEffect(videoNumber) {
        val asset = if (isFinalReward) "finalreward.mp4" else "mitzmodenew$videoNumber.mp4"
        val player = videoManager.createRewardPlayer(
            videoAsset = asset,
            onComplete = onDismiss,
            onError = {
                onDismiss()
            },
            volume = if (isFinalReward) MitzModeViewModel.FINAL_REWARD_PLAYER_VOLUME_UNMUTED else null
        )
        if (player == null) {
            onDismiss()
            return@LaunchedEffect
        }
        exoPlayer = player
        // Surface attach + prepare happen in [VideoManager.attachRewardSurface]; play starts when ready.
        videoManager.startRewardPlayback()
    }

    LaunchedEffect(exoPlayer, muted, normalVolume) {
        val vol = if (muted) 0f else normalVolume
        videoManager.setRewardPlayerVolume(vol)
    }

    DisposableEffect(videoNumber) {
        onDispose {
            videoManager.releaseRewardPlayer()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            securePolicy = SecureFlagPolicy.SecureOn
        )
    ) {
        // Black background to provide letterboxing
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            exoPlayer?.let {
                AndroidView(
                    factory = { ctx ->
                        videoManager.createTextureView(ctx)
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f)
                )
            }

            levelName?.let { level ->
                if (!isFinalReward) {
                    LevelUpOverlay(level = level)
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { muted = !muted },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White.copy(alpha = 0.92f)
                    )
                ) {
                    Icon(
                        imageVector = if (muted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = if (muted) "Unmute" else "Mute"
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White.copy(alpha = 0.92f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close"
                    )
                }
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