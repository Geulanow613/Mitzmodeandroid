package com.beardytop.beatzaddik.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.domain.ElectronicsRestPeriod
import com.beardytop.beatzaddik.domain.RestKind
import com.beardytop.beatzaddik.ui.components.AppText
import com.beardytop.beatzaddik.ui.components.HolyLightBackground
import com.beardytop.beatzaddik.ui.components.HalachicClickableText
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ShabbatRestScreen(period: ElectronicsRestPeriod) {
    Box(Modifier.fillMaxSize()) {
        HolyLightBackground(Modifier.fillMaxSize())
        Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.NightsStay,
            contentDescription = null,
            tint = TzaddikColors.GoldBright,
            modifier = Modifier.height(72.dp)
        )
        Spacer(Modifier.height(20.dp))
        AppText(
            period.title,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.GoldBright,
            textAlign = TextAlign.Center
        )
        period.hebrewDateLabel?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = TzaddikColors.ParchTop, style = MaterialTheme.typography.titleMedium)
        }
        period.locationLabel?.let {
            AppText(
                "Times for $it",
                color = TzaddikColors.ParchTop.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(TzaddikColors.ParchBase.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            HalachicClickableText(
                text = period.message,
                color = TzaddikColors.ParchTop,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(Modifier.height(20.dp))
        AppText(
            when (period.kind) {
                RestKind.SHABBAT ->
                    "This app is paused for Shabbat. Close it and enjoy a peaceful, screen-free day."
                RestKind.YOM_TOV ->
                    "This app is paused for Yom Tov. Close it and keep the day with joy and holiness."
            },
            color = TzaddikColors.GoldBorder,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center
        )
        period.endsAtEpochMillis?.let { ends ->
            Spacer(Modifier.height(16.dp))
            AppText(
                "You may open the app again after ${formatEndTime(ends)} (local time).",
                color = TzaddikColors.ParchTop.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(28.dp))
        Text(
            "Shabbat shalom · Gut Shabbos · שבת שלום",
            color = TzaddikColors.GoldBright.copy(alpha = 0.9f),
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
        }
    }
}

private fun formatEndTime(epochMillis: Long): String {
    val local = Instant.fromEpochMilliseconds(epochMillis)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val hour12 = ((local.hour + 11) % 12) + 1
    val amPm = if (local.hour < 12) "AM" else "PM"
    return "${local.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${local.dayOfMonth} at " +
        "$hour12:${local.minute.toString().padStart(2, '0')} $amPm"
}
