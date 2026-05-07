package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.exoplayer.ExoPlayer
import com.beardytop.mitzmode.util.VideoManager
import kotlinx.coroutines.delay

@Composable
fun BlessedAnimation(
    onFinished: () -> Unit
) {
    val context = LocalContext.current
    val videoManager = remember { VideoManager.getInstance(context) }
    var currentColor by remember { mutableStateOf(0f) }
    var playbackError by remember { mutableStateOf(false) }
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(50)
            currentColor = (currentColor + 0.02f) % 1f
        }
    }

    LaunchedEffect(Unit) {
        try {
            val player = videoManager.createRewardPlayer(
                videoAsset = "tzaddik.mp4",
                onComplete = onFinished,
                onError = { error ->
                    playbackError = true
                    onFinished()
                }
            )
            exoPlayer = player
            player?.playWhenReady = true
        } catch (e: Exception) {
            playbackError = true
            onFinished()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            // VideoManager handles cleanup
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Video player using TextureView instead of PlayerView
        if (!playbackError && exoPlayer != null) {
            // Black background to provide letterboxing
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { context ->
                        videoManager.createTextureView(context, isBackground = false)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f) // Portrait aspect ratio
                )
            }
        }

        // Rainbow flashing text - now translatable
        TranslatableText(
            text = "BLESSED!!",
            color = Color.hsv(currentColor * 360, 1f, 1f),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 128.dp)
        )

        if (playbackError) {
            TranslatableText(
                text = "Error playing video",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }
    }
} 