package com.beardytop.beatzaddik.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.domain.ElectronicsRestPeriod
import com.beardytop.beatzaddik.domain.RestKind
import com.beardytop.beatzaddik.domain.RestPhase
import com.beardytop.beatzaddik.domain.ZmanimFormatter
import com.beardytop.beatzaddik.ui.components.AppText
import com.beardytop.beatzaddik.ui.components.HolyLightBackground
import com.beardytop.beatzaddik.ui.components.HalachicClickableText
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedTemplate

@Composable
fun ShabbatRestScreen(
    period: ElectronicsRestPeriod,
    timezoneId: String,
    onOpenSettings: (() -> Unit)? = null,
) {
    val resolvedTitle = rememberAppTranslatedTemplate(period.title, period.args)
    val resolvedMessage = rememberAppTranslatedTemplate(period.message, period.args)
    Box(Modifier.fillMaxSize()) {
        HolyLightBackground(Modifier.fillMaxSize())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                Icons.Outlined.NightsStay,
                contentDescription = null,
                tint = TzaddikColors.GoldBright,
                modifier = Modifier.height(72.dp),
            )
            Spacer(Modifier.height(20.dp))
            AppText(
                resolvedTitle,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.GoldBright,
                textAlign = TextAlign.Center,
            )
            period.hebrewDateLabel?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = TzaddikColors.ParchTop, style = MaterialTheme.typography.titleMedium)
            }
            period.locationLabel?.let {
                val resolvedTimesFor = rememberAppTranslatedTemplate("Times for {place}", mapOf("place" to it))
                AppText(
                    resolvedTimesFor,
                    enableTerms = false,
                    color = TzaddikColors.ParchTop.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(24.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(TzaddikColors.ParchBase.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                    .padding(20.dp),
            ) {
                HalachicClickableText(
                    text = resolvedMessage,
                    color = TzaddikColors.ParchTop,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Spacer(Modifier.height(20.dp))
            when (period.phase) {
                RestPhase.APPROACHING -> {
                    period.startsAtEpochMillis?.let { starts ->
                        val resolvedPauseNotice = rememberAppTranslatedTemplate(
                            "The app will pause automatically at {time}.",
                            mapOf("time" to formatRestTime(starts, timezoneId)),
                        )
                        AppText(
                            resolvedPauseNotice,
                            enableTerms = false,
                            color = TzaddikColors.GoldBorder,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                RestPhase.ACTIVE -> {
                    AppText(
                        when (period.kind) {
                            RestKind.SHABBAT ->
                                "This app is paused for Shabbat. Close it and enjoy a peaceful, screen-free day."
                            RestKind.YOM_TOV ->
                                "This app is paused for Yom Tov. Close it and keep the day with joy and holiness."
                        },
                        color = TzaddikColors.GoldBorder,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        textAlign = TextAlign.Center,
                    )
                    period.endsAtEpochMillis?.let { ends ->
                        Spacer(Modifier.height(16.dp))
                        val endLabel = formatRestTime(ends, timezoneId)
                        val tzeitNote = when (period.kind) {
                            RestKind.SHABBAT -> "after tzeit (nightfall)"
                            RestKind.YOM_TOV -> "after Yom Tov ends at tzeit"
                        }
                        val resolvedReopenNotice = rememberAppTranslatedTemplate(
                            "You may open the app again {tzeitNote} — {endLabel}.",
                            mapOf("tzeitNote" to tzeitNote, "endLabel" to endLabel),
                        )
                        AppText(
                            resolvedReopenNotice,
                            enableTerms = false,
                            color = TzaddikColors.ParchTop.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                        )
                    }
                    if (period.kind == RestKind.SHABBAT) {
                        Spacer(Modifier.height(28.dp))
                        Text(
                            "Shabbat shalom · Gut Shabbos · שבת שלום",
                            color = TzaddikColors.GoldBright.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            if (onOpenSettings != null) {
                Spacer(Modifier.height(32.dp))
                AppText(
                    "I live somewhere different",
                    enableTerms = false,
                    color = TzaddikColors.ParchTop.copy(alpha = 0.55f),
                    style = MaterialTheme.typography.labelSmall.copy(textDecoration = TextDecoration.Underline),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable(onClick = onOpenSettings)
                        .padding(8.dp),
                )
            }
        }
    }
}

private fun formatRestTime(epochMillis: Long, timezoneId: String): String =
    ZmanimFormatter.formatMonthDayTime(epochMillis, timezoneId) ?: "when the holy day ends"
