package com.beardytop.beatzaddik

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController {
    val deps by ChecklistEmbedBridge.depsFlow.collectAsState()
    LaunchedEffect(Unit) {
        ChecklistEmbedBridge.ensureDependencies()
    }
    val ready = deps
    if (ready == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        App(deps = ready)
    }
}

/** Embedded Daily Mitzvot Checklist inside Mitz Mode iOS (same as Android). */
fun EmbeddedChecklistViewController(onClose: () -> Unit) = ComposeUIViewController {
    val deps by ChecklistEmbedBridge.depsFlow.collectAsState()
    val mitzvotCount by ChecklistEmbedBridge.mitzvotCountFlow.collectAsState()
    LaunchedEffect(Unit) {
        ChecklistEmbedBridge.ensureDependencies()
    }
    val ready = deps
    if (ready == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        App(
            deps = ready,
            embeddedMode = true,
            onRequestClose = onClose,
            mitzvotCount = mitzvotCount,
            onChecklistItemChecked = { itemId, title ->
                ChecklistEmbedBridge.notifyChecklistItemChecked(itemId, title)
            },
        )
    }
}
