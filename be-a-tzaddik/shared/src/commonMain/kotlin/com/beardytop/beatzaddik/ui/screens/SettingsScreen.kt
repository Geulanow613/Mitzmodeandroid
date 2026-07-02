package com.beardytop.beatzaddik.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import com.beardytop.beatzaddik.ui.components.AppText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.domain.Gender
import com.beardytop.beatzaddik.domain.IsraelDetectionSource
import com.beardytop.beatzaddik.domain.ManualCities
import com.beardytop.beatzaddik.ui.ManualCityListRow
import com.beardytop.beatzaddik.ui.cityDisplayLabel
import com.beardytop.beatzaddik.ui.filterManualCities
import com.beardytop.beatzaddik.ui.localizedLocationLabel
import com.beardytop.beatzaddik.ui.profileMatchesManualCity
import com.beardytop.beatzaddik.ui.rememberCityDisplayLabel
import com.beardytop.beatzaddik.domain.NusachSelection
import com.beardytop.beatzaddik.domain.displayLabel
import com.beardytop.beatzaddik.domain.KashrutWaitTimes
import com.beardytop.beatzaddik.ui.components.GoldButton
import com.beardytop.beatzaddik.ui.components.GoldFlourishDivider
import com.beardytop.beatzaddik.ui.components.ParchmentContentCard
import com.beardytop.beatzaddik.ui.components.ParchmentDialog
import com.beardytop.beatzaddik.ui.components.ParchmentTextButton
import com.beardytop.beatzaddik.ui.components.TextScaleControl
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.domain.AppDisclaimer
import com.beardytop.beatzaddik.ui.translation.LocalAppTranslation
import com.beardytop.beatzaddik.ui.translation.LocalLanguageSelector
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedTemplate
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedText
import com.beardytop.beatzaddik.viewmodel.AppViewModel

