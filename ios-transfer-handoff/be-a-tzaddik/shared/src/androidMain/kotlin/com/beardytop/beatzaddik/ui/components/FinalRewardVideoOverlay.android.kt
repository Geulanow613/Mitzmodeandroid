package com.beardytop.beatzaddik.ui.components

import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
        onComplete()
        return
    }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoPath(videoFile.absolutePath)
                    setOnCompletionListener { onComplete() }
                    setOnErrorListener { _, _, _ ->
                        onComplete()
                        true
                    }
                    start()
                }
            },
        )
    }

    DisposableEffect(Unit) {
        onDispose { }
    }
}
