@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.beardytop.beatzaddik.ui.components

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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.CValue
import kotlinx.cinterop.cValue
import platform.AVFoundation.AVLayerVideoGravityResizeAspect
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.setMuted
import platform.CoreGraphics.CGRect
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSBundle
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIColor
import platform.UIKit.UIView

/**
 * Full-bleed final-reward clip via [AVPlayerLayer].
 *
 * Do not host [platform.AVKit.AVPlayerViewController]'s bare `.view` in [UIKitView] —
 * the VC is never parented, so iOS often plays audio with a black frame.
 * Mirrors mitzi `RewardVideoPlayer` / `PlayerLayoutView`.
 */
private class PlayerLayoutView(
    frame: CValue<CGRect> = cValue { CGRectZero },
) : UIView(frame = frame) {
    var hostedLayer: AVPlayerLayer? = null

    override fun layoutSubviews() {
        super.layoutSubviews()
        hostedLayer?.setFrame(bounds)
    }
}

@Composable
actual fun FinalRewardVideoOverlay(
    visible: Boolean,
    onComplete: () -> Unit,
) {
    if (!visible) return

    val videoUrl = remember {
        NSBundle.mainBundle.URLForResource("finalreward", withExtension = "mp4")
    }
    if (videoUrl == null) {
        LaunchedEffect(Unit) { onComplete() }
        return
    }

    val onCompleteLatest by rememberUpdatedState(onComplete)
    val playerHolder = remember { arrayOfNulls<AVPlayer>(1) }
    val observerHolder = remember { arrayOfNulls<Any>(1) }
    var muted by remember { mutableStateOf(true) }

    LaunchedEffect(muted) {
        playerHolder[0]?.setMuted(muted)
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        UIKitView(
            factory = {
                val item = AVPlayerItem(uRL = videoUrl)
                val player = AVPlayer(playerItem = item)
                player.setMuted(true)
                playerHolder[0] = player

                val layer = AVPlayerLayer.playerLayerWithPlayer(player).apply {
                    videoGravity = AVLayerVideoGravityResizeAspect
                }
                val host = PlayerLayoutView().apply {
                    backgroundColor = UIColor.blackColor
                    hostedLayer = layer
                    this.layer.addSublayer(layer)
                }

                observerHolder[0] = NSNotificationCenter.defaultCenter.addObserverForName(
                    name = AVPlayerItemDidPlayToEndTimeNotification,
                    `object` = item,
                    queue = NSOperationQueue.mainQueue,
                ) { _ ->
                    onCompleteLatest()
                }
                player.play()
                host
            },
            modifier = Modifier.fillMaxSize(),
            onRelease = { view ->
                (view as? PlayerLayoutView)?.hostedLayer?.player = null
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
            observerHolder[0]?.let { NSNotificationCenter.defaultCenter.removeObserver(it) }
            observerHolder[0] = null
            playerHolder[0]?.pause()
            playerHolder[0] = null
        }
    }
}
