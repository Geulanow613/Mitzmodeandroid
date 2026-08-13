package com.beardytop.mitzmode.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import com.beardytop.mitzmode.ui.components.LowEndDeviceBackground

/**
 * Renders [placeholder] until the first frame is drawn, then [content].
 * Keeps heavy composition (glossary overlay, full app tree) off the critical first frame.
 */
@Composable
fun AfterFirstFrame(
    placeholder: @Composable () -> Unit = { LowEndDeviceBackground() },
    content: @Composable () -> Unit,
) {
    var ready by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        withFrameNanos { }
        ready = true
    }
    if (ready) {
        content()
    } else {
        placeholder()
    }
}
