package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.R
import com.beardytop.mitzmode.utils.openUrl
import com.beardytop.mitzmode.viewmodel.MusicPlayerViewModel
import kotlinx.coroutines.delay

@Composable
fun MusicPlayerDialog(
    onDismiss: () -> Unit,
    viewModel: MusicPlayerViewModel = hiltViewModel()
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val isLoaded by viewModel.isLoaded.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    
    // Auto-update position while playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(1000) // Update every second
            viewModel.updateCurrentPosition()
        }
    }
    
    // Only load song if not already loaded
    LaunchedEffect(isLoaded) {
        if (!isLoaded) {
            viewModel.loadSong()
        }
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TranslatableText(
                        text = "🎵 Official App Song",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Music note animation or static icon
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            if (isPlaying) 
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else 
                                MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isPlaying) "🎵" else "🎼",
                        fontSize = 48.sp,
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Progress bar
                if (isLoaded && duration > 0) {
                    Column {
                        LinearProgressIndicator(
                            progress = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Time display
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = formatTime(currentPosition),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = formatTime(duration),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    // Loading indicator
                    if (!isLoaded) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            TranslatableText(
                                text = "Loading song...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        // Placeholder when loaded but no duration yet
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Control buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stop button
                    IconButton(
                        onClick = { viewModel.stop() },
                        enabled = isLoaded,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                if (isLoaded) MaterialTheme.colorScheme.errorContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                    ) {
                        Icon(
                            Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = if (isLoaded) MaterialTheme.colorScheme.onErrorContainer
                                  else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    // Reload button (if not loaded)
                    if (!isLoaded) {
                        IconButton(
                            onClick = { viewModel.loadSong() },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Reload",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    // Play/Pause button (larger)
                    IconButton(
                        onClick = { 
                            if (isPlaying) viewModel.pause() else viewModel.play()
                        },
                        enabled = isLoaded,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(
                                if (isLoaded) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = if (isLoaded) MaterialTheme.colorScheme.onPrimary
                                  else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    // Spacer to balance the layout
                    Spacer(modifier = Modifier.size(48.dp))
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description
                TranslatableText(
                    text = "Enjoy the official Mitz Mode song!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Performer credit
                Text(
                    text = "Performed by G.E.U.L.A © 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(16.dp))

                StreamingPlatformLinks()
            }
        }
    }
}

@Composable
private fun StreamingPlatformLinks() {
    val context = LocalContext.current
    val platforms = listOf(
        StreamingPlatform(
            iconRes = R.drawable.ic_apple_music,
            label = "Apple Music",
            url = "https://music.apple.com/us/artist/geula/1615973719",
            logoWidth = 200.dp,
            logoHeight = 36.dp,
            showLabel = false,
        ),
        StreamingPlatform(
            iconRes = R.drawable.ic_spotify,
            label = "Spotify",
            url = "https://open.spotify.com/artist/2MSUpcrPITpkisaogXF5W9",
            logoWidth = 40.dp,
            logoHeight = 40.dp,
        ),
        StreamingPlatform(
            iconRes = R.drawable.ic_amazon_music,
            label = "Amazon Music",
            url = "https://www.amazon.com/music/player/artists/B0FCGB4RGM/g-e-u-l-a",
            logoWidth = 180.dp,
            logoHeight = 32.dp,
            showLabel = false,
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TranslatableText(
            text = "Listen to more music from G.E.U.L.A",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            platforms.forEach { platform ->
                StreamingPlatformChip(
                    platform = platform,
                    onClick = { context.openUrl(platform.url) }
                )
            }
        }
    }
}

private data class StreamingPlatform(
    val iconRes: Int,
    val label: String,
    val url: String,
    val logoWidth: Dp,
    val logoHeight: Dp,
    val showLabel: Boolean = true,
)

@Composable
private fun StreamingPlatformChip(
    platform: StreamingPlatform,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Image(
            painter = painterResource(platform.iconRes),
            contentDescription = platform.label,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(width = platform.logoWidth, height = platform.logoHeight)
        )
        if (platform.showLabel) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = platform.label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

private fun formatTime(milliseconds: Int): String {
    val seconds = milliseconds / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
} 