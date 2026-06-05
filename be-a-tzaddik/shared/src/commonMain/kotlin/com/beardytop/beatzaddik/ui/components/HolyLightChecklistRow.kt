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
import com.beardytop.beatzaddik.domain.ChecklistZmanEvaluator
import com.beardytop.beatzaddik.domain.ItemZmanAvailability
import com.beardytop.beatzaddik.domain.ResolvedChecklistItem
import com.beardytop.beatzaddik.domain.ZmanCountdownFormatter
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

@Composable
fun HolyLightChecklistRow(
    item: ResolvedChecklistItem,
    onCheckedChange: (Boolean) -> Unit,
    onInfoClick: () -> Unit,
    onHolyFlash: () -> Unit = {},
    onRemove: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val zmanMuted = item.zmanAvailability != ItemZmanAvailability.ACTIVE
    val isZmanTracked = ChecklistZmanEvaluator.appliesTo(item.def.id) && zmanMuted
    val canCheck = !zmanMuted

    if (isZmanTracked) {
        CollapsedZmanChecklistRow(
            item = item,
            onCheckedChange = onCheckedChange,
            onInfoClick = onInfoClick,
            onHolyFlash = onHolyFlash,
            onRemove = onRemove,
            modifier = modifier
        )
    } else {
        ActiveChecklistRow(
            item = item,
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
    onCheckedChange: (Boolean) -> Unit,
    onInfoClick: () -> Unit,
    onHolyFlash: () -> Unit,
    onRemove: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    var expanded by remember(item.def.id, item.zmanAvailability) { mutableStateOf(false) }
    var nowMillis by remember { mutableLongStateOf(Clock.System.now().toEpochMilliseconds()) }

    LaunchedEffect(item.zmanWindowStartMillis, item.zmanAvailability) {
        while (true) {
            nowMillis = Clock.System.now().toEpochMilliseconds()
            delay(30_000)
        }
    }

    val collapsedSummary = ZmanCountdownFormatter.unavailableCollapsedSummary(
        availability = item.zmanAvailability,
        windowStartMillis = item.zmanWindowStartMillis,
        nowMillis = nowMillis,
        atLabel = item.zmanAvailableAtLabel
    )

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
                    AppText(
                        text = item.displayTitle,
                        enableTerms = false,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = TzaddikColors.TextMuted.copy(alpha = 0.85f),
                        maxLines = 2
                    )
                    collapsedSummary?.let { summary ->
                        AppText(
                            text = summary,
                            style = MaterialTheme.typography.labelSmall,
                            color = when (item.zmanAvailability) {
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
            ActiveChecklistRow(
                item = item,
                onCheckedChange = onCheckedChange,
                onInfoClick = onInfoClick,
                onHolyFlash = onHolyFlash,
                onRemove = onRemove,
                zmanMuted = true,
                canCheck = false,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ActiveChecklistRow(
    item: ResolvedChecklistItem,
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
                // Split title: main name on line 1, parenthetical translation on line 2
                val title = item.displayTitle
                val parenStart = title.indexOf(" (")
                if (!item.checked && parenStart > 0) {
                    val main = title.substring(0, parenStart)
                    val paren = title.substring(parenStart + 1) // trim the leading space
                    AppText(
                        text = main,
                        enableTerms = false,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = if (zmanMuted) TzaddikColors.TextMuted.copy(alpha = 0.78f) else TzaddikColors.TextBrown
                    )
                    AppText(
                        text = paren,
                        enableTerms = false,
                        style = MaterialTheme.typography.bodySmall,
                        color = TzaddikColors.TextMuted.copy(alpha = 0.75f)
                    )
                } else {
                    AppText(
                        text = title,
                        enableTerms = false,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = if (!item.checked && !zmanMuted) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        color = when {
                            zmanMuted -> TzaddikColors.TextMuted.copy(alpha = 0.78f)
                            item.checked -> TzaddikColors.TextMuted
                            else -> TzaddikColors.TextBrown
                        }
                    )
                }
                item.zmanHint?.let { hint ->
                    AppText(
                        text = hint,
                        enableTerms = false,
                        style = MaterialTheme.typography.labelSmall,
                        color = TzaddikColors.TextMuted,
                    )
                }
                item.zmanMakeupNote?.takeIf { zmanMuted && item.zmanAvailability == ItemZmanAvailability.EXPIRED }?.let { note ->
                    AppText(
                        text = note,
                        enableTerms = false,
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
