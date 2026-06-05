package com.beardytop.beatzaddik

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beardytop.beatzaddik.ui.BeATzaddikApp
import com.beardytop.beatzaddik.viewmodel.AppViewModel

@Composable
fun App(
    deps: AppDependencies,
    embeddedMode: Boolean = false,
    onRequestClose: () -> Unit = {},
    returnToMainIcon: (@Composable () -> Unit)? = null
) {
    val viewModel: AppViewModel = viewModel { AppViewModel(deps) }
    BeATzaddikApp(
        viewModel = viewModel,
        embeddedMode = embeddedMode,
        onRequestClose = onRequestClose,
        returnToMainIcon = returnToMainIcon
    )
}
