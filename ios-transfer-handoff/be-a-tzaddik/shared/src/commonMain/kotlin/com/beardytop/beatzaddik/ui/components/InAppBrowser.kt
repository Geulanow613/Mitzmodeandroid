package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.beardytop.beatzaddik.platform.PlatformBackHandler
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

const val TORAH_ANYTIME_URL = "https://www.torahanytime.com"

val LocalOpenInAppBrowser = staticCompositionLocalOf<((String) -> Unit)?> { null }

fun shouldOpenInAppBrowser(uri: String): Boolean {
    val trimmed = uri.trim()
    if (!trimmed.startsWith("http://", ignoreCase = true) &&
        !trimmed.startsWith("https://", ignoreCase = true)
    ) {
        return false
    }
    val lower = trimmed.lowercase()
    // Store listings and similar must stay with the system handler.
    if ("play.google.com" in lower) return false
    if ("apps.apple.com" in lower) return false
    if ("itunes.apple.com" in lower) return false
    return true
}

@Composable
fun ProvideInAppBrowser(content: @Composable () -> Unit) {
    var browserUrl by remember { mutableStateOf<String?>(null) }
    CompositionLocalProvider(LocalOpenInAppBrowser provides { browserUrl = it }) {
        content()
        val url = browserUrl
        if (url != null) {
            InAppBrowserOverlay(
                url = url,
                onDismiss = { browserUrl = null },
            )
        }
    }
}

@Composable
fun InAppBrowserOverlay(
    url: String,
    onDismiss: () -> Unit,
    title: String? = null,
) {
    PlatformBackHandler(onBack = onDismiss)
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(TzaddikColors.ParchBase),
        ) {
            InAppBrowserToolbar(
                title = title ?: hostLabel(url),
                onClose = onDismiss,
            )
            PlatformWebView(
                url = url,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
        }
    }
}

/** Full-tab Learn page — TorahAnytime without a close chrome (bottom nav stays). */
@Composable
fun LearnWebScreen(
    url: String = TORAH_ANYTIME_URL,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TzaddikColors.ParchBase),
    ) {
        PlatformWebView(
            url = url,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun InAppBrowserToolbar(
    title: String,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(TzaddikColors.NavyDeep)
            .statusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onClose) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
            )
        }
        AppText(
            title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White,
            enableTerms = false,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 12.dp),
        )
    }
}

private fun hostLabel(url: String): String {
    val withoutScheme = url
        .removePrefix("https://")
        .removePrefix("http://")
        .removePrefix("www.")
    return withoutScheme.substringBefore('/').ifBlank { "Browser" }
}
