package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.domain.AppDisclaimer
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

@Composable
fun AboutContent(
    modifier: Modifier = Modifier,
    centered: Boolean = false,
    onWhatsAMitzvah: (() -> Unit)? = null,
) {
    val uriHandler = LocalUriHandler.current
    val align = if (centered) Alignment.CenterHorizontally else Alignment.Start
    val textAlign = if (centered) TextAlign.Center else TextAlign.Start

    HalachicTermsPage(key = "about") {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        if (centered) {
            GoldFlourishDivider()
            Spacer(Modifier.height(14.dp))
        }

        HalachicClickableText(
            text = AppDisclaimer.STARTUP_BODY,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.TextBrown,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        HalachicClickableText(
            text = AppDisclaimer.PRODUCER_INTRO,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.TextBrown,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(4.dp))
        AppText(
            AppDisclaimer.PRODUCER_NAME,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = TzaddikColors.GoldBorder,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        val linkText = buildAnnotatedString {
            pushStringAnnotation(tag = "URL", annotation = AppDisclaimer.WEBSITE_URL)
            withStyle(
                SpanStyle(
                    color = TzaddikColors.NavyMid,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(AppDisclaimer.WEBSITE_DISPLAY)
            }
            pop()
        }
        ClickableText(
            text = linkText,
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = textAlign),
            modifier = Modifier.fillMaxWidth(),
            onClick = { offset ->
                linkText.getStringAnnotations("URL", offset, offset).firstOrNull()?.let {
                    uriHandler.openUri(it.item)
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        AppText(
            AppDisclaimer.FEEDBACK_ABOUT_PROMPT,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.TextBrown,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        val emailLink = buildAnnotatedString {
            pushStringAnnotation(tag = "URL", annotation = AppDisclaimer.FEEDBACK_EMAIL_MAILTO)
            withStyle(
                SpanStyle(
                    color = TzaddikColors.NavyMid,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(AppDisclaimer.FEEDBACK_EMAIL)
            }
            pop()
        }
        ClickableText(
            text = emailLink,
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = textAlign),
            modifier = Modifier.fillMaxWidth(),
            onClick = { offset ->
                emailLink.getStringAnnotations("URL", offset, offset).firstOrNull()?.let {
                    uriHandler.openUri(it.item)
                }
            }
        )

        if (onWhatsAMitzvah != null) {
            Spacer(Modifier.height(20.dp))
            AppText(
                com.beardytop.beatzaddik.domain.MitzvahDefinitionText.ABOUT_LINK,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = TzaddikColors.NavyMid,
                textAlign = textAlign,
                // Glossary would steal the tap on "mitzvah" — this opens the definition dialog.
                enableTerms = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onWhatsAMitzvah),
            )
        }
    }
    }
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    ParchmentDialog(
        onDismiss = onDismiss,
        title = "About",
        confirmButton = {
            GoldButton(onClick = onDismiss, text = "Close")
        }
    ) {
        AboutContent(
            modifier = Modifier.padding(vertical = 4.dp),
            centered = true
        )
    }
}
