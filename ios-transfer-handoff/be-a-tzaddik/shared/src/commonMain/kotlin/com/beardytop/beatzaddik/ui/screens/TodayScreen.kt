package com.beardytop.beatzaddik.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import com.beardytop.beatzaddik.ui.components.AppText
import com.beardytop.beatzaddik.ui.translation.LocalAppTranslation
import com.beardytop.beatzaddik.ui.translation.embedLtrForRtlMix
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedTemplate
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedText
import com.beardytop.beatzaddik.ui.translation.rememberLocalizedZmanInlineHint
import com.beardytop.beatzaddik.ui.translation.rememberLocalizedZmanPeriodHint
import com.beardytop.beatzaddik.ui.translation.isAppRtlLanguage
import com.beardytop.beatzaddik.ui.translation.AppDirectionalLayout
import kotlinx.datetime.LocalDate
import androidx.compose.ui.text.TextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.beardytop.beatzaddik.ui.translation.RuntimeZmanLocalization
import com.beardytop.beatzaddik.ui.translation.formatRtlUpcomingTimingCluster
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.ui.components.GUIDE_TERM_TAG
import com.beardytop.beatzaddik.ui.components.HalachicTermsPage
import com.beardytop.beatzaddik.ui.components.LocalOpenShabbatGuide
import com.beardytop.beatzaddik.ui.components.drawHalachicTermUnderlines
import com.beardytop.beatzaddik.ui.components.halachicTermUnderlineColor
import com.beardytop.beatzaddik.domain.DayChecklists
import com.beardytop.beatzaddik.domain.EffectiveNusach
import com.beardytop.beatzaddik.domain.OmerCountText
import com.beardytop.beatzaddik.domain.ParshaData
import com.beardytop.beatzaddik.domain.HolyDayPhoneNotice
import com.beardytop.beatzaddik.domain.ChecklistDebugScenarios
import com.beardytop.beatzaddik.domain.forDebugCalendar
import com.beardytop.beatzaddik.domain.ChecklistSectionOrder
import com.beardytop.beatzaddik.domain.Gender
import com.beardytop.beatzaddik.domain.ItemZmanAvailability
import com.beardytop.beatzaddik.domain.MealCategory
import com.beardytop.beatzaddik.domain.ResolvedChecklistItem
import com.beardytop.beatzaddik.domain.TimeOfDay
import com.beardytop.beatzaddik.domain.TzeitDay
import com.beardytop.beatzaddik.domain.UserProfile
import com.beardytop.beatzaddik.domain.UpcomingHoliday
import com.beardytop.beatzaddik.domain.ZmanimFormatter
import com.beardytop.beatzaddik.ui.ChecklistPeriodScroll
import com.beardytop.beatzaddik.ui.components.ChecklistDebugMenu
import com.beardytop.beatzaddik.ui.components.ZmanimLocationBanner
import com.beardytop.beatzaddik.ui.components.CollapsibleChecklistSectionHeader
import com.beardytop.beatzaddik.ui.components.CurrentTimeLine
import com.beardytop.beatzaddik.ui.components.GoldButton
import com.beardytop.beatzaddik.ui.components.HolyFlashController
import com.beardytop.beatzaddik.ui.components.HolyLightChecklistRow
import com.beardytop.beatzaddik.ui.components.MitzvahInfoDialog
import com.beardytop.beatzaddik.ui.components.ParchmentContentCard
import com.beardytop.beatzaddik.ui.components.HalachicClickableText
import com.beardytop.beatzaddik.ui.components.ParchmentDialog
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.ui.tour.LocalTourTargetReporter
import com.beardytop.beatzaddik.ui.tour.TourTarget
import com.beardytop.beatzaddik.ui.tour.reportTourTarget
import com.beardytop.beatzaddik.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TodayScreen(
    viewModel: AppViewModel,
    holyFlash: HolyFlashController,
    onOpenTimer: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onChecklistItemChecked: ((itemId: String, title: String) -> Unit)? = null,
    mitzvotCount: Int? = null,
    onDebugSetMitzvotCount: ((Int) -> Unit)? = null,
    onDebugShowRatingPrompt: (() -> Unit)? = null,
) {
    val day by viewModel.dayChecklists.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val progress by viewModel.requiredProgress.collectAsState()
    val kashrut by viewModel.kashrutWait.collectAsState()
    val upcoming by viewModel.upcomingHolidays.collectAsState()
    val debugOverride by viewModel.checklistDebugOverride.collectAsState()
    val debugMenuVisible by viewModel.checklistDebugMenuVisible.collectAsState()
    val displayTimezone = remember(debugOverride, profile.timezoneId) {
        val scenario = debugOverride?.scenarioId?.let { ChecklistDebugScenarios.byId(it) }
        profile.forDebugCalendar(scenario).timezoneId
    }
    var customText by remember { mutableStateOf("") }
    var infoItem by remember { mutableStateOf<ResolvedChecklistItem?>(null) }
    var guideTopic by remember { mutableStateOf<GuideTopic?>(null) }
    var omerExplainerTemplate by remember { mutableStateOf<String?>(null) }
    var omerExplainerArgs by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    val scrollState = rememberScrollState()
    val scrollScope = rememberCoroutineScope()
    var periodScrollOffsets by remember { mutableStateOf<Map<TimeOfDay, Int>>(emptyMap()) }
    var scrollToPeriodRequest by remember { mutableStateOf<TimeOfDay?>(null) }
    var expandPeriodRequest by remember { mutableStateOf<TimeOfDay?>(null) }
    var scrollRootY by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(scrollToPeriodRequest, periodScrollOffsets) {
        val period = scrollToPeriodRequest ?: return@LaunchedEffect
        periodScrollOffsets[period]?.let { y ->
            scrollState.animateScrollTo(y.coerceAtLeast(0))
        }
        scrollToPeriodRequest = null
    }

    fun scrollToChecklistPeriod(period: TimeOfDay) {
        expandPeriodRequest = period
        scrollScope.launch {
            delay(120)
            scrollToPeriodRequest = period
        }
    }

    val openShabbatGuide = LocalOpenShabbatGuide.current ?: {}
    val pageKey = remember(day?.header?.civilDateLabel, day?.items?.size) {
        "${day?.header?.civilDateLabel.orEmpty()}|${day?.items?.size ?: 0}"
    }
    val appTranslation = LocalAppTranslation.current
    val displayLang = appTranslation.displayLanguageCode
    val tourReporter = LocalTourTargetReporter.current

    AppDirectionalLayout(displayLang) {
    HalachicTermsPage(key = pageKey) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .onGloballyPositioned { scrollRootY = it.positionInRoot().y }
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        ParchmentContentCard {
            if (debugMenuVisible) {
                ChecklistDebugMenu(
                    viewModel = viewModel,
                    activeOverride = debugOverride,
                    timezoneId = displayTimezone,
                    mitzvotCount = mitzvotCount,
                    onDebugSetMitzvotCount = onDebugSetMitzvotCount,
                    onDebugShowRatingPrompt = onDebugShowRatingPrompt,
                )
            }
            Column(
                modifier = Modifier.reportTourTarget(TourTarget.HeaderUpcoming, tourReporter),
            ) {
                CalendarHeader(
                    day = day,
                    timezoneId = displayTimezone,
                    locationLabel = profile.locationLabel,
                    manualCityId = profile.manualCityId,
                    effectiveNusach = profile.effectiveNusach(),
                    upcoming = upcoming,
                    nowEpochMillis = debugOverride?.epochMillis,
                    onNusachClick = onOpenSettings,
                    onPeriodClick = { day?.let { scrollToChecklistPeriod(it.activePeriod) } },
                    onOpenShabbatGuide = openShabbatGuide,
                    onOpenGuideTopic = { topic -> guideTopic = topic },
                    onOpenOmerExplainer = {
                        omerExplainerTemplate = day?.header?.omerExplainerTemplate
                        omerExplainerArgs = day?.header?.omerExplainerArgs ?: emptyMap()
                    },
                )
                if (!profile.hasZmanimLocation()) {
                    ZmanimLocationBanner(
                        onOpenSettings = onOpenSettings,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                }
                if (upcoming.isNotEmpty()) {
                    UpcomingHolidaysBlock(
                        holidays = upcoming,
                        onOpenGuideTopic = { topic -> guideTopic = topic },
                        onOpenShabbatGuide = openShabbatGuide,
                    )
                }
            }
            kashrut?.let { wait ->
                var isDone by remember(wait.endsAtEpochMillis) {
                    mutableStateOf(Clock.System.now().toEpochMilliseconds() >= wait.endsAtEpochMillis)
                }
                LaunchedEffect(wait.endsAtEpochMillis) {
                    val remaining = wait.endsAtEpochMillis - Clock.System.now().toEpochMilliseconds()
                    if (remaining > 0) delay(remaining)
                    isDone = true
                }
                if (isDone) {
                    val allowedFood = when (wait.category) {
                        MealCategory.MEAT -> "dairy"
                        MealCategory.DAIRY -> "meat"
                    }
                    val endedAt = ZmanimFormatter.formatTimeWithDate(
                        wait.endsAtEpochMillis,
                        profile.timezoneId
                    )
                    val translatedFood = rememberAppTranslatedText(allowedFood)
                    val translatedNow = rememberAppTranslatedText("now")
                    val kashrutMessage = rememberAppTranslatedTemplate(
                        "You can now eat {food} (as of {time})",
                        mapOf(
                            "food" to translatedFood,
                            "time" to (endedAt ?: translatedNow),
                        ),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(TzaddikColors.GoldBright.copy(alpha = 0.25f))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            kashrutMessage,
                            color = TzaddikColors.NavyDeep,
                            modifier = Modifier.weight(1f)
                        )
                        AppText(
                            "Clear",
                            color = TzaddikColors.NavyMid,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.clickable { viewModel.clearKashrut() }
                        )
                    }
                } else {
                    AssistChip(
                        onClick = onOpenTimer,
                        label = { AppText("Kashrut timer active — tap to view", color = TzaddikColors.NavyDeep) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = TzaddikColors.GoldBright.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
            if (day == null) {
                AppText("Loading checklist…", color = TzaddikColors.TextMuted, modifier = Modifier.padding(12.dp))
            }
            day?.let { d ->
                if (d.holyDayPhoneNotice != null) {
                    HolyDayPhoneNoticeCard(d.holyDayPhoneNotice)
                    Spacer(Modifier.height(12.dp))
                    return@let
                }
                Column(
                    modifier = Modifier.reportTourTarget(TourTarget.Checklist, tourReporter),
                ) {
                val (done, total) = progress
                if (total > 0) {
                    val allDone = done == total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            if (allDone) "All done for now!" else "Today's mitzvot",
                            enableTerms = false,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = if (allDone) TzaddikColors.GoldBorder else TzaddikColors.NavyDeep
                        )
                        Text(
                            "$done / $total",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (allDone) TzaddikColors.GoldBorder else TzaddikColors.NavyMid
                        )
                    }
                    LinearProgressIndicator(
                        progress = { done.toFloat() / total },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        color = if (allDone) TzaddikColors.GoldBright else TzaddikColors.GoldBorder,
                        trackColor = TzaddikColors.GoldBorder.copy(alpha = 0.2f)
                    )
                    if (allDone) {
                        AppText(
                            "Every mitzvah you do today matters. Keep going.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TzaddikColors.GoldBorder,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
                if (profile.hasZmanimLocation()) {
                    ClickablePeriodLabel(
                        label = d.activePeriodLabel,
                        onClick = { scrollToChecklistPeriod(d.activePeriod) }
                    )
                    d.activePeriodHint?.let { hint ->
                        val localizedHint = rememberLocalizedZmanPeriodHint(hint)
                        AppText(
                            localizedHint,
                            enableTerms = false,
                            style = MaterialTheme.typography.bodySmall,
                            color = TzaddikColors.TextMuted,
                        )
                    }
                }
                ChecklistSections(
                    items = d.items,
                    viewModel = viewModel,
                    holyFlash = holyFlash,
                    onInfo = { infoItem = it },
                    activePeriod = d.activePeriod,
                    profile = profile,
                    prioritizePrepSections = d.prioritizePrepSections,
                    sinkMorningPrayerSection = d.sinkMorningPrayerSection,
                    expandPeriodRequest = expandPeriodRequest,
                    onExpandPeriodConsumed = { expandPeriodRequest = null },
                    scrollRootY = scrollRootY,
                    scrollInProgress = scrollState.isScrollInProgress,
                    onPeriodAnchorPosition = { period, y ->
                        periodScrollOffsets = periodScrollOffsets + (period to y)
                    },
                    clockNowEpochMillis = debugOverride?.epochMillis,
                    onChecklistItemChecked = onChecklistItemChecked,
                )
                } // checklist tour target
            }
            if (day?.holyDayPhoneNotice == null) {
                Spacer(Modifier.height(12.dp))
                AppText("Add custom mitzvah", style = MaterialTheme.typography.titleSmall, color = TzaddikColors.NavyDeep)
                OutlinedTextField(
                    value = customText,
                    onValueChange = { customText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { AppText("Type something for today…", color = TzaddikColors.TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TzaddikColors.GoldBorder,
                        unfocusedBorderColor = TzaddikColors.GoldBorder.copy(alpha = 0.4f),
                        cursorColor = TzaddikColors.GoldBorder
                    )
                )
                GoldButton(
                    onClick = {
                        if (customText.isNotBlank()) {
                            viewModel.addCustomItem(customText)
                            customText = ""
                        }
                    },
                    text = "Add",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    infoItem?.let { item ->
        MitzvahInfoDialog(item = item, onDismiss = { infoItem = null })
    }
    guideTopic?.let { topic ->
        GuideTopicExplainerDialog(topic = topic, onDismiss = { guideTopic = null })
    }
    omerExplainerTemplate?.let { template ->
        val translatedBody = rememberAppTranslatedTemplate(template, omerExplainerArgs)
        ParchmentDialog(
            onDismiss = {
                omerExplainerTemplate = null
                omerExplainerArgs = emptyMap()
            },
            title = "Sefirat HaOmer",
            confirmButton = {
                GoldButton(
                    onClick = {
                        omerExplainerTemplate = null
                        omerExplainerArgs = emptyMap()
                    },
                    text = "Done",
                )
            },
        ) {
            HalachicClickableText(
                text = translatedBody,
                style = MaterialTheme.typography.bodyMedium,
                color = TzaddikColors.NavyDeep,
            )
        }
    }
    }
    }
}

@Composable
private fun HolyDayPhoneNoticeCard(notice: HolyDayPhoneNotice) {
    val resolvedMessage = if (notice.messageArgs.isNotEmpty()) {
        rememberAppTranslatedTemplate(notice.message, notice.messageArgs)
    } else {
        rememberAppTranslatedText(notice.message)
    }
    val resolvedFooter = rememberAppTranslatedText(notice.footer)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(TzaddikColors.NavyMid.copy(alpha = 0.92f), RoundedCornerShape(14.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Outlined.NightsStay,
            contentDescription = null,
            tint = TzaddikColors.GoldBright,
            modifier = Modifier.height(48.dp)
        )
        Spacer(Modifier.height(12.dp))
        AppText(
            notice.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.GoldBright,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        HalachicClickableText(
            text = resolvedMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.ParchTop,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        AppText(
            resolvedFooter,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = TzaddikColors.GoldBorder,
            textAlign = TextAlign.Center
        )
    }
}

private fun sectionSortKey(
    section: String,
    activePeriod: TimeOfDay,
    prioritizePrepSections: Set<String> = emptySet(),
    sinkMorningPrayerSection: Boolean = false,
): Int {
    // Force the two ongoing groups to the bottom (below everything else).
    if (section == "Daily ongoing mitzvot") return 100_000
    if (section == "Permanent ongoing mitzvot") return 100_001
    return ChecklistSectionOrder.sortIndex(
        section,
        activePeriod,
        prioritizePrepSections,
        sinkMorningPrayerSection,
    )
}

/** Sections that start collapsed — mostly one-time or infrequent setup. */
private val defaultCollapsedSectionNames = setOf(
    "Important Lifestyle Mitzvot",
    // "Married women's mitzvot" is intentionally expanded by default when present.
    // Prepare for Shabbat / Prepare for the festival / Pesach prep stay expanded when prioritized.
    // Sefirat HaOmer stays expanded by default so a missed nighttime count is visible during the day.
    "Chanukah",
    "Purim"
)

private fun defaultCollapsedSectionKeys(
    sectionPrefix: String,
    activePeriod: TimeOfDay,
    profile: UserProfile,
    prioritizePrepSections: Set<String> = emptySet(),
): Set<String> {
    val names = defaultCollapsedSectionNames.toMutableSet()
    prioritizePrepSections.forEach { names.remove(it) }
    if (profile.gender == Gender.FEMALE) {
        if (profile.married) {
            names.remove("Important Lifestyle Mitzvot")
        }
    }
    return names.map { sectionPrefix + it }.toSet()
}

@Composable
private fun ClickablePeriodLabel(
    label: String,
    onClick: () -> Unit
) {
    AppText(
        label,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        ),
        color = TzaddikColors.NavyDeep,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp)
    )
}

@Composable
private fun ChecklistSections(
    items: List<ResolvedChecklistItem>,
    viewModel: AppViewModel,
    holyFlash: HolyFlashController,
    onInfo: (ResolvedChecklistItem) -> Unit,
    sectionPrefix: String = "",
    activePeriod: TimeOfDay = TimeOfDay.DAY,
    collapsibleSections: Boolean = true,
    profile: UserProfile = UserProfile(),
    prioritizePrepSections: Set<String> = emptySet(),
    sinkMorningPrayerSection: Boolean = false,
    expandPeriodRequest: TimeOfDay? = null,
    onExpandPeriodConsumed: () -> Unit = {},
    scrollRootY: Float = 0f,
    scrollInProgress: Boolean = false,
    onPeriodAnchorPosition: (TimeOfDay, Int) -> Unit = { _, _ -> },
    /** When set (e.g. checklist debug), live zman rows use this clock instead of wall time. */
    clockNowEpochMillis: Long? = null,
    onChecklistItemChecked: ((itemId: String, title: String) -> Unit)? = null,
) {
    if (items.isEmpty()) {
        AppText(
            "No mitzvot on your checklist for today.",
            modifier = Modifier.padding(vertical = 12.dp),
            color = TzaddikColors.TextMuted
        )
        return
    }
    // Default collapsed behavior:
    // - Keep the "setup / infrequent" sections collapsed (existing behavior)
    // - Additionally: sections whose items are not ACTIVE *and* whose period does not match
    //   the current period start collapsed (but remain expandable via the header toggle).
    val inactiveOtherPeriodKeys = remember(items, activePeriod, sectionPrefix) {
        items.groupBy { it.sectionLabel }
            .mapNotNull { (sectionLabel, sectionItems) ->
                val hasActive = sectionItems.any { it.zmanAvailability == ItemZmanAvailability.ACTIVE }
                if (hasActive) return@mapNotNull null
                val baseSection = sectionItems.firstOrNull()?.def?.section ?: return@mapNotNull null
                val period = ChecklistPeriodScroll.periodForBaseSection(baseSection) ?: return@mapNotNull null
                if (period == activePeriod) return@mapNotNull null
                sectionPrefix + sectionLabel
            }
            .distinct()
            .sorted()
    }

    var collapsedSectionKeys by rememberSaveable(
        sectionPrefix,
        activePeriod,
        profile.married,
        profile.gender,
        prioritizePrepSections,
        // Recompute defaults when availability shifts materially (e.g. morning→afternoon).
        inactiveOtherPeriodKeys,
    ) {
        val defaults = (
            defaultCollapsedSectionKeys(
                sectionPrefix,
                activePeriod,
                profile,
                prioritizePrepSections,
            ) + inactiveOtherPeriodKeys
            ).toMutableSet()
        if (profile.dailyOngoingCollapsed) {
            defaults += sectionPrefix + "Daily ongoing mitzvot"
        }
        if (profile.permanentOngoingCollapsed) {
            defaults += sectionPrefix + "Permanent ongoing mitzvot"
        }
        mutableStateOf(defaults.distinct().toList())
    }

    LaunchedEffect(expandPeriodRequest) {
        val period = expandPeriodRequest ?: return@LaunchedEffect
        val targetBase = ChecklistPeriodScroll.targetBaseSection(period)
        val sectionLabel = items.firstOrNull { it.def.section == targetBase }?.sectionLabel
        if (sectionLabel != null) {
            val key = sectionPrefix + sectionLabel
            collapsedSectionKeys = collapsedSectionKeys.filterNot { it == key }
        }
        onExpandPeriodConsumed()
    }

    // Keep ongoing collapse in sync if profile changes from outside (e.g. new-day re-expand).
    LaunchedEffect(profile.dailyOngoingCollapsed, profile.permanentOngoingCollapsed, sectionPrefix) {
        val dailyKey = sectionPrefix + "Daily ongoing mitzvot"
        val permanentKey = sectionPrefix + "Permanent ongoing mitzvot"
        collapsedSectionKeys = collapsedSectionKeys
            .filterNot { it == dailyKey || it == permanentKey }
            .let { base ->
                buildList {
                    addAll(base)
                    if (profile.dailyOngoingCollapsed) add(dailyKey)
                    if (profile.permanentOngoingCollapsed) add(permanentKey)
                }
            }
    }

    val collapsedSet = remember(collapsedSectionKeys) { collapsedSectionKeys.toSet() }
    val sortedSections = remember(items, activePeriod, prioritizePrepSections, sinkMorningPrayerSection) {
        items.groupBy { it.sectionLabel }
            .toList()
            .sortedBy { (section, _) ->
                sectionSortKey(section, activePeriod, prioritizePrepSections, sinkMorningPrayerSection)
            }
    }
    sortedSections.forEach { (section, sectionItems) ->
            val sectionKey = sectionPrefix + section
            val expanded = sectionKey !in collapsedSet
            val baseSection = sectionItems.firstOrNull()?.def?.section
            val periodAnchor = baseSection?.let { base ->
                ChecklistPeriodScroll.periodForBaseSection(base)?.takeIf { period ->
                    ChecklistPeriodScroll.targetBaseSection(period) == base
                }
            }
            val anchorModifier = if (periodAnchor != null) {
                Modifier.onGloballyPositioned { coords ->
                    if (!scrollInProgress) {
                        val yInScroll = (coords.positionInRoot().y - scrollRootY).toInt()
                        onPeriodAnchorPosition(periodAnchor, yInScroll)
                    }
                }
            } else {
                Modifier
            }
            if (collapsibleSections) {
                val ongoingSubtitle = when (section) {
                    "Permanent ongoing mitzvot" -> "Click once — it will stay checked."
                    "Daily ongoing mitzvot" -> "Click each day you fulfilled this mitzvah (resets at nightfall)."
                    "Eating & Blessings" -> "Check off if you observe these mitzvot today (resets at nightfall)"
                    else -> null
                }
                CollapsibleChecklistSectionHeader(
                    title = section,
                    subtitle = ongoingSubtitle,
                    expanded = expanded,
                    itemCount = sectionItems.size,
                    onToggle = {
                        val willCollapse = expanded
                        collapsedSectionKeys = if (willCollapse) {
                            collapsedSectionKeys + sectionKey
                        } else {
                            collapsedSectionKeys - sectionKey
                        }
                        when (section) {
                            "Permanent ongoing mitzvot",
                            "Daily ongoing mitzvot" -> {
                                viewModel.setOngoingSectionCollapsed(section, collapsed = willCollapse)
                            }
                        }
                    },
                    modifier = anchorModifier
                )
            } else {
                AppText(
                    section,
                    enableTerms = false,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = TzaddikColors.NavyMid,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                when (section) {
                    "Permanent ongoing mitzvot" -> AppText(
                        "Click once — it will stay checked.",
                        enableTerms = false,
                        style = MaterialTheme.typography.bodySmall,
                        color = TzaddikColors.TextMuted,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    "Daily ongoing mitzvot" -> AppText(
                        "Click each day you fulfilled this mitzvah (resets at nightfall).",
                        enableTerms = false,
                        style = MaterialTheme.typography.bodySmall,
                        color = TzaddikColors.TextMuted,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                    "Eating & Blessings" -> AppText(
                        "Check off if you observe these mitzvot today (resets at nightfall)",
                        enableTerms = false,
                        style = MaterialTheme.typography.bodySmall,
                        color = TzaddikColors.TextMuted,
                        modifier = Modifier.padding(bottom = 6.dp),
                    )
                }
            }
            val showItems = !collapsibleSections || expanded
            AnimatedVisibility(
                visible = showItems,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    sectionItems.sortedWith(compareBy({ it.def.sortOrder }, { it.displayTitle })).forEach { item ->
                        val isCustom = item.def.id.startsWith("custom_")
                        key(item.def.id) {
                        HolyLightChecklistRow(
                            item = item,
                            timezoneId = profile.timezoneId,
                            effectiveNusach = profile.effectiveNusach(),
                            clockNowEpochMillis = clockNowEpochMillis,
                            onCheckedChange = { checked ->
                                if (item.zmanAvailability == ItemZmanAvailability.ACTIVE) {
                                    when {
                                        item.def.monthlyMitzvah ->
                                            viewModel.setMonthlyChecked(item.def.id, checked)
                                        item.def.weeklyMitzvah ->
                                            viewModel.setWeeklyChecked(item.def.id, checked)
                                        item.def.tzeitMitzvah || item.def.id == TzeitDay.WOMENS_DAILY_PRAYER_ID ->
                                            viewModel.setTzeitDayChecked(item.def.id, checked)
                                        else ->
                                            viewModel.setChecked(item.def.id, checked, item.def.persistChecked)
                                    }
                                    if (checked) {
                                        onChecklistItemChecked?.invoke(item.def.id, item.displayTitle)
                                    }
                                }
                            },
                            onInfoClick = { onInfo(item) },
                            onHolyFlash = { holyFlash.trigger() },
                            onRemove = if (isCustom) {
                                { viewModel.removeCustomItem(item.def.id) }
                            } else null
                        )
                        }
                    }
                }
            }
        }
}

private fun upcomingWhenLabel(holiday: UpcomingHoliday): String =
    holiday.whenLabelOverride ?: when {
        holiday.daysAway == 0 && holiday.beginsTonightWhenImminent -> "Tonight"
        holiday.daysAway == 0 -> "today"
        holiday.daysAway == 1 -> "tomorrow"
        else -> "in {count} days"
    }

private const val UPCOMING_HOLIDAY_NAME_TAG = "upcoming_holiday_name"

@Composable
private fun UpcomingHolidayRtlRow(
    translatedName: String,
    holidayName: String,
    whenPart: String,
    timingCluster: String?,
    bodyStyle: TextStyle,
    timingStyle: TextStyle,
    onNameClick: () -> Unit,
) {
    val subtextColor = TzaddikColors.ParchTop.copy(alpha = 0.68f)
    val annotated = remember(translatedName, holidayName, whenPart, timingCluster, timingStyle) {
        buildAnnotatedString {
            pushStringAnnotation(UPCOMING_HOLIDAY_NAME_TAG, holidayName)
            withStyle(
                SpanStyle(
                    color = TzaddikColors.ParchTop,
                    textDecoration = TextDecoration.Underline,
                )
            ) {
                append(translatedName)
            }
            pop()
            withStyle(SpanStyle(color = subtextColor)) {
                append(whenPart)
            }
            timingCluster?.takeIf { it.isNotBlank() }?.let { cluster ->
                withStyle(
                    SpanStyle(
                        color = subtextColor,
                        fontSize = timingStyle.fontSize,
                        fontWeight = timingStyle.fontWeight,
                    )
                ) {
                    append(" · $cluster")
                }
            }
        }
    }
    var textLayout by remember(annotated) { mutableStateOf<TextLayoutResult?>(null) }
    Text(
        text = annotated,
        style = bodyStyle,
        textAlign = TextAlign.Start,
        onTextLayout = { textLayout = it },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(annotated) {
                detectTapGestures { position ->
                    textLayout?.let { layout ->
                        val offset = layout.getOffsetForPosition(position)
                        if (annotated.getStringAnnotations(UPCOMING_HOLIDAY_NAME_TAG, offset, offset).isNotEmpty()) {
                            onNameClick()
                        }
                    }
                }
            },
    )
}

@Composable
private fun UpcomingHolidayLine(
    holiday: UpcomingHoliday,
    whenLabel: String,
    openTopic: GuideTopic,
    onOpenGuideTopic: (GuideTopic) -> Unit,
) {
    val bodyStyle = MaterialTheme.typography.bodyMedium
    val timingStyle = MaterialTheme.typography.labelSmall
    val translatedName = rememberAppTranslatedText(holiday.name)
    val translatedWhenLabel = when {
        holiday.whenLabelOverride != null -> rememberAppTranslatedText(whenLabel)
        holiday.daysAway > 1 -> rememberAppTranslatedTemplate(
            "in {count} days",
            mapOf("count" to holiday.daysAway.toString()),
        )
        else -> rememberAppTranslatedText(whenLabel)
    }
    val rawHint = holiday.timingHint.orEmpty()
    val isCandlesHint = rawHint.startsWith("Candles ")
    val candlesTimeRaw = if (isCandlesHint) rawHint.removePrefix("Candles ").trim() else ""
    val candlesTimeLastSpace = candlesTimeRaw.lastIndexOf(' ')
    val candlesTimePart = when {
        !isCandlesHint -> ""
        candlesTimeLastSpace > 0 -> candlesTimeRaw.substring(0, candlesTimeLastSpace)
        else -> candlesTimeRaw
    }
    val candlesWeekdayKey = if (isCandlesHint && candlesTimeLastSpace > 0) {
        candlesTimeRaw.substring(candlesTimeLastSpace + 1)
    } else {
        ""
    }
    val appTranslation = LocalAppTranslation.current
    val displayLang = appTranslation.displayLanguageCode
    val isRtl = isAppRtlLanguage(displayLang)
    val translatedCandlesWeekday = rememberAppTranslatedText(candlesWeekdayKey)
    val translatedCandlesLabel = rememberAppTranslatedText("Candles")
    val localizedCandlesClock = remember(candlesTimePart, displayLang) {
        ZmanimFormatter.reformatClockStringForLanguage(candlesTimePart, displayLang)
    }
    val rtlTimingCluster = remember(
        isCandlesHint,
        rawHint,
        localizedCandlesClock,
        translatedCandlesWeekday,
        translatedCandlesLabel,
        displayLang,
    ) {
        when {
            isCandlesHint && localizedCandlesClock.isNotBlank() ->
                formatRtlUpcomingTimingCluster(
                    listOf(translatedCandlesLabel, localizedCandlesClock, translatedCandlesWeekday),
                )
            !isCandlesHint && rawHint.isNotBlank() ->
                RuntimeZmanLocalization.localizeInlineZmanHintForRow(rawHint, displayLang)
            else -> null
        }
    }
    val embeddedCandlesTime = remember(localizedCandlesClock, translatedCandlesWeekday, displayLang) {
        val displayTime = if (translatedCandlesWeekday.isNotBlank()) {
            "$localizedCandlesClock $translatedCandlesWeekday"
        } else {
            localizedCandlesClock
        }
        if (ZmanimFormatter.uses24HourClock(displayLang)) {
            embedLtrForRtlMix(displayTime)
        } else {
            displayTime
        }
    }
    val translatedCandlesHint = rememberAppTranslatedTemplate(
        "Candles {time}",
        mapOf("time" to if (isCandlesHint) embeddedCandlesTime else ""),
    )
    val localizedPlainHint = rememberLocalizedZmanInlineHint(if (!isCandlesHint) rawHint else "")
    val translatedTimingHint = when {
        isCandlesHint && embeddedCandlesTime.isNotBlank() -> translatedCandlesHint
        !isCandlesHint && rawHint.isNotBlank() -> localizedPlainHint
        else -> null
    }

    if (isRtl) {
        AppDirectionalLayout(displayLang) {
            UpcomingHolidayRtlRow(
                translatedName = translatedName,
                holidayName = holiday.name,
                whenPart = " — $translatedWhenLabel",
                timingCluster = rtlTimingCluster,
                bodyStyle = bodyStyle,
                timingStyle = timingStyle,
                onNameClick = { onOpenGuideTopic(openTopic) },
            )
        }
        return
    }

    UpcomingHolidayRtlRow(
        translatedName = translatedName,
        holidayName = holiday.name,
        whenPart = " — $translatedWhenLabel",
        timingCluster = translatedTimingHint,
        bodyStyle = bodyStyle,
        timingStyle = timingStyle,
        onNameClick = { onOpenGuideTopic(openTopic) },
    )
}

@Composable
private fun GuideTopicExplainerDialog(topic: GuideTopic, onDismiss: () -> Unit) {
    val translatedBody = rememberAppTranslatedText(topic.body)
    ParchmentDialog(
        onDismiss = onDismiss,
        title = topic.title,
        confirmButton = { GoldButton(onClick = onDismiss, text = "Done") },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 480.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            HalachicClickableText(
                text = translatedBody,
                style = MaterialTheme.typography.bodyMedium,
                color = TzaddikColors.NavyDeep,
                enableTerms = false,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UpcomingHolidaysBlock(
    holidays: List<UpcomingHoliday>,
    onOpenGuideTopic: (GuideTopic) -> Unit,
    onOpenShabbatGuide: (anchor: String?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(TzaddikColors.NavyMid.copy(alpha = 0.92f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            AppText(
                "Upcoming & seasonal",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.GoldBright,
                enableTerms = false,
            )
            AppText(
                "Shabbat guide ›",
                style = MaterialTheme.typography.labelSmall,
                color = TzaddikColors.GoldBright.copy(alpha = 0.7f),
                enableTerms = false,
                modifier = Modifier.clickable { onOpenShabbatGuide(null) },
            )
        }
        Spacer(Modifier.height(8.dp))
        holidays.forEach { h ->
            val whenLabel = upcomingWhenLabel(h)
            val openTopic = remember(h.name, h.hint) {
                ShabbatGuideData.guideTopicForUpcoming(h.name, h.hint)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp),
            ) {
                UpcomingHolidayLine(
                    holiday = h,
                    whenLabel = whenLabel,
                    openTopic = openTopic,
                    onOpenGuideTopic = onOpenGuideTopic,
                )
                if (h.hint.isNotBlank()) {
                    Spacer(Modifier.height(3.dp))
                    HintWithTermLinks(h.hint, onOpenShabbatGuide)
                }
            }
        }
    }
}

@Composable
private fun HintWithTermLinks(
    hint: String,
    onOpenShabbatGuide: (anchor: String?) -> Unit
) {
    val appTranslation = LocalAppTranslation.current
    val displayLang = appTranslation.displayLanguageCode
    val isRtl = isAppRtlLanguage(displayLang)
    val hintColor = TzaddikColors.ParchTop.copy(alpha = 0.8f)
    val hintStyle = MaterialTheme.typography.bodySmall
    val directionalHintStyle = hintStyle.copy(textAlign = TextAlign.Start)
    val hintModifier = Modifier.fillMaxWidth()

    @Composable
    fun DirectionalHint(content: @Composable () -> Unit) {
        AppDirectionalLayout(displayLang) {
            content()
        }
    }

    val originalSegments = hint.split(", ")
    val hasLinkedTerms = originalSegments.any { seg -> ShabbatGuideData.anchorForLabel(seg) != null }

    if (!hasLinkedTerms) {
        val translatedHint = rememberAppTranslatedText(hint).let { full ->
            if (full == hint && displayLang != "en") {
                RuntimeZmanLocalization.localizeDotJoinedHint(hint, displayLang)
            } else {
                full
            }
        }
        DirectionalHint {
            AppText(
                translatedHint,
                color = hintColor,
                enableTerms = false,
                style = directionalHintStyle,
                textAlign = TextAlign.Start,
                modifier = hintModifier,
            )
        }
        return
    }

    // Translate the full hint string and split into segments matching the originals.
    // Original segments are still used for anchor lookups; translated segments are displayed.
    val translatedHint = rememberAppTranslatedText(hint)
    val translatedSegments = translatedHint.split(", ")
    if (translatedSegments.size != originalSegments.size) {
        DirectionalHint {
            AppText(
                translatedHint,
                color = hintColor,
                enableTerms = false,
                style = directionalHintStyle,
                textAlign = TextAlign.Start,
                modifier = hintModifier,
            )
        }
        return
    }

    val segmentSeparator = if (isRtl) "، " else ", "

    val annotated = remember(translatedHint, isRtl) {
        buildAnnotatedString {
            originalSegments.forEachIndexed { index, seg ->
                val anchor = ShabbatGuideData.anchorForLabel(seg)
                val displaySeg = translatedSegments.getOrElse(index) { seg }
                val segmentText = if (anchor != null) "$displaySeg ›" else displaySeg
                if (anchor != null) {
                    pushStringAnnotation(GUIDE_TERM_TAG, anchor)
                }
                withStyle(SpanStyle(color = hintColor)) {
                    append(segmentText)
                }
                if (anchor != null) {
                    pop()
                }
                if (index < originalSegments.lastIndex) {
                    append(segmentSeparator)
                }
            }
        }
    }
    val density = LocalDensity.current
    val underlineColor = halachicTermUnderlineColor(hintColor)
    val underlineStrokePx = with(density) { 0.75.dp.toPx() }
    val underlineOffsetPx = with(density) { 2.dp.toPx() }
    var textLayout by remember(annotated) { mutableStateOf<TextLayoutResult?>(null) }

    DirectionalHint {
        Text(
            text = annotated,
            style = directionalHintStyle,
            textAlign = TextAlign.Start,
            modifier = hintModifier
                .drawBehind {
                    textLayout?.let { layout ->
                        drawHalachicTermUnderlines(
                            layoutResult = layout,
                            annotated = annotated,
                            underlineColor = underlineColor,
                            strokeWidthPx = underlineStrokePx,
                            underlineOffsetPx = underlineOffsetPx,
                            annotationTag = GUIDE_TERM_TAG,
                        )
                    }
                }
                .pointerInput(annotated) {
                    detectTapGestures { position ->
                        textLayout?.let { layout ->
                            val offset = layout.getOffsetForPosition(position)
                            annotated.getStringAnnotations(GUIDE_TERM_TAG, offset, offset)
                                .firstOrNull()
                                ?.item
                                ?.let { onOpenShabbatGuide(it) }
                        }
                    }
                },
            onTextLayout = { textLayout = it },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CalendarHeader(
    day: DayChecklists?,
    timezoneId: String,
    locationLabel: String?,
    manualCityId: String? = null,
    effectiveNusach: EffectiveNusach,
    upcoming: List<UpcomingHoliday> = emptyList(),
    nowEpochMillis: Long? = null,
    onNusachClick: () -> Unit = {},
    onPeriodClick: () -> Unit = {},
    onOpenShabbatGuide: (anchor: String?) -> Unit = {},
    onOpenGuideTopic: (GuideTopic) -> Unit = {},
    onOpenOmerExplainer: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    listOf(TzaddikColors.NavyDeep, TzaddikColors.NavyMid.copy(alpha = 0.85f))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(18.dp)
    ) {
        CurrentTimeLine(
            timezoneId = timezoneId,
            locationLabel = locationLabel,
            manualCityId = manualCityId,
            nowEpochMillis = nowEpochMillis,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        day?.let { d ->
            d.header.todayOccasionLabel?.let { occasion ->
                val topic = d.header.todayOccasionGuideAnchor
                    ?.let(ShabbatGuideData::topicForAnchor)
                    ?: ShabbatGuideData.topicForUpcomingHoliday(occasion)
                val occasionTemplate = d.header.todayOccasionTemplate
                val occasionArgs = d.header.todayOccasionTemplateArgs
                val localizedOccasion = if (occasionTemplate != null) {
                    rememberAppTranslatedTemplate(occasionTemplate, occasionArgs)
                } else {
                    rememberAppTranslatedText(occasion)
                }
                Column {
                    if (topic != null) {
                        AppText(
                            localizedOccasion,
                            color = TzaddikColors.GoldBright,
                            enableTerms = false,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = TextDecoration.Underline,
                            ),
                            modifier = Modifier.clickable { onOpenGuideTopic(topic) },
                        )
                    } else {
                        AppText(
                            localizedOccasion,
                            color = TzaddikColors.GoldBright,
                            enableTerms = false,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                    }
                    d.header.todayOccasionSubtitle?.let { subtitle ->
                        AppText(
                            subtitle,
                            color = TzaddikColors.ParchTop.copy(alpha = 0.75f),
                            enableTerms = false,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
            }
            d.header.omerDay?.let { omerDay ->
                val appTranslation = LocalAppTranslation.current
                val displayLang = appTranslation.displayLanguageCode
                val isRtlOmer = isAppRtlLanguage(displayLang)
                val omerLabel = remember(omerDay, displayLang, effectiveNusach) {
                    OmerCountText.localizedHeaderLabel(omerDay, displayLang, effectiveNusach)
                }
                val omerModifier = if (isRtlOmer) {
                    Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onOpenOmerExplainer)
                } else {
                    Modifier.clickable(onClick = onOpenOmerExplainer)
                }
                AppDirectionalLayout(displayLang) {
                    Text(
                        text = omerLabel,
                        color = TzaddikColors.ParchTop.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            textDecoration = TextDecoration.Underline,
                            textAlign = TextAlign.Start,
                        ),
                        modifier = omerModifier,
                    )
                }
                Spacer(Modifier.height(6.dp))
            }
            // Civil + Hebrew dates on the same visual tier — same weight, clear step down from clock
            TranslatedCivilDate(
                date = d.date,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.ParchTop,
            )
            TranslatedHebrewDate(
                label = d.header.hebrewDateLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = TzaddikColors.ParchTop.copy(alpha = 0.75f),
            )
            d.header.timeLabel?.let { periodLabel ->
                Spacer(Modifier.height(2.dp))
                AppText(
                    periodLabel,
                    color = TzaddikColors.ParchTop.copy(alpha = 0.9f),
                    enableTerms = false,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline,
                    ),
                    modifier = Modifier.clickable(onClick = onPeriodClick)
                )
            }
            d.header.parshaLabel?.let { parsha ->
                val appTranslation = LocalAppTranslation.current
                val localizedParsha = ParshaData.localizedDisplayName(
                    parsha,
                    appTranslation.displayLanguageCode,
                )
                val parshaLine = rememberAppTranslatedTemplate(
                    "Parsha: {parsha}",
                    mapOf("parsha" to localizedParsha),
                )
                AppText(
                    parshaLine,
                    color = TzaddikColors.ParchTop.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodySmall,
                    enableTerms = false,
                )
            }
            Spacer(Modifier.height(10.dp))

            // Bottom row: Nusach pill + extra status chips (not already in Upcoming & seasonal)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                HeaderNusachPill(label = d.nusachLabel, onClick = onNusachClick)

                headerStatusChips(
                    chips = d.header.statusChips,
                    upcoming = upcoming,
                    todayOccasion = d.header.todayOccasionLabel,
                ).forEach { chip ->
                    val guideAnchor = ShabbatGuideData.anchorForLabel(chip)
                    HeaderStatusPill(
                        label = chip,
                        onClick = guideAnchor?.let { anchor -> { onOpenShabbatGuide(anchor) } },
                    )
                }
            }
        }
    }
    Spacer(Modifier.height(12.dp))
}

/** Status chips already covered by the Upcoming & seasonal block — skip to avoid duplicate labels. */
private fun headerStatusChips(
    chips: List<String>,
    upcoming: List<UpcomingHoliday>,
    todayOccasion: String? = null,
): List<String> {
    val coveredByUpcoming = upcoming
        .filter { it.daysAway == 0 || it.whenLabelOverride != null }
        .map { it.name }
        .toSet()
    return chips.filter { chip ->
        chip !in setOf(
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday",
            "Erev Shabbat",
        )
            && chip !in coveredByUpcoming
            && coveredByUpcoming.none { chip.startsWith(it) }
            && !chipCoveredByTodayOccasion(chip, todayOccasion)
    }
}

private fun chipCoveredByTodayOccasion(chip: String, todayOccasion: String?): Boolean {
    if (todayOccasion == null) return false
    if (todayOccasion.contains(chip, ignoreCase = true)) return true
    if (chip == "Fast day" && todayOccasion.contains("Fast", ignoreCase = true)) return true
    if (chip.startsWith("Chanukah") && todayOccasion.contains("Chanukah", ignoreCase = true)) return true
    if (chip == "Purim" && todayOccasion.contains("Purim", ignoreCase = true)) return true
    if (chip == "Shabbat" && todayOccasion?.equals("Motzei Shabbat", ignoreCase = true) == true) return true
    if (chip == "Shabbat" && todayOccasion.contains("Shabbat", ignoreCase = true)) return true
    if (chip == "Rosh Chodesh" && todayOccasion.contains("Rosh Chodesh", ignoreCase = true)) return true
    return false
}

@Composable
private fun HeaderNusachPill(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(TzaddikColors.GoldBorder.copy(alpha = 0.22f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .rotate(45f)
                .background(TzaddikColors.GoldBright, RoundedCornerShape(1.dp))
        )
        Spacer(Modifier.width(7.dp))
        AppText(
            label,
            color = TzaddikColors.GoldBright,
            style = MaterialTheme.typography.labelMedium,
        )
        Spacer(Modifier.width(6.dp))
        Text(
            "›",
            color = TzaddikColors.GoldBright.copy(alpha = 0.65f),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun HeaderStatusPill(label: String, onClick: (() -> Unit)?) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(TzaddikColors.NavyMid.copy(alpha = 0.7f))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppText(
            label,
            color = TzaddikColors.ParchTop,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun TranslatedCivilDate(
    date: LocalDate,
    style: TextStyle,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    val weekday = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    val line = rememberAppTranslatedTemplate(
        "{weekday}, {month} {day}, {year}",
        mapOf(
            "weekday" to weekday,
            "month" to month,
            "day" to date.dayOfMonth.toString(),
            "year" to date.year.toString(),
        ),
    )
    AppText(
        line,
        color = color,
        style = style,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
        enableTerms = false,
    )
}

@Composable
private fun TranslatedHebrewDate(
    label: String,
    style: TextStyle,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
) {
    val parts = label.trim().split(" ")
    val display = if (parts.size >= 3) {
        val year = parts.last()
        val day = parts.first()
        val monthKey = parts.subList(1, parts.size - 1).joinToString(" ")
        val translatedMonth = rememberAppTranslatedText(monthKey)
        rememberAppTranslatedTemplate(
            "{day} {month} {year}",
            mapOf("day" to day, "month" to translatedMonth, "year" to year),
        )
    } else {
        rememberAppTranslatedText(label)
    }
    AppText(
        display,
        color = color,
        style = style,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Start,
        enableTerms = false,
    )
}
