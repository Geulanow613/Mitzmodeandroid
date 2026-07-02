package com.beardytop.beatzaddik.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.beatzaddik.domain.EffectiveNusach
import com.beardytop.beatzaddik.domain.OmerCountText
import com.beardytop.beatzaddik.domain.ParshaData
import com.beardytop.beatzaddik.domain.ChecklistZmanEvaluator
import com.beardytop.beatzaddik.domain.ItemZmanAvailability
import com.beardytop.beatzaddik.domain.ResolvedChecklistItem
import com.beardytop.beatzaddik.domain.ZmanAvailabilityLive
import com.beardytop.beatzaddik.domain.ZmanCountdownFormatter
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.ui.translation.LocalAppTranslation
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedText
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedTemplate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@Composable
fun HolyLightChecklistRow(
    item: ResolvedChecklistItem,
    timezoneId: String,
    effectiveNusach: EffectiveNusach,
    onCheckedChange: (Boolean) -> Unit,
    onInfoClick: () -> Unit,
    onHolyFlash: () -> Unit = {},
    onRemove: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val tracksZman = ChecklistZmanEvaluator.appliesTo(item.def.id)
    var nowMillis by remember { mutableLongStateOf(Clock.System.now().toEpochMilliseconds()) }
    LaunchedEffect(tracksZman, item.zmanWindowStartMillis, item.zmanWindowEndMillis, item.zmanAvailability) {
        if (!tracksZman) return@LaunchedEffect
        while (true) {
            nowMillis = Clock.System.now().toEpochMilliseconds()
            val until = item.zmanWindowStartMillis?.let { it - nowMillis }
            val delayMs = when {
                until != null && until > 0 && until < 5 * 60_000 -> 5_000L
                until != null && until > 0 && until < 60 * 60_000 -> 15_000L
                else -> 30_000L
            }
            delay(delayMs)
        }
    }
    val liveAvailability = if (tracksZman) {
        remember(item.zmanAvailability, item.zmanWindowStartMillis, item.zmanWindowEndMillis, nowMillis) {
            ZmanAvailabilityLive.derive(
                availability = item.zmanAvailability,
                windowStartMillis = item.zmanWindowStartMillis,
                windowEndMillis = item.zmanWindowEndMillis,
                nowMillis = nowMillis,
            )
        }
    } else {
        item.zmanAvailability
    }
    val zmanMuted = liveAvailability != ItemZmanAvailability.ACTIVE
    val isZmanTracked = tracksZman && zmanMuted
    val canCheck = !zmanMuted

    if (isZmanTracked) {
        CollapsedZmanChecklistRow(
            item = item,
            liveAvailability = liveAvailability,
            nowMillis = nowMillis,
            timezoneId = timezoneId,
            effectiveNusach = effectiveNusach,
            onCheckedChange = onCheckedChange,
            onInfoClick = onInfoClick,
            onHolyFlash = onHolyFlash,
            onRemove = onRemove,
            modifier = modifier
        )
    } else {
        ActiveChecklistRow(
            item = item,
            effectiveNusach = effectiveNusach,
            onCheckedChange = onCheckedChange,
            onInfoClick = onInfoClick,
            onHolyFlash = onHolyFlash,
            onRemove = onRemove,
            zmanMuted = zmanMuted,
            canCheck = canCheck,
            modifier = modifier
        )
    }
}

