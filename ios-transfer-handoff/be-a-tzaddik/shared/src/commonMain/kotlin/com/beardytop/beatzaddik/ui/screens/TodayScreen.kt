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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.beardytop.beatzaddik.ui.components.LocalHalachicTermsUsedOnPage
import com.beardytop.beatzaddik.ui.components.LocalOpenShabbatGuide
import com.beardytop.beatzaddik.ui.components.drawHalachicTermUnderlines
import com.beardytop.beatzaddik.ui.components.halachicTermUnderlineColor
import com.beardytop.beatzaddik.domain.DayChecklists
import com.beardytop.beatzaddik.domain.HolyDayPhoneNotice
import com.beardytop.beatzaddik.domain.ChecklistEngine
import com.beardytop.beatzaddik.domain.Gender
import com.beardytop.beatzaddik.domain.ItemZmanAvailability
import com.beardytop.beatzaddik.domain.MealCategory
import com.beardytop.beatzaddik.domain.ResolvedChecklistItem
import com.beardytop.beatzaddik.domain.TimeOfDay
import com.beardytop.beatzaddik.domain.UserProfile
import com.beardytop.beatzaddik.domain.UpcomingHoliday
import com.beardytop.beatzaddik.domain.ZmanimFormatter
import com.beardytop.beatzaddik.ui.ChecklistPeriodScroll
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
) {
    val day by viewModel.dayChecklists.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val progress by viewModel.requiredProgress.collectAsState()
    val kashrut by viewModel.kashrutWait.collectAsState()
    val upcoming by viewModel.upcomingHolidays.collectAsState()
    val nowMillis by produceState(Clock.System.now().toEpochMilliseconds()) {
        while (true) {
            value = Clock.System.now().toEpochMilliseconds()
            delay(1000)
        }
    }
    var customText by remember { mutableStateOf("") }
    var infoItem by remember { mutableStateOf<ResolvedChecklistItem?>(null) }
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
    val usedTermsOnPage = remember(pageKey) { mutableSetOf<String>() }

    CompositionLocalProvider(LocalHalachicTermsUsedOnPage provides usedTermsOnPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .onGloballyPositioned { scrollRootY = it.positionInRoot().y }
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        ParchmentContentCard {
            CalendarHeader(
                day = day,
                timezoneId = profile.timezoneId,
                locationLabel = profile.locationLabel,
                onNusachClick = onOpenSettings,
                onPeriodClick = { day?.let { scrollToChecklistPeriod(it.activePeriod) } },
                onOpenShabbatGuide = openShabbatGuide,
            )
            if (upcoming.isNotEmpty()) {
                UpcomingHolidaysBlock(upcoming, openShabbatGuide)
            }
            kashrut?.let { wait ->
                val isDone = nowMillis >= wait.endsAtEpochMillis
                if (isDone) {
                    val allowedFood = when (wait.category) {
                        MealCategory.MEAT -> "dairy"
                        MealCategory.DAIRY -> "meat"
                    }
                    val endedAt = ZmanimFormatter.formatTimeWithDate(
                        wait.endsAtEpochMillis,
                        profile.timezoneId
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
                            "You can eat $allowedFood as of ${endedAt ?: "now"}",
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
                d.holyDayPhoneNotice?.let { notice ->
                    HolyDayPhoneNoticeCard(notice)
                    Spacer(Modifier.height(12.dp))
                    return@let
                }
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
                ClickablePeriodLabel(
                    label = d.activePeriodLabel,
                    onClick = { scrollToChecklistPeriod(d.activePeriod) }
                )
                d.activePeriodHint?.let {
                    AppText(
                        it,
                        enableTerms = false,
                        style = MaterialTheme.typography.bodySmall,
                        color = TzaddikColors.TextMuted,
                    )
                }
                ChecklistSections(
                    items = d.items,
                    viewModel = viewModel,
                    holyFlash = holyFlash,
                    onInfo = { infoItem = it },
                    activePeriod = d.activePeriod,
                    profile = profile,
                    expandPeriodRequest = expandPeriodRequest,
                    onExpandPeriodConsumed = { expandPeriodRequest = null },
                    scrollRootY = scrollRootY,
                    onPeriodAnchorPosition = { period, y ->
                        periodScrollOffsets = periodScrollOffsets + (period to y)
                    }
                )
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
    }
}

@Composable
private fun HolyDayPhoneNoticeCard(notice: HolyDayPhoneNotice) {
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
            text = notice.message,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.ParchTop,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        AppText(
            notice.footer,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = TzaddikColors.GoldBorder,
            textAlign = TextAlign.Center
        )
    }
}

private fun sectionSortKey(section: String): Int {
    val base = section.substringBefore(" (")
    return ChecklistEngine.sectionOrder.indexOf(base).takeIf { it >= 0 }
        ?: ChecklistEngine.sectionOrder.indexOf(section).takeIf { it >= 0 }
        ?: 200
}

/** Sections that start collapsed — mostly one-time or infrequent setup. */
private val defaultCollapsedSectionNames = setOf(
    "Ongoing mitzvot",
    "Important Lifestyle Mitzvot",
    // "Married women's mitzvot" is intentionally expanded by default when present.
    "Prepare for Shabbat",
    "Seasonal",
    "Pesach prep",
    "Sefirat HaOmer",
    "Chanukah",
    "Purim"
)

private fun defaultCollapsedSectionKeys(
    sectionPrefix: String,
    activePeriod: TimeOfDay,
    profile: UserProfile
): Set<String> {
    val names = defaultCollapsedSectionNames.toMutableSet()
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
    expandPeriodRequest: TimeOfDay? = null,
    onExpandPeriodConsumed: () -> Unit = {},
    scrollRootY: Float = 0f,
    onPeriodAnchorPosition: (TimeOfDay, Int) -> Unit = { _, _ -> }
) {
    if (items.isEmpty()) {
        AppText(
            "No mitzvot on your checklist for today.",
            modifier = Modifier.padding(vertical = 12.dp),
            color = TzaddikColors.TextMuted
        )
        return
    }
    var collapsedSectionKeys by rememberSaveable(sectionPrefix, activePeriod, profile.married, profile.gender) {
        mutableStateOf(defaultCollapsedSectionKeys(sectionPrefix, activePeriod, profile).toList())
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

    val collapsedSet = remember(collapsedSectionKeys) { collapsedSectionKeys.toSet() }
    items.groupBy { it.sectionLabel }
        .toList()
        .sortedBy { (section, _) -> sectionSortKey(section) }
        .forEach { (section, sectionItems) ->
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
                    val yInScroll = (coords.positionInRoot().y - scrollRootY).toInt()
                    onPeriodAnchorPosition(periodAnchor, yInScroll)
                }
            } else {
                Modifier
            }
            if (collapsibleSections) {
                CollapsibleChecklistSectionHeader(
                    title = section,
                    expanded = expanded,
                    itemCount = sectionItems.size,
                    onToggle = {
                        collapsedSectionKeys = if (expanded) {
                            collapsedSectionKeys + sectionKey
                        } else {
                            collapsedSectionKeys - sectionKey
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
                        HolyLightChecklistRow(
                            item = item,
                            onCheckedChange = { checked ->
                                if (item.zmanAvailability == ItemZmanAvailability.ACTIVE) {
                                    when {
                                        item.def.monthlyMitzvah ->
                                            viewModel.setMonthlyChecked(item.def.id, checked)
                                        item.def.weeklyMitzvah ->
                                            viewModel.setWeeklyChecked(item.def.id, checked)
                                        else ->
                                            viewModel.setChecked(item.def.id, checked, item.def.persistChecked)
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

private fun upcomingWhenLabel(holiday: UpcomingHoliday): String = when {
    holiday.daysAway == 0 && holiday.beginsTonightWhenImminent -> "Tonight"
    holiday.daysAway == 0 -> "today"
    holiday.daysAway == 1 -> "tomorrow"
    else -> "in ${holiday.daysAway} days"
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UpcomingHolidaysBlock(
    holidays: List<UpcomingHoliday>,
    onOpenShabbatGuide: (anchor: String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(TzaddikColors.NavyMid.copy(alpha = 0.92f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AppText(
                "Upcoming & seasonal",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.GoldBright,
                modifier = Modifier.weight(1f)
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
            // Determine anchor: map holiday name to a guide section
            val anchor = ShabbatGuideData.anchorForLabel(h.name)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .then(
                        if (anchor != null)
                            Modifier.clickable { onOpenShabbatGuide(anchor) }
                        else Modifier
                    )
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    AppText(
                        "${h.name} — $whenLabel",
                        color = TzaddikColors.ParchTop,
                        enableTerms = false,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (h.hint.isNotBlank()) {
                        Spacer(Modifier.height(3.dp))
                        // Render hint words — tap any known term to open its guide entry
                        HintWithTermLinks(h.hint, onOpenShabbatGuide)
                    }
                }
                if (anchor != null) {
                    Text(
                        "›",
                        color = TzaddikColors.GoldBright.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 6.dp)
                    )
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
    val hintColor = TzaddikColors.ParchTop.copy(alpha = 0.8f)
    val segments = hint.split(", ")
    val hasLinkedTerms = segments.any { seg -> ShabbatGuideData.anchorForLabel(seg) != null }

    if (!hasLinkedTerms) {
        AppText(
            hint,
            color = hintColor,
            enableTerms = false,
            style = MaterialTheme.typography.bodySmall,
        )
        return
    }

    val annotated = remember(hint) {
        buildAnnotatedString {
            segments.forEachIndexed { index, seg ->
                val anchor = ShabbatGuideData.anchorForLabel(seg)
                val segmentText = if (anchor != null) "$seg ›" else seg
                if (anchor != null) {
                    pushStringAnnotation(GUIDE_TERM_TAG, anchor)
                }
                withStyle(SpanStyle(color = hintColor)) {
                    append(segmentText)
                }
                if (anchor != null) {
                    pop()
                }
                if (index < segments.lastIndex) {
                    append(", ")
                }
            }
        }
    }
    val density = LocalDensity.current
    val underlineColor = halachicTermUnderlineColor(hintColor)
    val underlineStrokePx = with(density) { 0.75.dp.toPx() }
    val underlineOffsetPx = with(density) { 2.dp.toPx() }
    var textLayout by remember(annotated) { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotated,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CalendarHeader(
    day: DayChecklists?,
    timezoneId: String,
    locationLabel: String?,
    onNusachClick: () -> Unit = {},
    onPeriodClick: () -> Unit = {},
    onOpenShabbatGuide: (anchor: String?) -> Unit = {},
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
            modifier = Modifier.padding(bottom = 6.dp)
        )
        day?.let { d ->
            // Civil + Hebrew dates on the same visual tier — same weight, clear step down from clock
            AppText(
                d.header.civilDateLabel,
                color = TzaddikColors.ParchTop,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                d.header.hebrewDateLabel,
                color = TzaddikColors.ParchTop.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodyMedium
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
            d.header.parshaLabel?.let {
                AppText(
                    "Parsha: $it",
                    color = TzaddikColors.ParchTop.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(10.dp))

            // Bottom row: Nusach pill + meaningful status chips side by side
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Nusach pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(TzaddikColors.GoldBorder.copy(alpha = 0.22f))
                        .clickable(onClick = onNusachClick)
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .rotate(45f)
                            .background(TzaddikColors.GoldBright, RoundedCornerShape(1.dp))
                    )
                    Spacer(Modifier.width(7.dp))
                    AppText(
                        d.nusachLabel,
                        color = TzaddikColors.GoldBright,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "›",
                        color = TzaddikColors.GoldBright.copy(alpha = 0.65f),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                // Only show meaningful status chips — skip bare day-of-week names
                val meaningfulChips = d.header.statusChips.filter { chip ->
                    chip !in setOf(
                        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday",
                        "Erev Shabbat", // shown as "Shabbat — Tonight" in upcoming list
                    )
                }
                meaningfulChips.forEach { chip ->
                    val guideAnchor = ShabbatGuideData.anchorForLabel(chip)
                    AssistChip(
                        onClick = { guideAnchor?.let { onOpenShabbatGuide(it) } },
                        label = {
                            AppText(
                                chip,
                                color = TzaddikColors.ParchTop,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = TzaddikColors.NavyMid.copy(alpha = 0.7f)
                        ),
                        border = null
                    )
                }
            }
        }
    }
    Spacer(Modifier.height(12.dp))
}
