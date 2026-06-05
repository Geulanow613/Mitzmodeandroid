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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.domain.ManualCities
import com.beardytop.beatzaddik.domain.ManualCity
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

/** Display label used in city lists (includes country when helpful for sorting/search). */
fun cityDisplayLabel(city: ManualCity): String {
    val label = city.label
    val suffix = label.substringAfterLast(", ", missingDelimiterValue = "").trim()
    val country = when {
        suffix.length == 2 && suffix.all { it.isUpperCase() } -> {
            if (suffix in setOf("ON", "QC", "BC", "AB", "MB", "SK", "NS", "NB", "NL", "PE")) "Canada"
            else "USA"
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

/** True when [profileCityId] refers to [city], including legacy short ids (e.g. nyc → new_york). */
fun profileMatchesManualCity(profileCityId: String?, city: ManualCity): Boolean {
    if (profileCityId == null) return false
    if (profileCityId == city.id) return true
    return ManualCities.byId(profileCityId)?.id == city.id
}

/** All manual cities matching [query], sorted A→Z by [cityDisplayLabel]. */
fun filterManualCities(query: String): List<ManualCity> {
    val q = query.trim().lowercase()
    if (q.isBlank()) {
        return ManualCities.all.sortedBy { cityDisplayLabel(it) }
    }
    return ManualCities.all.filter { city ->
        cityDisplayLabel(city).lowercase().contains(q) ||
            city.label.lowercase().contains(q) ||
            city.id.lowercase().contains(q)
    }.sortedBy { cityDisplayLabel(it) }
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
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) TzaddikColors.NavyDeep else TzaddikColors.TextBrown,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f),
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
