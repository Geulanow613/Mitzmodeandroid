package com.beardytop.beatzaddik

import androidx.compose.ui.window.ComposeUIViewController
import com.beardytop.beatzaddik.platform.PlatformLocationService
import kotlinx.coroutines.runBlocking

fun MainViewController() = ComposeUIViewController {
    val deps = runBlocking {
        AppDependencies.create(platformContext = null, locationService = PlatformLocationService())
    }
    App(deps)
}

/** Embedded Daily Mitzvot Checklist inside Mitz Mode iOS (same as Android). */
fun EmbeddedChecklistViewController(onClose: () -> Unit) = ComposeUIViewController {
    val deps = runBlocking {
        AppDependencies.create(platformContext = null, locationService = PlatformLocationService())
    }
    App(
        deps = deps,
        embeddedMode = true,
        onRequestClose = onClose
    )
}
