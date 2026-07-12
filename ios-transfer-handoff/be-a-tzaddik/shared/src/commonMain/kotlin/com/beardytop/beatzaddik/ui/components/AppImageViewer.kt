package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import beatzaddik.shared.generated.resources.Res
import beatzaddik.shared.generated.resources.kosherhaircut
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/** Custom scheme for in-app guide images: `app-image:kosherhaircut`. */
const val APP_IMAGE_URI_PREFIX = "app-image:"

val LocalOpenAppImage = staticCompositionLocalOf<((String) -> Unit)?> { null }

fun appImageDrawable(key: String): DrawableResource? = when (key) {
    "kosherhaircut" -> Res.drawable.kosherhaircut
    else -> null
}

/** Opens http(s) links via [uriHandler]; `app-image:` keys via [openAppImage]. */
fun openChecklistUri(
    uri: String,
    uriHandler: UriHandler,
    openAppImage: ((String) -> Unit)?,
) {
    if (uri.startsWith(APP_IMAGE_URI_PREFIX)) {
        openAppImage?.invoke(uri.removePrefix(APP_IMAGE_URI_PREFIX).trim())
        return
    }
    runCatching { uriHandler.openUri(uri) }
}

@Composable
fun ProvideAppImageViewer(content: @Composable () -> Unit) {
    var imageKey by remember { mutableStateOf<String?>(null) }
    CompositionLocalProvider(LocalOpenAppImage provides { imageKey = it }) {
        content()
        val key = imageKey
        if (key != null) {
            AppImageViewerDialog(imageKey = key, onDismiss = { imageKey = null })
        }
    }
}

@Composable
private fun AppImageViewerDialog(imageKey: String, onDismiss: () -> Unit) {
    val drawable = appImageDrawable(imageKey)
    if (drawable == null) {
        onDismiss()
        return
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black),
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = "Visual haircut instructions",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 8.dp),
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp),
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = TzaddikColors.GoldBright)
            }
        }
    }
}
