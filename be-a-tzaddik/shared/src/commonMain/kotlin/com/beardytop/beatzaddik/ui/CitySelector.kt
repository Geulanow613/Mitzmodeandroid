package com.beardytop.beatzaddik.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.beardytop.beatzaddik.ui.components.AppText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.data.CityGeographyCatalog
import com.beardytop.beatzaddik.domain.ManualCities
import com.beardytop.beatzaddik.domain.ManualCity
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.ui.translation.BundledTranslationLanguages
import com.beardytop.beatzaddik.ui.translation.LocalAppTranslation

private val canadaProvinces = setOf("ON", "QC", "BC", "AB", "MB", "SK", "NS", "NB", "NL", "PE")

/** English display label (includes country when helpful for sorting/search). */
fun cityDisplayLabelEnglish(city: ManualCity): String {
    val label = city.label
    val suffix = label.substringAfterLast(", ", missingDelimiterValue = "").trim()
    val country = when {
        suffix.length == 2 && suffix.all { it.isUpperCase() } -> {
            if (suffix in canadaProvinces) "Canada" else "USA"
        }
        suffix.isNotBlank() -> suffix
        city.timezoneId == "Asia/Jerusalem" -> "Israel"
        city.timezoneId == "Asia/Singapore" -> "Singapore"
        city.timezoneId == "Asia/Hong_Kong" -> "Hong Kong"
        city.timezoneId.startsWith("Australia/") -> "Australia"
        city.timezoneId == "Pacific/Auckland" -> "New Zealand"
        else -> "Unknown"
    }
    return if (label.endsWith(", $country")) label else "$label, $country"
}

/** Localized display label for city lists; falls back to English when no bundle exists. */
fun cityDisplayLabel(city: ManualCity, languageCode: String): String {
    if (languageCode == "en" || !BundledTranslationLanguages.isBundled(languageCode)) {
        return cityDisplayLabelEnglish(city)
    }
    return CityGeographyCatalog.label(city.id, languageCode) ?: cityDisplayLabelEnglish(city)
}

/** Localized city name for compact UI (clock line, GPS pill) — omits country/region suffix. */
private fun shortCityDisplayLabel(city: ManualCity, languageCode: String): String =
    cityDisplayLabel(city, languageCode).substringBefore(',').trim()
        .ifBlank { city.label.substringBefore(',').trim() }

/**
 * Resolves the location line under the clock — manual-city labels use [cityDisplayLabel];
 * GPS geocoder strings are matched via [CityGeographyCatalog] aliases when possible.
 */
fun localizedLocationLabel(
    locationLabel: String?,
    manualCityId: String?,
    languageCode: String,
): String? {
    if (locationLabel.isNullOrBlank()) return locationLabel
    if (languageCode == "en" || !BundledTranslationLanguages.isBundled(languageCode)) {
        return locationLabel
    }
    manualCityId?.let { id ->
        ManualCities.byId(id)?.let { return shortCityDisplayLabel(it, languageCode) }
    }
    val cityPrefix = locationLabel.substringBefore(',').trim()
    CityGeographyCatalog.cityIdForLocationLabel(locationLabel)?.let { cityId ->
        ManualCities.byId(cityId)?.let { return shortCityDisplayLabel(it, languageCode) }
    }
    ManualCities.all.firstOrNull { city ->
        city.label.equals(cityPrefix, ignoreCase = true) ||
            cityDisplayLabelEnglish(city).substringBefore(',').trim()
                .equals(cityPrefix, ignoreCase = true)
    }?.let { return shortCityDisplayLabel(it, languageCode) }
    val bundledFull = com.beardytop.beatzaddik.ui.translation.resolveBundledTranslationSync(
        locationLabel,
        languageCode,
    )
    if (bundledFull != locationLabel) return bundledFull
    if (cityPrefix.isNotBlank() && cityPrefix != locationLabel) {
        val bundledPrefix = com.beardytop.beatzaddik.ui.translation.resolveBundledTranslationSync(
            cityPrefix,
            languageCode,
        )
        if (bundledPrefix != cityPrefix) return bundledPrefix
    }
    return locationLabel
}

@Composable
fun rememberLocalizedLocationLabel(locationLabel: String?, manualCityId: String?): String? {
    val languageCode = LocalAppTranslation.current.displayLanguageCode
    return remember(locationLabel, manualCityId, languageCode) {
        localizedLocationLabel(locationLabel, manualCityId, languageCode)
    }
}

@Composable
fun rememberCityDisplayLabel(city: ManualCity): String {
    val languageCode = LocalAppTranslation.current.displayLanguageCode
    return remember(city.id, languageCode) { cityDisplayLabel(city, languageCode) }
}

/** True when [profileCityId] refers to [city], including legacy short ids (e.g. nyc → new_york). */
fun profileMatchesManualCity(profileCityId: String?, city: ManualCity): Boolean {
    if (profileCityId == null) return false
    if (profileCityId == city.id) return true
    return ManualCities.byId(profileCityId)?.id == city.id
}

/** All manual cities matching [query], sorted A→Z by localized display label. */
fun filterManualCities(query: String, languageCode: String): List<ManualCity> {
    val q = query.trim().lowercase()
    if (q.isBlank()) {
        return ManualCities.all.sortedBy { cityDisplayLabel(it, languageCode) }
    }
    return ManualCities.all.filter { city ->
        cityDisplayLabel(city, languageCode).lowercase().contains(q) ||
            cityDisplayLabelEnglish(city).lowercase().contains(q) ||
            city.label.lowercase().contains(q) ||
            city.id.lowercase().contains(q) ||
            CityGeographyCatalog.searchAliases(city.id).any { it.lowercase().contains(q) }
    }.sortedBy { cityDisplayLabel(it, languageCode) }
}

/** Single-select city row with clear selected state (checkmark + highlight). */
@Composable
fun ManualCityListRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(10.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                if (selected) TzaddikColors.GoldBright.copy(alpha = 0.32f)
                else TzaddikColors.ParchMid.copy(alpha = 0.35f)
            )
            .then(
                if (selected) {
                    Modifier.border(1.5.dp, TzaddikColors.GoldBorder, shape)
                } else {
                    Modifier.border(1.dp, TzaddikColors.GoldBorder.copy(alpha = 0.15f), shape)
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppText(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = if (selected) TzaddikColors.NavyDeep else TzaddikColors.TextBrown,
            modifier = Modifier.weight(1f),
            enableTerms = false
        )
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = TzaddikColors.NavyDeep,
            )
        }
    }
}
