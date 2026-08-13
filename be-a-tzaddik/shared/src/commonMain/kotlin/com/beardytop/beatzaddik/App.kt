package com.beardytop.beatzaddik

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beardytop.beatzaddik.domain.AppMode
import com.beardytop.beatzaddik.ui.BeATzaddikApp
import com.beardytop.beatzaddik.viewmodel.AppViewModel

@Composable
fun App(
    deps: AppDependencies,
    appMode: AppMode = AppMode.Standalone,
    /** @deprecated Use [appMode] = [AppMode.Embedded] */
    embeddedMode: Boolean = false,
    onRequestClose: () -> Unit = {},
    returnToMainIcon: (@Composable () -> Unit)? = null,
    mitzvotCount: Int = 0,
    onChecklistItemChecked: ((itemId: String, title: String, dayKey: String) -> Unit)? = null,
) {
    val resolvedMode = when {
        embeddedMode && appMode == AppMode.Standalone -> AppMode.Embedded
        else -> appMode
    }
    val viewModel: AppViewModel = viewModel { AppViewModel(deps) }
    BeATzaddikApp(
        viewModel = viewModel,
        deps = deps,
        appMode = resolvedMode,
        onRequestClose = onRequestClose,
        returnToMainIcon = returnToMainIcon,
        externalMitzvotCount = mitzvotCount,
        externalChecklistChecked = onChecklistItemChecked,
    )
}