@Composable
private fun CollapsedZmanChecklistRow(
    item: ResolvedChecklistItem,
    liveAvailability: ItemZmanAvailability,
    nowMillis: Long,
    timezoneId: String,
    effectiveNusach: EffectiveNusach,
    onCheckedChange: (Boolean) -> Unit,
    onInfoClick: () -> Unit,
    onHolyFlash: () -> Unit,
    onRemove: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    var expanded by remember(item.def.id, item.zmanAvailability) { mutableStateOf(false) }
    val languageCode = LocalAppTranslation.current.displayLanguageCode

    val collapsedTemplate = item.zmanCollapsedTemplate
        ?: ZmanCountdownFormatter.unavailableCollapsedSummaryTemplate(
            availability = liveAvailability,
            windowStartMillis = item.zmanWindowStartMillis,
            nowMillis = nowMillis,
            atLabel = item.zmanAvailableAtLabel,
            timezoneId = timezoneId,
            languageCode = languageCode,
        )?.first
    val collapsedArgs = item.zmanCollapsedArgs.ifEmpty {
        ZmanCountdownFormatter.unavailableCollapsedSummaryTemplate(
            availability = liveAvailability,
            windowStartMillis = item.zmanWindowStartMillis,
            nowMillis = nowMillis,
            atLabel = item.zmanAvailableAtLabel,
            timezoneId = timezoneId,
            languageCode = languageCode,
        )?.second.orEmpty()
    }
    val collapsedSummary = if (collapsedTemplate != null) {
        rememberAppTranslatedTemplate(collapsedTemplate, collapsedArgs)
    } else {
        ZmanCountdownFormatter.unavailableCollapsedSummary(
            availability = liveAvailability,
            windowStartMillis = item.zmanWindowStartMillis,
            nowMillis = nowMillis,
            atLabel = item.zmanAvailableAtLabel,
            timezoneId = timezoneId,
            languageCode = languageCode,
        )?.let { rememberAppTranslatedText(it) }
    }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 3.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.72f)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            TzaddikColors.ParchTop.copy(alpha = 0.55f),
                            TzaddikColors.ParchMid.copy(alpha = 0.4f)
                        )
                    )
                )
                .border(
                    width = 0.8.dp,
                    color = TzaddikColors.GoldBorder.copy(alpha = 0.22f),
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { expanded = !expanded }
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    val displayTitle = rememberTranslatedChecklistTitle(
                        rawTitle = item.displayTitle,
                        translationKey = item.titleTranslationKey,
                        effectiveNusach = effectiveNusach,
                    )
                    AppText(
                        text = displayTitle,
                        enableTerms = false,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = TzaddikColors.TextMuted.copy(alpha = 0.85f),
                        maxLines = 2
                    )
                    collapsedSummary?.let { summary ->
                        AppText(
                            text = summary,
                            style = MaterialTheme.typography.labelSmall,
                            color = when (liveAvailability) {
                                ItemZmanAvailability.UPCOMING -> TzaddikColors.NavyMid.copy(alpha = 0.9f)
                                else -> TzaddikColors.TextMuted.copy(alpha = 0.9f)
                            }
                        )
                    }
                }
                IconButton(onClick = onInfoClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Info",
                        tint = TzaddikColors.GoldBorder.copy(alpha = 0.65f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = TzaddikColors.TextMuted.copy(alpha = 0.8f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            ExpandedZmanHintPanel(
                item = item,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp),
            )
        }
    }
}

