package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.domain.AppRatingPromptPolicy
import com.beardytop.beatzaddik.platform.openAppStoreListing
import com.beardytop.beatzaddik.platform.submitAppRatingFeedback
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import kotlinx.coroutines.launch

private enum class RatingStep {
    Rate,
    AskReview,
    CollectFeedback,
    FeedbackSent,
}

/**
 * Soft in-app rating: 4–5 opens the store write-review page; lower opens Formspree feedback.
 */
@Composable
fun AppRatingPromptDialog(
    onDismissForLater: () -> Unit,
    onCompleted: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var step by remember { mutableStateOf(RatingStep.Rate) }
    var stars by remember { mutableIntStateOf(0) }
    var feedback by remember { mutableStateOf("") }
    var submitting by remember { mutableStateOf(false) }
    var submitFailed by remember { mutableStateOf(false) }

    ParchmentDialog(
        onDismiss = when (step) {
            RatingStep.FeedbackSent -> onCompleted
            else -> onDismissForLater
        },
        title = when (step) {
            RatingStep.Rate -> "Enjoying Mitz Mode?"
            RatingStep.AskReview -> "Thank you!"
            RatingStep.CollectFeedback -> "We're sorry to hear that"
            RatingStep.FeedbackSent -> "Thanks for the feedback"
        },
        showCloseIcon = false,
        centerButtons = true,
        dismissButton = when (step) {
            RatingStep.Rate, RatingStep.AskReview -> {
                {
                    ParchmentTextButton(
                        onClick = onDismissForLater,
                        text = "Not now",
                    )
                }
            }
            RatingStep.CollectFeedback -> {
                {
                    if (!submitting) {
                        ParchmentTextButton(
                            onClick = onDismissForLater,
                            text = "Cancel",
                        )
                    }
                }
            }
            RatingStep.FeedbackSent -> null
        },
        confirmButton = when (step) {
            RatingStep.AskReview -> {
                {
                    GoldButton(
                        onClick = {
                            openAppStoreListing()
                            onCompleted()
                        },
                        text = "Leave a review",
                    )
                }
            }
            RatingStep.CollectFeedback -> {
                {
                    GoldButton(
                        onClick = {
                            if (submitting) return@GoldButton
                            submitting = true
                            submitFailed = false
                            scope.launch {
                                val ok = submitAppRatingFeedback(stars, feedback.trim())
                                submitting = false
                                if (ok) {
                                    step = RatingStep.FeedbackSent
                                } else {
                                    submitFailed = true
                                }
                            }
                        },
                        text = if (submitting) "Sending…" else "Send feedback",
                    )
                }
            }
            RatingStep.FeedbackSent -> {
                {
                    GoldButton(onClick = onCompleted, text = "Done")
                }
            }
            RatingStep.Rate -> null
        },
    ) {
        when (step) {
            RatingStep.Rate -> {
                AppText(
                    "How would you rate this app?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TzaddikColors.TextBrown,
                    textAlign = TextAlign.Center,
                    enableTerms = false,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    for (i in 1..5) {
                        val selected = i <= stars
                        Icon(
                            imageVector = if (selected) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = "$i stars",
                            tint = if (selected) TzaddikColors.GoldBright else TzaddikColors.NavyMid.copy(alpha = 0.45f),
                            modifier = Modifier
                                .size(44.dp)
                                .padding(2.dp)
                                .clickable {
                                    stars = i
                                    step = if (i >= AppRatingPromptPolicy.POSITIVE_STAR_THRESHOLD) {
                                        RatingStep.AskReview
                                    } else {
                                        RatingStep.CollectFeedback
                                    }
                                },
                        )
                    }
                }
            }
            RatingStep.AskReview -> {
                AppText(
                    "We're glad you're enjoying Mitz Mode. A short review on the store helps others find the app.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TzaddikColors.TextBrown,
                    textAlign = TextAlign.Center,
                    enableTerms = false,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            RatingStep.CollectFeedback -> {
                AppText(
                    "Tell us what we can improve — your note goes straight to the developer.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TzaddikColors.TextBrown,
                    enableTerms = false,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = feedback,
                    onValueChange = { feedback = it.take(2000) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !submitting,
                    minLines = 4,
                    placeholder = {
                        AppText(
                            "What felt off or confusing?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TzaddikColors.TextMuted,
                            enableTerms = false,
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TzaddikColors.GoldBorder,
                        unfocusedBorderColor = TzaddikColors.NavyMid.copy(alpha = 0.35f),
                        focusedTextColor = TzaddikColors.NavyDeep,
                        unfocusedTextColor = TzaddikColors.NavyDeep,
                        cursorColor = TzaddikColors.NavyMid,
                    ),
                )
                if (submitFailed) {
                    Spacer(Modifier.height(8.dp))
                    AppText(
                        "Couldn't send right now. Check your connection and try again.",
                        style = MaterialTheme.typography.labelMedium,
                        color = TzaddikColors.TextBrown,
                        enableTerms = false,
                    )
                }
                if (submitting) {
                    Spacer(Modifier.height(12.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterHorizontally),
                        color = TzaddikColors.GoldBright,
                        strokeWidth = 3.dp,
                    )
                }
            }
            RatingStep.FeedbackSent -> {
                AppText(
                    "Got it — thank you for helping us make Mitz Mode better.",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = TzaddikColors.NavyDeep,
                    textAlign = TextAlign.Center,
                    enableTerms = false,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
