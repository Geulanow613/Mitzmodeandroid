package com.beardytop.mitzmode.ui.components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.beardytop.mitzmode.utils.openUrl

/**
 * Displays a tappable link. Tap opens the URL; long-press shows a menu with
 * "Open link" and "Copy URL" so users can inspect the destination first.
 *
 * The display label runs through [TranslatableText] so it participates in the
 * app's translation flow. The URL itself is never translated.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinkText(
    displayText: String,
    url: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    textAlign: TextAlign? = null,
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    var showMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        TranslatableText(
            text = displayText,
            style = style,
            textAlign = textAlign,
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { context.openUrl(url) },
                    onLongClick = { showMenu = true }
                )
        )
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Open link") },
                onClick = {
                    showMenu = false
                    context.openUrl(url)
                }
            )
            DropdownMenuItem(
                text = { Text("Copy URL") },
                onClick = {
                    showMenu = false
                    clipboard.setText(AnnotatedString(url))
                    Toast.makeText(context, "URL copied", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
