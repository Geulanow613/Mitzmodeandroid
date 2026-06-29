package com.beardytop.beatzaddik.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import com.beardytop.beatzaddik.ui.components.AppText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.domain.Gender
import com.beardytop.beatzaddik.ui.cityDisplayLabel
import com.beardytop.beatzaddik.ui.filterManualCities
import com.beardytop.beatzaddik.ui.profileMatchesManualCity
import com.beardytop.beatzaddik.domain.NusachSelection
import com.beardytop.beatzaddik.domain.displayLabel
import com.beardytop.beatzaddik.platform.PlatformBackHandler
import com.beardytop.beatzaddik.ui.components.ChecklistSectionHeader
import com.beardytop.beatzaddik.ui.components.HalachicClickableText
import com.beardytop.beatzaddik.ui.components.GoldButton
import com.beardytop.beatzaddik.ui.components.ParchmentContentCard
import com.beardytop.beatzaddik.ui.components.ParchmentTextButton
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.viewmodel.AppViewModel

private const val TOTAL_STEPS = 2

@Composable
fun OnboardingScreen(
    viewModel: AppViewModel,
    appTitle: String = "Be a Tzaddik",
    onBackFromFirstStep: (() -> Unit)? = null
) {
    var step by rememberSaveable { mutableIntStateOf(0) }
    var gender by rememberSaveable { mutableStateOf(Gender.MALE) }
    var married by rememberSaveable { mutableStateOf(false) }
    var children by rememberSaveable { mutableStateOf(false) }
    var nusach by rememberSaveable { mutableStateOf(NusachSelection.NOT_SURE) }
    var useGps by rememberSaveable { mutableStateOf(false) }
    val profile by viewModel.profile.collectAsState()
    val locMsg by viewModel.locationMessage.collectAsState()

    LaunchedEffect(step) {
        if (step == 1) {
            val granted = viewModel.hasLocationPermission()
            useGps = granted
            if (granted) viewModel.refreshGps()
        }
    }

    PlatformBackHandler(enabled = step > 0) {
        if (step > 0) step--
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                targetState = step,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { it } + fadeIn()) togetherWith
                            (slideOutHorizontally { -it } + fadeOut())
                    } else {
                        (slideInHorizontally { -it } + fadeIn()) togetherWith
                            (slideOutHorizontally { it } + fadeOut())
                    }
                },
                label = "step"
            ) { currentStep ->
                when (currentStep) {
                    0 -> AboutYouStep(gender, married, children,
                        onGender = { gender = it },
                        onMarried = { married = it },
                        onChildren = { children = it },
                        modifier = Modifier.fillMaxSize()
                    )
                    else -> PrayerTraditionStep(nusach, useGps, locMsg,
                        onNusach = { nusach = it },
                        onGps = { enabled -> viewModel.setGpsForZmanim(enabled) { useGps = it } },
                        selectedCityId = profile.manualCityId,
                        onSelectCity = { cityId -> viewModel.setManualCity(cityId) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Pinned above the system navigation bar (gesture bar / 3-button nav)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(TOTAL_STEPS) { i ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (i == step) 10.dp else 7.dp)
                            .clip(CircleShape)
                            .background(
                                if (i == step) TzaddikColors.GoldBorder
                                else TzaddikColors.GoldBorder.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            if (step < TOTAL_STEPS - 1) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (step > 0) {
                            OnboardingFooterTextButton(onClick = { step-- }, text = "Back")
                        } else if (onBackFromFirstStep != null) {
                            OnboardingFooterTextButton(onClick = onBackFromFirstStep, text = "Back")
                        }
                        if (step == 0) {
                            OnboardingFooterTextButton(
                                onClick = { viewModel.skipOnboardingWithDefaults() },
                                text = "Skip (set to default)",
                            )
                        }
                    }
                    GoldButton(
                        onClick = {
                            if (step == 0) {
                                viewModel.saveOnboardingAboutYou(gender, married, children)
                            }
                            step++
                        },
                        text = "Continue →"
                    )
                }
            } else {
                OnboardingFooterTextButton(onClick = { step-- }, text = "Back")
                GoldButton(
                    onClick = {
                        viewModel.completeOnboarding(gender, married, children, nusach, useGps)
                    },
                    text = "Start my daily checklist",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/** Text actions on the dark onboarding footer — bright gold so they stay readable on navy. */
@Composable
private fun OnboardingFooterTextButton(onClick: () -> Unit, text: String) {
    ParchmentTextButton(
        onClick = onClick,
        text = text,
        contentColor = TzaddikColors.GoldBright,
    )
}

@Composable
private fun AboutYouStep(
    gender: Gender,
    married: Boolean,
    children: Boolean,
    onGender: (Gender) -> Unit,
    onMarried: (Boolean) -> Unit,
    onChildren: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var scale by rememberSaveable { mutableStateOf(1f) }
    ParchmentContentCard(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoomChange, _ ->
                    scale = (scale * zoomChange).coerceIn(0.85f, 2.5f)
                }
            }
    ) {
        AppText(
            "Tell us about yourself",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.NavyDeep
        )
        Spacer(Modifier.height(12.dp))
        AppText(
            "This checklist tries to include the basic Torah obligations and relevant information for Jewish people to live a rewarding Torah lifestyle.\n\n" +
                "The details differ depending on your gender and marital situation. No data is shared with anyone, it all stays locally on your device.",
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted
        )
        Spacer(Modifier.height(12.dp))
        ChecklistSectionHeader("I am")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StyledChip(
                label = "Male",
                selected = gender == Gender.MALE,
                onClick = { onGender(Gender.MALE) },
                modifier = Modifier.weight(1f)
            )
            StyledChip(
                label = "Female",
                selected = gender == Gender.FEMALE,
                onClick = { onGender(Gender.FEMALE) },
                modifier = Modifier.weight(1f)
            )
        }
        StyledChip(
            label = Gender.PREFER_NOT_TO_SAY.displayLabel(),
            selected = gender == Gender.PREFER_NOT_TO_SAY,
            onClick = { onGender(Gender.PREFER_NOT_TO_SAY) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        ToggleRow("I am married", married) { onMarried(it) }
        ToggleRow("I have children", children) { onChildren(it) }
    }
}

@Composable
private fun PrayerTraditionStep(
    nusach: NusachSelection,
    useGps: Boolean,
    locMsg: String?,
    onNusach: (NusachSelection) -> Unit,
    onGps: (Boolean) -> Unit,
    selectedCityId: String?,
    onSelectCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var cityQuery by rememberSaveable { mutableStateOf("") }
    val filteredCities = remember(cityQuery) { filterManualCities(cityQuery) }
    ParchmentContentCard(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AppText(
            "Prayer tradition & location",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.NavyDeep
        )
        Spacer(Modifier.height(4.dp))
        HalachicClickableText(
            text = "Different Jewish communities have slightly different prayer traditions (nusach). Choosing yours helps the app show the correct prayer text and customs.",
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted,
        )
        Spacer(Modifier.height(12.dp))
        ChecklistSectionHeader("My prayer tradition (Nusach)")

        NusachOption(
            label = "Ashkenaz",
            description = "The prayer tradition common in many European-origin communities.",
            selected = nusach == NusachSelection.ASHKENAZ,
            onClick = { onNusach(NusachSelection.ASHKENAZ) }
        )
        NusachOption(
            label = "Sephardi",
            description = "Prayer tradition of Iberian and Mediterranean diaspora communities (Nusach Sefard / Bet Yosef). Halachic guidance in this app follows Shulchan Aruch and poskim such as Rav Ovadia Yosef where noted.",
            selected = nusach == NusachSelection.SEFARD,
            onClick = { onNusach(NusachSelection.SEFARD) }
        )
        NusachOption(
            label = "Edot HaMizrach",
            description = "Prayer tradition of Middle Eastern and North African kehillot (Iraqi, Syrian, Moroccan, Persian, Yemenite, and related communities), usually Nusach Edot HaMizrach. Customs vary by community — follow your kehilla and rav.",
            selected = nusach == NusachSelection.EDOT_HAMIZRACH,
            onClick = { onNusach(NusachSelection.EDOT_HAMIZRACH) }
        )
        NusachOption(
            label = "Chabad — Nusach Ari",
            description = "Used by Chabad-Lubavitch. Siddur: Tehillat Hashem. Not the same as Sephardi or Edot HaMizrach.",
            selected = nusach == NusachSelection.CHABAD,
            onClick = { onNusach(NusachSelection.CHABAD) }
        )
        NusachOption(
            label = "I'm not sure",
            description = "The holy Ari (Rabbi Isaac Luria) taught that someone who does not know their family's prayer tradition should follow Nusach Ari. This app uses that for your checklist until you choose otherwise.",
            selected = nusach == NusachSelection.NOT_SURE,
            onClick = { onNusach(NusachSelection.NOT_SURE) }
        )

        Spacer(Modifier.height(12.dp))
        ChecklistSectionHeader("Location (for prayer times)")
        AppText(
            "The app shows accurate prayer times (zmanim) when it knows your location — sunrise, sunset, Shema deadline, and more.",
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted
        )
        Spacer(Modifier.height(8.dp))
        ToggleRow("Use GPS for accurate zmanim", useGps) { onGps(it) }
        locMsg?.let {
            AppText(it, style = MaterialTheme.typography.bodySmall, color = TzaddikColors.TextMuted)
        }
        AppText(
            "You can also set a city manually in Settings after setup.",
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.height(10.dp))
        ChecklistSectionHeader("Choose city now (no GPS)")
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
                .height(170.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            filteredCities.forEach { city ->
                StyledChip(
                    label = cityDisplayLabel(city),
                    selected = profileMatchesManualCity(selectedCityId, city),
                    onClick = { onSelectCity(city.id) },
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

@Composable
private fun NusachOption(
    label: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    StyledChip(
        label = label,
        selected = selected,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
    )
    if (selected) {
        AppText(
            description,
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.NavyMid,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
        )
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppText(label, color = TzaddikColors.TextBrown, modifier = Modifier.weight(1f), enableTerms = false)
        Spacer(Modifier.width(8.dp))
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TzaddikColors.GoldBright,
                checkedTrackColor = TzaddikColors.GoldBorder.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun StyledChip(
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
                color = if (selected) TzaddikColors.NavyDeep else TzaddikColors.TextBrown,
                enableTerms = false
            )
        },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = TzaddikColors.GoldBright.copy(alpha = 0.35f),
            containerColor = TzaddikColors.ParchMid.copy(alpha = 0.6f)
        )
    )
}
