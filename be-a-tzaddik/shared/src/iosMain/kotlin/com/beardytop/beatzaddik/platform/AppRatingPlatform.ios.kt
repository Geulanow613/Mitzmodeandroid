@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.domain.AppRatingPromptPolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.Foundation.NSDate
import platform.Foundation.NSISO8601DateFormatter
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.Foundation.NSURLSession
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.dataTaskWithRequest
import platform.Foundation.setHTTPBody
import platform.Foundation.setHTTPMethod
import platform.Foundation.setValue
import platform.UIKit.UIApplication
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.resume

actual fun appRatingPlatformLabel(): String = "ios"

actual fun appStoreWriteReviewUrl(): String = AppRatingPromptPolicy.APP_STORE_URL

actual fun openAppStoreListing() {
    // Prefer HTTPS App Store page the user verified; then itms-apps for device Store app.
    val candidates = listOf(
        AppRatingPromptPolicy.APP_STORE_URL,
        "https://apps.apple.com/app/id6740733628",
        "itms-apps://apps.apple.com/app/id6740733628",
    )
    dispatch_async(dispatch_get_main_queue()) {
        val app = UIApplication.sharedApplication
        for (candidate in candidates) {
            val url = NSURL.URLWithString(candidate) ?: continue
            // Do not gate on canOpenURL — that requires LSApplicationQueriesSchemes and
            // Compose UriHandler was failing with "custom URL schemes" / Safari invalid errors.
            app.openURL(url, options = emptyMap<Any?, Any>(), completionHandler = null)
            return@dispatch_async
        }
    }
}

actual suspend fun submitAppRatingFeedback(rating: Int, message: String): Boolean =
    withContext(Dispatchers.IO) {
        runCatching { postFeedback(rating, message) }.getOrDefault(false)
    }

private suspend fun postFeedback(rating: Int, message: String): Boolean {
    val url = NSURL.URLWithString(AppRatingPromptPolicy.FORMSPREE_ENDPOINT) ?: return false
    val timestamp = NSISO8601DateFormatter().stringFromDate(NSDate())
    val payload = mapOf<Any?, Any?>(
        "name" to "App rating feedback",
        "rating" to rating.toString(),
        "message" to message.ifBlank { "(no message)" },
        "platform" to "ios",
        "timestamp" to timestamp,
    )
    val jsonData = NSJSONSerialization.dataWithJSONObject(payload, options = 0u, error = null)
        ?: return false
    val request = NSMutableURLRequest.requestWithURL(url).apply {
        setHTTPMethod("POST")
        setTimeoutInterval(12.0)
        setValue("application/json", forHTTPHeaderField = "Content-Type")
        setValue("application/json", forHTTPHeaderField = "Accept")
        setValue("MitzMode-iOS-App", forHTTPHeaderField = "User-Agent")
        setHTTPBody(jsonData)
    }
    return suspendCancellableCoroutine { cont ->
        val task = NSURLSession.sharedSession.dataTaskWithRequest(request) { _, response, error ->
            if (!cont.isActive) return@dataTaskWithRequest
            if (error != null) {
                cont.resume(false)
                return@dataTaskWithRequest
            }
            val code = (response as? NSHTTPURLResponse)?.statusCode?.toInt() ?: 0
            cont.resume(code in 200..299)
        }
        cont.invokeOnCancellation { task.cancel() }
        task.resume()
    }
}
