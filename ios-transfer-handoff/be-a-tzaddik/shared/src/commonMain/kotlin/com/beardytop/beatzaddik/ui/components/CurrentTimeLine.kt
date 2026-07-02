package com.beardytop.beatzaddik.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.beardytop.beatzaddik.domain.ZmanimFormatter
import com.beardytop.beatzaddik.ui.rememberLocalizedLocationLabel
import com.beardytop.beatzaddik.ui.translation.embedLtrForRtlMix
import com.beardytop.beatzaddik.ui.translation.LocalAppTranslation
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import kotlinx.coroutines.delay
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun CurrentTimeLine(
    timezoneId: String,
    locationLabel: String? = null,
    manualCityId: String? = null,
    modifier: Modifier = Modifier
) {
    var nowMillis by remember { mutableLongStateOf(kotlinx.datetime.Clock.System.now().toEpochMilliseconds()) }
    LaunchedEffect(Unit) {
        while (true) {
            nowMillis = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
            delay(1_000)
        }
    }

    val appTranslation = LocalAppTranslation.current
    val use24Hour = ZmanimFormatter.uses24HourClock(appTranslation.displayLanguageCode)
    val profileTime = remember(nowMillis, timezoneId, use24Hour) {
        val raw = formatClockTime(nowMillis, timezoneId, use24Hour)
        if (use24Hour) embedLtrForRtlMix(raw) else raw
    }

    AppText(
        text = profileTime,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = TzaddikColors.GoldBright,
        modifier = modifier,
        enableTerms = false
    )
    // Show location only — suppress timezone city if it matches the location label to avoid duplication
    val tzCity = shortTimezoneLabel(timezoneId)
    val resolvedLocation = rememberLocalizedLocationLabel(locationLabel, manualCityId)
    val locationText = resolvedLocation?.takeIf { it.isNotBlank() && !it.equals(tzCity, ignoreCase = true) }
        ?: tzCity.takeIf { it.isNotBlank() }
    if (!locationText.isNullOrBlank()) {
        val displayLocation = if (use24Hour) embedLtrForRtlMix(locationText) else locationText
        AppText(
            text = displayLocation,
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.ParchTop.copy(alpha = 0.55f),
            enableTerms = false
        )
    }
}

private fun shortTimezoneLabel(timezoneId: String): String =
    timezoneId.substringAfterLast('/').replace('_', ' ')

private fun formatClockTime(epochMillis: Long, timezoneId: String, use24Hour: Boolean): String =
    runCatching {
        val local = Instant.fromEpochMilliseconds(epochMillis)
            .toLocalDateTime(TimeZone.of(timezoneId))
        if (use24Hour) {
            val hour = local.hour.toString().padStart(2, '0')
            val minute = local.minute.toString().padStart(2, '0')
            val second = local.second.toString().padStart(2, '0')
            "$hour:$minute:$second"
        } else {
            val hour24 = local.hour
            val hour12 = when {
                hour24 == 0 -> 12
                hour24 > 12 -> hour24 - 12
                else -> hour24
            }
            val amPm = if (hour24 < 12) "AM" else "PM"
            val minute = local.minute.toString().padStart(2, '0')
            val second = local.second.toString().padStart(2, '0')
            "$hour12:$minute:$second $amPm"
        }
    }.getOrElse { "—" }