@Composable
private fun ExpandedZmanHintPanel(
    item: ResolvedChecklistItem,
    modifier: Modifier = Modifier,
) {
    val hintTemplate = item.zmanHintTemplate
    val hintPlain = item.zmanHint
    if (hintTemplate == null && hintPlain == null) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(TzaddikColors.ParchTop.copy(alpha = 0.45f))
            .border(
                width = 0.6.dp,
                color = TzaddikColors.GoldBorder.copy(alpha = 0.18f),
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Column {
            val displayHint = if (hintTemplate != null) {
                rememberAppTranslatedTemplate(hintTemplate, item.zmanHintArgs)
            } else {
                rememberAppTranslatedText(hintPlain!!)
            }
            AppText(
                text = displayHint,
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.TextMuted,
            )
            val makeupTemplate = item.zmanMakeupTemplate
            val makeupPlain = item.zmanMakeupNote
            if (item.zmanAvailability == ItemZmanAvailability.EXPIRED &&
                (makeupTemplate != null || makeupPlain != null)
            ) {
                val displayNote = if (makeupTemplate != null) {
                    rememberAppTranslatedTemplate(makeupTemplate, item.zmanMakeupArgs)
                } else {
                    rememberAppTranslatedText(makeupPlain!!)
                }
                AppText(
                    text = displayNote,
                    modifier = Modifier.padding(top = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = TzaddikColors.TextMuted.copy(alpha = 0.85f),
                )
            }
        }
    }
}

@Composable
private fun ActiveChecklistRow(
    item: ResolvedChecklistItem,
    effectiveNusach: EffectiveNusach,
    onCheckedChange: (Boolean) -> Unit,
    onInfoClick: () -> Unit,
    onHolyFlash: () -> Unit,
    onRemove: (() -> Unit)?,
    zmanMuted: Boolean,
    canCheck: Boolean = !zmanMuted,
    modifier: Modifier = Modifier
) {
    val rowAlpha = remember { Animatable(1f) }
    val checkScale = remember { Animatable(1f) }
    val rowDim = if (zmanMuted) 0.58f else 1f

    LaunchedEffect(item.checked) {
        if (item.checked) {
            onHolyFlash()
            launch {
                checkScale.snapTo(0.82f)
                checkScale.animateTo(1f, tween(280))
            }
        } else {
            launch {
                rowAlpha.snapTo(0.55f)
                rowAlpha.animateTo(1f, tween(220))
            }
        }
    }

    val rowBg = if (item.checked) {
        Brush.horizontalGradient(
            listOf(
                TzaddikColors.ParchMid.copy(alpha = 0.5f),
                TzaddikColors.ParchBase.copy(alpha = 0.35f)
            )
        )
    } else {
        Brush.horizontalGradient(
            listOf(TzaddikColors.ParchTop.copy(alpha = 0.7f), TzaddikColors.ParchMid.copy(alpha = 0.5f))
        )
    }

    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .alpha(rowAlpha.value * rowDim)
            .clip(shape)
            .background(rowBg)
            .border(
                width = if (item.checked) 1.dp else 0.7.dp,
                color = if (item.checked) TzaddikColors.GoldBorder.copy(alpha = 0.55f)
                else TzaddikColors.GoldBorder.copy(alpha = 0.22f),
                shape = shape
            )
            .clickable { if (canCheck) onCheckedChange(!item.checked) }
    ) {
        // Checked state: left-edge gold accent bar
        if (item.checked) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight()
                    .width(3.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(TzaddikColors.GoldBright, TzaddikColors.GoldBorder)
                        )
                    )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.checked,
                onCheckedChange = { if (canCheck) onCheckedChange(it) },
                enabled = canCheck,
                modifier = Modifier.graphicsLayer {
                    scaleX = checkScale.value
                    scaleY = checkScale.value
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = TzaddikColors.NavyMid,
                    uncheckedColor = TzaddikColors.GoldBorder.copy(alpha = 0.55f),
                    checkmarkColor = Color(0xFFFFF6D8),
                    disabledCheckedColor = TzaddikColors.NavyMid.copy(alpha = 0.45f),
                    disabledUncheckedColor = TzaddikColors.GoldBorder.copy(alpha = 0.3f)
                )
            )
            Column(modifier = Modifier.weight(1f).padding(start = 2.dp)) {
                ChecklistItemTitle(
                    rawTitle = item.displayTitle,
                    translationKey = item.titleTranslationKey,
                    effectiveNusach = effectiveNusach,
                    checked = item.checked,
                    zmanMuted = zmanMuted,
                )
                item.zmanHint?.let { hint ->
                    val displayHint = if (item.zmanHintTemplate != null) {
                        rememberAppTranslatedTemplate(item.zmanHintTemplate, item.zmanHintArgs)
                    } else {
                        rememberAppTranslatedText(hint)
                    }
                    Text(
                        text = displayHint,
                        style = MaterialTheme.typography.labelSmall,
                        color = TzaddikColors.TextMuted,
                    )
                }
                val makeupTemplate = item.zmanMakeupTemplate
                val makeupPlain = item.zmanMakeupNote
                if (zmanMuted && item.zmanAvailability == ItemZmanAvailability.EXPIRED &&
                    (makeupTemplate != null || makeupPlain != null)
                ) {
                    val displayNote = if (makeupTemplate != null) {
                        rememberAppTranslatedTemplate(makeupTemplate, item.zmanMakeupArgs)
                    } else {
                        rememberAppTranslatedText(makeupPlain!!)
                    }
                    Text(
                        text = displayNote,
                        style = MaterialTheme.typography.labelSmall,
                        color = TzaddikColors.TextMuted.copy(alpha = 0.85f),
                    )
                }
            }
            if (onRemove != null) {
                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Close, contentDescription = "Remove",
                        tint = TzaddikColors.TextMuted, modifier = Modifier.size(16.dp)
                    )
                }
            }
            // Info button — gold circle badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(androidx.compose.foundation.shape.CircleShape)
                    .background(TzaddikColors.GoldBorder.copy(alpha = 0.12f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                    ) { onInfoClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Info, contentDescription = "Info",
                    tint = TzaddikColors.GoldBorder,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Translates [translationKey] (the static bundle key, e.g. "Modeh Ani (Thank G-d upon waking)")
 * and then re-appends any dynamic suffix that is part of [rawTitle] but not the key — such as
 * " · Ashkenaz" (nusach tag) or " — Parshat Pinchas" (weekly parsha name).
 */
@Composable
private fun rememberTranslatedChecklistTitle(
    rawTitle: String,
    translationKey: String,
    effectiveNusach: EffectiveNusach,
): String {
    val appTranslation = LocalAppTranslation.current
    val bundledKey = rememberAppTranslatedText(translationKey)
    val omerDay = OmerCountText.dayFromCountTitle(translationKey)
    val translatedKey = if (omerDay != null && appTranslation.languageCode in setOf("he", "yi")) {
        OmerCountText.localizedBuildTitle(omerDay, appTranslation.languageCode, effectiveNusach)
    } else {
        bundledKey
    }
    val dynamicSuffix = if (rawTitle.startsWith(translationKey)) {
        rawTitle.substring(translationKey.length)
    } else {
        ""
    }
    return translatedKey + rememberTranslatedChecklistTitleSuffix(dynamicSuffix)
}

@Composable
private fun rememberTranslatedChecklistTitleSuffix(dynamicSuffix: String): String {
    if (dynamicSuffix.isEmpty()) return ""
    val languageCode = LocalAppTranslation.current.displayLanguageCode
    val parshaMarker = " — Parshat "
    val parshaIdx = dynamicSuffix.indexOf(parshaMarker)
    if (parshaIdx >= 0) {
        val before = dynamicSuffix.substring(0, parshaIdx)
        val parshaName = dynamicSuffix.substring(parshaIdx + parshaMarker.length)
        val localizedParsha = ParshaData.localizedDisplayName(parshaName, languageCode)
        val parshaPart = rememberAppTranslatedTemplate(
            " — Parshat {parsha}",
            mapOf("parsha" to localizedParsha),
        )
        return rememberTranslatedChecklistTitleSuffix(before) + parshaPart
    }
    return when {
        dynamicSuffix.startsWith(" · ") -> {
            val tag = dynamicSuffix.removePrefix(" · ")
            val translatedTag = rememberAppTranslatedText(tag)
            " · $translatedTag"
        }
        else -> dynamicSuffix
    }
}

/**
 * Renders a translated checklist title, split at the first " (" for the standard two-line display.
 */
@Composable
private fun ChecklistItemTitle(
    rawTitle: String,
    translationKey: String,
    effectiveNusach: EffectiveNusach,
    checked: Boolean,
    zmanMuted: Boolean,
) {
    val displayTitle = rememberTranslatedChecklistTitle(rawTitle, translationKey, effectiveNusach)

    val mainColor = when {
        zmanMuted -> TzaddikColors.TextMuted.copy(alpha = 0.78f)
        checked -> TzaddikColors.TextMuted
        else -> TzaddikColors.TextBrown
    }

    val parenIdx = displayTitle.indexOf(" (")
    if (!checked && parenIdx > 0) {
        val main = displayTitle.substring(0, parenIdx)
        val paren = displayTitle.substring(parenIdx + 1)
        Text(
            text = main,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = mainColor,
        )
        Text(
            text = paren,
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted.copy(alpha = 0.75f),
        )
    } else {
        Text(
            text = displayTitle,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                fontWeight = if (!checked && !zmanMuted) FontWeight.SemiBold else FontWeight.Normal,
            ),
            color = mainColor,
        )
    }
}
