package com.beardytop.beatzaddik.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.beatzaddik.domain.ChecklistDebugOverride
import com.beardytop.beatzaddik.domain.ChecklistDebugPhase
import com.beardytop.beatzaddik.domain.ChecklistDebugScenario
import com.beardytop.beatzaddik.domain.ChecklistDebugScenarios
import com.beardytop.beatzaddik.domain.ChecklistDebugTimeSlot
import com.beardytop.beatzaddik.domain.ZmanimFormatter
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.viewmodel.AppViewModel

private val DebugBannerBg = Color(0xFFFFF3CD)
private val DebugBannerBorder = Color(0xFFFFC107)
private val DebugAccent = Color(0xFFB45309)

/**
 * Temporary dev tool — simulates calendar date/time for checklist preview.
 * Remove before production release.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChecklistDebugMenu(
    viewModel: AppViewModel,
    activeOverride: ChecklistDebugOverride?,
    timezoneId: String,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val resolving by viewModel.checklistDebugResolving.collectAsState()

    CompositionLocalProvider(LocalHalachicTermsUsedOnPage provides null) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(DebugBannerBg, RoundedCornerShape(10.dp))
            .border(1.dp, DebugBannerBorder, RoundedCornerShape(10.dp))
            .padding(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "DEBUG — Calendar simulator",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                    ),
                    color = DebugAccent,
                )
                activeOverride?.let { o ->
                    val timeLabel = ZmanimFormatter.formatTime(o.epochMillis, timezoneId)
                    Text(
                        "Simulating: ${o.label} · ${ZmanimFormatter.formatCivilDate(o.simulatedDate)} · ${o.timeSlot.label}" +
                            (timeLabel?.let { " ($it)" } ?: ""),
                        style = MaterialTheme.typography.labelSmall,
                        color = TzaddikColors.NavyDeep,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                } ?: Text(
                    if (resolving) "Finding calendar date…" else "Tap to pick a holiday, fast, or season (erev / day of)",
                    style = MaterialTheme.typography.labelSmall,
                    color = TzaddikColors.TextMuted,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (activeOverride != null) {
                    Text(
                        "Clear",
                        style = MaterialTheme.typography.labelLarge,
                        color = DebugAccent,
                        modifier = Modifier
                            .clickable { viewModel.clearChecklistDebug() }
                            .padding(end = 8.dp),
                    )
                }
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = DebugAccent,
                )
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    "Time of simulated day",
                    style = MaterialTheme.typography.labelMedium,
                    color = TzaddikColors.NavyMid,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(bottom = 10.dp),
                ) {
                    ChecklistDebugTimeSlot.entries.forEach { slot ->
                        val selected = activeOverride?.timeSlot == slot
                        AssistChip(
                            onClick = {
                                if (activeOverride != null) {
                                    viewModel.setChecklistDebugTimeSlot(slot)
                                }
                            },
                            label = { Text(slot.label, color = TzaddikColors.NavyDeep) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (selected) {
                                    TzaddikColors.GoldBright.copy(alpha = 0.45f)
                                } else {
                                    Color.White.copy(alpha = 0.7f)
                                },
                            ),
                        )
                    }
                }

                ChecklistDebugScenarios.groups.forEach { group ->
                    Text(
                        group,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = TzaddikColors.NavyMid,
                        modifier = Modifier.padding(top = 6.dp, bottom = 4.dp),
                    )
                    val inGroup = ChecklistDebugScenarios.all.filter { it.group == group }
                    val names = inGroup.map { it.label }.distinct()
                    names.forEach { name ->
                        val erev = inGroup.firstOrNull { it.label == name && it.phase == ChecklistDebugPhase.EREV }
                        val dayOf = inGroup.firstOrNull { it.label == name && it.phase == ChecklistDebugPhase.DAY_OF }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                name,
                                style = MaterialTheme.typography.bodySmall,
                                color = TzaddikColors.TextBrown,
                                modifier = Modifier.weight(1f),
                            )
                            erev?.let { scenario ->
                                DebugPhaseChip(
                                    label = "Erev",
                                    selected = activeOverride?.scenarioId == scenario.id,
                                    enabled = !resolving,
                                    onClick = {
                                        viewModel.applyChecklistDebugScenario(
                                            scenario,
                                            activeOverride?.timeSlot ?: ChecklistDebugTimeSlot.MORNING,
                                        )
                                    },
                                )
                            }
                            dayOf?.let { scenario ->
                                DebugPhaseChip(
                                    label = "Day",
                                    selected = activeOverride?.scenarioId == scenario.id,
                                    enabled = !resolving,
                                    onClick = {
                                        viewModel.applyChecklistDebugScenario(
                                            scenario,
                                            activeOverride?.timeSlot ?: ChecklistDebugTimeSlot.MORNING,
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    }
}

@Composable
private fun DebugPhaseChip(
    label: String,
    selected: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    AssistChip(
        onClick = onClick,
        enabled = enabled,
        label = { Text(label, color = TzaddikColors.NavyDeep) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) {
                TzaddikColors.NavyMid.copy(alpha = 0.2f)
            } else {
                Color.White.copy(alpha = 0.85f)
            },
        ),
    )
}
