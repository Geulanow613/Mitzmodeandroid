package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    centered: Boolean = false
) {
    val uriHandler = LocalUriHandler.current
    val align = if (centered) Alignment.CenterHorizontally else Alignment.Start
    val textAlign = if (centered) TextAlign.Center else TextAlign.Start

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = align
    ) {
        if (centered) {
            GoldFlourishDivider()
            Spacer(Modifier.height(14.dp))
        }

        Text(
            AppDisclaimer.STARTUP_BODY,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.TextBrown,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Text(
            AppDisclaimer.PRODUCER_INTRO,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.TextBrown,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(4.dp))
        Text(
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
