package com.beardytop.mitzmode.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beardytop.beatzaddik.App
import com.beardytop.beatzaddik.domain.AppMode
import com.beardytop.beatzaddik.domain.MitzModeFeatures
import com.beardytop.mitzmode.tzaddik.TzaddikBridge
import com.beardytop.mitzmode.tzaddik.TzaddikPermissionHost
import com.beardytop.mitzmode.ui.MitzModeApp as LegacyMitzModeApp

/**
 * Root UI for shipping Mitz Mode: checklist-first unified shell.
 * Set [MitzModeFeatures.legacyHomeEnabled] to true to restore the old home temporarily.
 * Center nav button uses shared `mitzvahbutton` drawable (same as iOS).
 */
@Composable
fun MitzModeRoot() {
    if (MitzModeFeatures.legacyHomeEnabled) {
        LegacyMitzModeApp()
        return
    }

    val activity = LocalContext.current as ComponentActivity
    val deps by TzaddikBridge.depsFlow.collectAsStateWithLifecycle()

    LaunchedEffect(activity) {
        TzaddikBridge.bindActivity(activity)
        TzaddikBridge.ensureDependencies(activity)
    }

    LaunchedEffect(deps) {
        if (deps != null) {
            TzaddikPermissionHost.requestFromActivity(activity)
        }
    }

    val ready = deps
    if (ready == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .navigationBarsPadding()
        ) {
            App(
                deps = ready,
                appMode = AppMode.Unified,
            )
        }
    }
}
