package com.beardytop.beatzaddik.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Platform WebView host for http(s) pages inside the app. */
@Composable
expect fun PlatformWebView(
    url: String,
    modifier: Modifier = Modifier,
)
