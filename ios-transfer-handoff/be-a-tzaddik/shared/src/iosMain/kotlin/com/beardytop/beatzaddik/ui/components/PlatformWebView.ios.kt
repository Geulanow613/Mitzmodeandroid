package com.beardytop.beatzaddik.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView

@Composable
actual fun PlatformWebView(
    url: String,
    modifier: Modifier,
) {
    val request = remember(url) {
        val nsUrl = NSURL.URLWithString(url) ?: return@remember null
        NSURLRequest.requestWithURL(nsUrl)
    }
    UIKitView(
        modifier = modifier,
        factory = {
            WKWebView().apply {
                request?.let { loadRequest(it) }
            }
        },
        update = { webView ->
            val current = webView.URL?.absoluteString
            if (request != null && current != url) {
                webView.loadRequest(request)
            }
        },
    )
}
