package com.beardytop.beatzaddik.ui.components

import android.media.MediaPlayer
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import java.io.File

@Composable
actual fun FinalRewardVideoOverlay(
    visible: Boolean,
    onComplete: () -> Unit,
) {
    if (!visible) return
    val context = LocalContext.current
    val videoFile = remember {
        runCatching {
            val out = File(context.cacheDir, "finalreward.mp4")
            if (!out.exists() || out.length() == 0L) {
                context.assets.open("finalreward.mp4").use { input ->
                    out.outputStream().use { output -> input.copyTo(output) }
                }
            }
            out
        }.getOrNull()
    }

    if (videoFile == null) {
        LaunchedEffect(Unit) { onComplete() }
        return
    }

    var muted by remember { mutableStateOf(true) }
    val mediaPlayerHolder = remember { arrayOfNulls<MediaPlayer>(1) }

    LaunchedEffect(muted) {
        val vol = if (muted) 0f else 1f
        mediaPlayerHolder[0]?.setVolume(vol, vol)
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoPath(videoFile.absolutePath)
                    setOnPreparedListener { mp ->
                        mediaPlayerHolder[0] = mp
                        mp.setVolume(0f, 0f)
                        start()
                    }
                    setOnCompletionListener { onComplete() }
                    setOnErrorListener { _, _, _ ->
                        onComplete()
                        true
                    }
                }
            },
            update = {
                val vol = if (muted) 0f else 1f
                mediaPlayerHolder[0]?.setVolume(vol, vol)
            },
        )

        IconButton(
            onClick = { muted = !muted },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 16.dp, bottom = 20.dp)
                .size(48.dp)
                .background(Color.Black.copy(alpha = 0.45f), CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.White.copy(alpha = 0.95f),
            ),
        ) {
            Icon(
                imageVector = if (muted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = if (muted) "Unmute" else "Mute",
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayerHolder[0] = null
        }
    }
}
