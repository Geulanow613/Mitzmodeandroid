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
        App(deps = ready, appMode = com.beardytop.beatzaddik.domain.AppMode.Unified)
    }
}

/**
 * Full Mitz Mode + checklist shell for iOS host (checklist-first).
 * [onClose] is kept for API compatibility with older Swift hosts but is unused in unified mode.
 */
fun EmbeddedChecklistViewController(onClose: () -> Unit) = ComposeUIViewController {
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
        App(
            deps = ready,
            appMode = com.beardytop.beatzaddik.domain.AppMode.Unified,
            onRequestClose = onClose,
        )
    }
}