@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    scrollToKashrut: Boolean = false,
    onScrollTargetConsumed: () -> Unit = {}
) {
    val profile by viewModel.profile.collectAsState()
    val locMsg by viewModel.locationMessage.collectAsState()
    val scrollState = rememberScrollState()
    var kashrutScrollY by remember { mutableIntStateOf(0) }
    var showCityPicker by remember { mutableStateOf(false) }
    var cityQuery by remember { mutableStateOf("") }
    val languageCode = LocalAppTranslation.current.displayLanguageCode

    LaunchedEffect(scrollToKashrut) {
        if (scrollToKashrut) {
            delay(80)
            scrollState.animateScrollTo(kashrutScrollY.coerceAtLeast(0))
            onScrollTargetConsumed()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Screen title
        AppText(
            "Settings",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.GoldBright,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )

        LanguageCard()

        // --- Profile card ---
        SettingsCard(title = "Your Profile") {
            SettingLabel("Gender", "Determines which mitzvot appear in your daily checklist")
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SettingsChip(
                        label = Gender.MALE.displayLabel(),
                        selected = profile.gender == Gender.MALE,
                        onClick = { viewModel.updateProfile(gender = Gender.MALE) },
                        modifier = Modifier.weight(1f)
                    )
                    SettingsChip(
                        label = Gender.FEMALE.displayLabel(),
                        selected = profile.gender == Gender.FEMALE,
                        onClick = { viewModel.updateProfile(gender = Gender.FEMALE) },
                        modifier = Modifier.weight(1f)
                    )
                }
                SettingsChip(
                    label = Gender.PREFER_NOT_TO_SAY.displayLabel(),
                    selected = profile.gender == Gender.PREFER_NOT_TO_SAY,
                    onClick = { viewModel.updateProfile(gender = Gender.PREFER_NOT_TO_SAY) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(12.dp))
            SettingsDivider()
            Spacer(Modifier.height(10.dp))
            ProfileToggle(
                label = "Married",
                checked = profile.married,
                onChange = { viewModel.updateProfile(married = it) }
            )
            Spacer(Modifier.height(4.dp))
            ProfileToggle(
                label = "Have children",
                checked = profile.hasChildren,
                onChange = { viewModel.updateProfile(hasChildren = it) }
            )
        }

        // --- Prayer tradition card ---
        SettingsCard(title = "Prayer Tradition (Nusach)") {
            AppText(
                "Your nusach determines which prayer customs appear in your checklist.",
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.TextMuted
            )
            Spacer(Modifier.height(10.dp))
            NusachSelection.selectable.forEach { n ->
                SettingsChip(
                    label = n.displayLabel(),
                    selected = profile.nusachSelection == n,
                    onClick = { viewModel.saveProfile(profile.copy(nusachSelection = n)) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                )
            }
            val effectiveName = profile.effectiveNusach().displayLabel()
            Spacer(Modifier.height(6.dp))
            CurrentlyUsingLine(nusachLabel = effectiveName)
        }

        // --- Location card ---
        SettingsCard(title = "Location & Prayer Times") {
            AppText(
                "Prayer times (zmanim) — Shema deadline, Mincha window, candle lighting — are calculated from your location. Your location is stored on this device only.",
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.TextMuted
            )
            Spacer(Modifier.height(10.dp))
            val selectedCity = profile.manualCityId?.let { ManualCities.byId(it) }
            LocationPill(
                template = when {
                    profile.useGps && profile.locationLabel != null -> "Current location: {place} (GPS)"
                    profile.useGps -> null
                    selectedCity != null -> "Current location: {place} (city)"
                    profile.locationLabel != null -> "Current location: {place}"
                    else -> null
                },
                templateArgs = when {
                    profile.useGps && profile.locationLabel != null ->
                        mapOf(
                            "place" to (
                                localizedLocationLabel(
                                    profile.locationLabel,
                                    profile.manualCityId,
                                    languageCode,
                                ) ?: profile.locationLabel!!
                            ),
                        )
                    selectedCity != null ->
                        mapOf("place" to cityDisplayLabel(selectedCity, languageCode))
                    profile.locationLabel != null ->
                        mapOf(
                            "place" to (
                                localizedLocationLabel(
                                    profile.locationLabel,
                                    profile.manualCityId,
                                    languageCode,
                                ) ?: profile.locationLabel!!
                            ),
                        )
                    else -> emptyMap()
                },
                fallbackText = when {
                    profile.useGps -> "Current location: waiting for GPS…"
                    else -> "No location set — turn on GPS or choose a city below"
                },
            )
            locMsg?.let {
                Spacer(Modifier.height(4.dp))
                AppText(it, style = MaterialTheme.typography.bodySmall, color = TzaddikColors.TextMuted)
            }
            Spacer(Modifier.height(10.dp))
            ProfileToggle(
                label = "Use GPS for location",
                description = "Updates prayer times from your device location. Turn off to pick a city below.",
                checked = profile.useGps,
                onChange = { enabled -> viewModel.setGpsForZmanim(enabled) { } }
            )
            Spacer(Modifier.height(10.dp))
            AppText(
                "Or select your city:",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = TzaddikColors.NavyDeep
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = cityQuery,
                onValueChange = { cityQuery = it },
                modifier = Modifier.fillMaxWidth(),
                label = { AppText("Search city") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            val inlineCities = remember(cityQuery, languageCode) { filterManualCities(cityQuery, languageCode) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                inlineCities.forEach { city ->
                    val selected = profileMatchesManualCity(profile.manualCityId, city)
                    ManualCityListRow(
                        label = rememberCityDisplayLabel(city),
                        selected = selected,
                        onClick = {
                            viewModel.setManualCity(city.id)
                            cityQuery = ""
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (inlineCities.isEmpty()) {
                    AppText(
                        "No cities found. Try another spelling.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TzaddikColors.TextMuted
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            GoldButton(
                onClick = { showCityPicker = true },
                text = "More cities (dropdown)",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            SettingsDivider()
            Spacer(Modifier.height(10.dp))

            // "I live in Israel" — shown as a manual fallback when location can't auto-detect it.
            // If GPS or city already resolves to Israel, we show an informational note instead.
            val israelSource = profile.isInIsraelSource
            val israelAutoDetected = israelSource != IsraelDetectionSource.MANUAL_FLAG && profile.isInIsrael
            if (israelAutoDetected) {
                AppText(
                    if (israelSource == IsraelDetectionSource.GPS) {
                        "🇮🇱 Israel customs active — detected from your GPS location (1-day Yom Tov, Israel parsha cycle)."
                    } else {
                        "🇮🇱 Israel customs active — detected from your city selection (1-day Yom Tov, Israel parsha cycle)."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = TzaddikColors.NavyMid,
                    enableTerms = false,
                )
            } else {
                ProfileToggle(
                    label = "I live in Israel",
                    description = "Uses 1-day Yom Tov customs and the Israel parsha cycle. Select a city above or enable GPS for automatic detection.",
                    checked = profile.liveInIsrael,
                    onChange = { viewModel.setLiveInIsrael(it) }
                )
            }
        }

        if (showCityPicker) {
            val filteredCities = remember(cityQuery, languageCode) { filterManualCities(cityQuery, languageCode) }
            ParchmentDialog(
                onDismiss = { showCityPicker = false },
                title = "Choose city for zmanim",
                dismissButton = {
                    ParchmentTextButton(
                        onClick = { showCityPicker = false },
                        text = "Close"
                    )
                }
            ) {
                OutlinedTextField(
                    value = cityQuery,
                    onValueChange = { cityQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { AppText("Search city") },
                    singleLine = true
                )
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    filteredCities.forEach { city ->
                        ManualCityListRow(
                            label = rememberCityDisplayLabel(city),
                            selected = profileMatchesManualCity(profile.manualCityId, city),
                            onClick = {
                                viewModel.setManualCity(city.id)
                                showCityPicker = false
                                cityQuery = ""
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (filteredCities.isEmpty()) {
                        AppText(
                            "No cities found. Try another spelling.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TzaddikColors.TextMuted
                        )
                    }
                }
            }
        }

        // --- Kashrut card ---
        SettingsCard(
            title = "Meat / Dairy Wait Times",
            modifier = Modifier.onGloballyPositioned { coords ->
                kashrutScrollY = coords.positionInParent().y.toInt()
            }
        ) {
            AppText(
                "After eating meat, you wait before eating dairy, and vice versa. The wait time is a matter of your family tradition — ask your rabbi if unsure.",
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.TextMuted
            )
            Spacer(Modifier.height(14.dp))
            WaitTimeSlider(
                label = "After meat → before dairy",
                value = profile.meatToDairyHours(),
                onValueChange = {
                    viewModel.setKashrutHours(meatToDairyHours = it, dairyToMeatMinutes = null)
                }
            )
            Spacer(Modifier.height(10.dp))
            DairyToMeatWaitSlider(
                label = "After dairy → before meat",
                valueMinutes = profile.dairyToMeatWaitMinutes(),
                onValueChange = {
                    viewModel.setKashrutHours(meatToDairyHours = null, dairyToMeatMinutes = it)
                }
            )
            Spacer(Modifier.height(6.dp))
            AppText(
                "Default wait times are set automatically based on your nusach if not customized here.",
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.TextMuted
            )
        }

        // --- Text size card ---
        SettingsCard(title = "Text Size") {
            AppText(
                "Adjust to your preferred reading size.",
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.TextMuted
            )
            Spacer(Modifier.height(10.dp))
            TextScaleControl(
                scale = profile.textScale,
                onScaleChange = { viewModel.setTextScale(it) },
                onAdjust = { viewModel.adjustTextScale(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun CurrentlyUsingLine(nusachLabel: String) {
    val translatedNusach = rememberAppTranslatedText(nusachLabel)
    val line = rememberAppTranslatedTemplate(
        "Currently using: {nusach}",
        mapOf("nusach" to translatedNusach),
    )
    AppText(
        line,
        style = MaterialTheme.typography.labelMedium,
        color = TzaddikColors.NavyMid,
        enableTerms = false,
    )
}

@Composable
private fun LocationPill(
    template: String?,
    templateArgs: Map<String, String>,
    fallbackText: String,
) {
    val display = if (template != null) {
        rememberAppTranslatedTemplate(template, templateArgs)
    } else {
        rememberAppTranslatedText(fallbackText)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(TzaddikColors.NavyDeep.copy(alpha = 0.07f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        AppText(
            display,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = TzaddikColors.NavyMid,
            enableTerms = false,
        )
    }
}

@Composable
private fun SettingsCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(TzaddikColors.ParchTop, TzaddikColors.ParchBase)
                )
            )
            .border(1.dp, TzaddikColors.GoldBorder.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        AppText(
            title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.NavyDeep
        )
        Spacer(Modifier.height(4.dp))
        GoldFlourishDivider(widthFraction = 0.5f, modifier = Modifier.padding(bottom = 12.dp))
        content()
    }
}

@Composable
private fun SettingLabel(label: String, description: String? = null) {
    AppText(
        label,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
        color = TzaddikColors.NavyDeep
    )
    description?.let {
        AppText(
            it,
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted
        )
    }
}

@Composable
private fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(TzaddikColors.GoldBorder.copy(alpha = 0.2f))
    )
}

@Composable
private fun LanguageCard() {
    val selector = LocalLanguageSelector.current
    if (selector.availableLanguages.isEmpty()) return

    var showAllLanguagesDialog by remember { mutableStateOf(false) }
    val offlineLanguages = selector.availableLanguages.filter { it.isOffline }
    val currentCode = selector.currentLanguageCode
    val englishOption = selector.availableLanguages.firstOrNull { it.code == "en" }
    val offlineOptions = listOfNotNull(englishOption) + offlineLanguages.filter { it.code != "en" }
    val currentIsOtherLanguage = offlineOptions.none { it.code == currentCode }
    val currentOtherLanguage = if (currentIsOtherLanguage) {
        selector.availableLanguages.find { it.code == currentCode }
    } else null

    SettingsCard(title = "Language") {
        ProfileToggle(
            label = "Enable Translation",
            checked = selector.enabled,
            onChange = { selector.onEnabledChange(it) },
            description = if (!selector.enabled) "Show the app in a language other than English" else null,
        )

        if (selector.enabled) {
            Spacer(Modifier.height(12.dp))
            AppText(
                AppDisclaimer.TRANSLATION_LINKS_NOTE,
                enableTerms = false,
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.TextMuted,
            )
            Spacer(Modifier.height(8.dp))
            SettingLabel(
                label = "Offline languages",
                description = "No internet required"
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                offlineOptions.forEach { lang ->
                    SettingsChip(
                        label = if (lang.code == "en") "English" else lang.nativeName,
                        selected = currentCode == lang.code,
                        onClick = { selector.onLanguageChange(lang.code) },
                    )
                }
            }

            if (currentOtherLanguage != null) {
                Spacer(Modifier.height(8.dp))
                val resolvedCurrentOther = rememberAppTranslatedTemplate(
                    "Current: {native} ({english})",
                    mapOf("native" to currentOtherLanguage.nativeName, "english" to currentOtherLanguage.englishName),
                )
                AppText(
                    resolvedCurrentOther,
                    enableTerms = false,
                    style = MaterialTheme.typography.bodySmall,
                    color = TzaddikColors.GoldBorder,
                )
            }

            Spacer(Modifier.height(8.dp))
            ParchmentTextButton(
                onClick = { showAllLanguagesDialog = true },
                text = "All languages (online) →",
                contentColor = TzaddikColors.NavyMid,
            )
        }
    }

    if (showAllLanguagesDialog) {
        AllLanguagesDialog(
            languages = selector.availableLanguages.filter { off ->
                offlineOptions.none { it.code == off.code }
            },
            currentCode = currentCode,
            onSelect = { code ->
                selector.onLanguageChange(code)
                showAllLanguagesDialog = false
            },
            onDismiss = { showAllLanguagesDialog = false },
        )
    }
}

@Composable
private fun AllLanguagesDialog(
    languages: List<com.beardytop.beatzaddik.ui.translation.LanguageOption>,
    currentCode: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    ParchmentDialog(
        onDismiss = onDismiss,
        title = "Select Language",
        confirmButton = { GoldButton(onClick = onDismiss, text = "Done") },
    ) {
        AppText(
            "Online languages use Google Translate. Quality varies.",
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted,
        )
        Spacer(Modifier.height(4.dp))
        AppText(
            AppDisclaimer.TRANSLATION_LINKS_NOTE,
            enableTerms = false,
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted,
        )
        Spacer(Modifier.height(10.dp))
        Column(
            modifier = Modifier.fillMaxWidth().heightIn(max = 360.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            languages.forEach { lang ->
                val isSelected = lang.code == currentCode
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) TzaddikColors.GoldBright.copy(alpha = 0.22f)
                            else TzaddikColors.ParchMid.copy(alpha = 0.3f)
                        )
                        .border(
                            if (isSelected) 1.dp else 0.dp,
                            TzaddikColors.GoldBorder.copy(alpha = 0.45f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onSelect(lang.code) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppText(
                        lang.nativeName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        color = if (isSelected) TzaddikColors.NavyDeep else TzaddikColors.TextBrown,
                        modifier = Modifier.weight(1f),
                        enableTerms = false,
                    )
                    AppText(
                        lang.englishName,
                        style = MaterialTheme.typography.bodySmall,
                        color = TzaddikColors.TextMuted,
                        enableTerms = false,
                    )
                }
            }
        }
    }
}

@Composable
private fun DairyToMeatWaitSlider(
    label: String,
    valueMinutes: Int,
    onValueChange: (Int) -> Unit
) {
    val options = KashrutWaitTimes.dairyToMeatMinuteOptions
    val index = KashrutWaitTimes.dairyToMeatOptionIndex(valueMinutes)
    val displayMinutes = options[index]

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppText(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.TextBrown,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(TzaddikColors.GoldBorder.copy(alpha = 0.18f))
                .padding(horizontal = 14.dp, vertical = 5.dp)
        ) {
            val badge = KashrutWaitTimes.waitBadgeForDairyToMeatMinutes(displayMinutes)
            val waitText = rememberAppTranslatedTemplate(badge.templateKey, badge.args)
            AppText(
                waitText,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.NavyDeep,
                textAlign = TextAlign.Center,
                enableTerms = false,
            )
        }
    }
    Slider(
        value = index.toFloat(),
        onValueChange = { onValueChange(options[it.toInt().coerceIn(options.indices)]) },
        valueRange = 0f..(options.size - 1).toFloat(),
        steps = options.size - 2,
        modifier = Modifier.fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = TzaddikColors.GoldBorder,
            activeTrackColor = TzaddikColors.GoldBright
        )
    )
}

@Composable
private fun WaitTimeSlider(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppText(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.TextBrown,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(TzaddikColors.GoldBorder.copy(alpha = 0.18f))
                .padding(horizontal = 14.dp, vertical = 5.dp)
        ) {
            val hourBadge = KashrutWaitTimes.waitBadgeForMeatToDairyHours(value)
            val hourText = rememberAppTranslatedTemplate(hourBadge.templateKey, hourBadge.args)
            AppText(
                hourText,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.NavyDeep,
                textAlign = TextAlign.Center,
                enableTerms = false,
            )
        }
    }
    Slider(
        value = value.toFloat(),
        onValueChange = { onValueChange(it.toInt()) },
        valueRange = 1f..12f,
        steps = 10,
        modifier = Modifier.fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = TzaddikColors.GoldBorder,
            activeTrackColor = TzaddikColors.GoldBright
        )
    )
}

@Composable
private fun ProfileToggle(
    label: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
    description: String? = null,
) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            AppText(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TzaddikColors.TextBrown
            )
            if (!description.isNullOrBlank()) {
                AppText(description, style = MaterialTheme.typography.bodySmall, color = TzaddikColors.TextMuted)
            }
        }
        Spacer(Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TzaddikColors.GoldBright,
                checkedTrackColor = TzaddikColors.GoldBorder.copy(alpha = 0.55f)
            )
        )
    }
}

@Composable
private fun SettingsChip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            AppText(
                label,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = if (selected) TzaddikColors.NavyDeep else TzaddikColors.TextBrown
            )
        },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = TzaddikColors.GoldBright.copy(alpha = 0.38f),
            containerColor = TzaddikColors.ParchMid.copy(alpha = 0.5f)
        )
    )
}
