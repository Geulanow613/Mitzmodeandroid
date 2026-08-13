package com.beardytop.beatzaddik.platform

/** Platform label included in Formspree submissions ("ios" / "android"). */
expect fun appRatingPlatformLabel(): String

/** Direct listing URL for this platform. */
expect fun appStoreWriteReviewUrl(): String

/**
 * Open the App Store / Play Store listing using the platform browser/store handler.
 * Prefer this over Compose [androidx.compose.ui.platform.UriHandler] on iOS (canOpenURL / schemes).
 */
expect fun openAppStoreListing()

/** POST soft-feedback to Formspree. Returns true on HTTP 2xx. */
expect suspend fun submitAppRatingFeedback(
    rating: Int,
    message: String,
): Boolean
