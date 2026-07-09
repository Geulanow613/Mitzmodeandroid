package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.style.TextOverflow
import com.beardytop.beatzaddik.ui.theme.TextScaleDefaults
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

private val DialogShape = RoundedCornerShape(22.dp)

/**
 * Ornamental centered divider: a gold diamond flanked by tapering lines.
 * Adds a refined, traditional flourish between sections of a dialog or card.
 */
@Composable
fun GoldFlourishDivider(
    modifier: Modifier = Modifier,
    widthFraction: Float = 0.62f
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(widthFraction / 2f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color.Transparent, TzaddikColors.GoldBorder.copy(alpha = 0.7f))
                    )
                )
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(7.dp)
                .rotate(45f)
                .background(TzaddikColors.GoldBright, RoundedCornerShape(1.dp))
                .border(0.8.dp, TzaddikColors.GoldBorder, RoundedCornerShape(1.dp))
        )
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(TzaddikColors.GoldBorder.copy(alpha = 0.7f), Color.Transparent)
                    )
                )
        )
    }
}

@Composable
fun StarryScaffold(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
    holyFlash: HolyFlashController? = null,
    candlelightReward: CandlelightRewardController? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Box(modifier.fillMaxSize()) {
        HolyLightBackground(Modifier.fillMaxSize())
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = bottomBar,
            content = content
        )
        holyFlash?.let { ctrl ->
            HolyLightFlashOverlay(
                visible = ctrl.visible,
                alpha = ctrl.alpha,
                sweepT = ctrl.sweepT,
                variant = ctrl.displayedVariant,
                modifier = Modifier.fillMaxSize()
            )
        }
        candlelightReward?.let { ctrl ->
            CandlelightRewardOverlay(
                visible = ctrl.visible,
                flameScale = ctrl.scale,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun MitzModeBottomNav(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    tabs: List<Pair<String, @Composable () -> Unit>>
) {
    NavigationBar(
        modifier = Modifier.navigationBarsPadding(),
        windowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = TzaddikColors.NavyDeep.copy(alpha = 0.94f),
        contentColor = TzaddikColors.GoldBright
    ) {
        tabs.forEachIndexed { index, (label, icon) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = icon,
                label = if (label.isNotBlank()) {
                    {
                        AppText(
                            label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            enableTerms = false,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                } else null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = TzaddikColors.GoldBright,
                    selectedTextColor = TzaddikColors.GoldBright,
                    indicatorColor = TzaddikColors.GoldBorder.copy(alpha = 0.35f),
                    unselectedIconColor = TzaddikColors.ParchTop.copy(alpha = 0.55f),
                    unselectedTextColor = TzaddikColors.ParchTop.copy(alpha = 0.55f)
                )
            )
        }
    }
}

@Composable
fun ParchmentContentCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .shadow(12.dp, RoundedCornerShape(18.dp), ambientColor = TzaddikColors.GoldBorder)
            .clip(RoundedCornerShape(18.dp))
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to TzaddikColors.ParchTop.copy(alpha = 0.96f),
                        0.35f to TzaddikColors.ParchMid.copy(alpha = 0.94f),
                        1f to TzaddikColors.ParchBase.copy(alpha = 0.92f)
                    )
                )
            )
            .border(1.2.dp, TzaddikColors.GoldBorder.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
            .padding(14.dp),
        content = content
    )
}

@Composable
fun ChecklistSectionHeader(title: String, modifier: Modifier = Modifier) {
    Column(modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(20.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(TzaddikColors.GoldBright, TzaddikColors.GoldBorder)
                        ),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
            Spacer(Modifier.width(8.dp))
            AppText(
                title,
                enableTerms = false,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                ),
                color = TzaddikColors.NavyDeep
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.8.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(TzaddikColors.GoldBorder.copy(alpha = 0.55f), Color.Transparent)
                    )
                )
        )
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun CollapsibleChecklistSectionHeader(
    title: String,
    subtitle: String? = null,
    expanded: Boolean,
    onToggle: () -> Unit,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(20.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(TzaddikColors.GoldBright, TzaddikColors.GoldBorder)
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
                Spacer(Modifier.width(8.dp))
                // Translate the bare section name first; appending the count beforehand would
                // produce a string like "Upon waking (2)" which has no bundle entry.
                AppText(
                    text = title,
                    enableTerms = false,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    ),
                    color = TzaddikColors.NavyDeep
                )
                if (itemCount > 0) {
                    Text(
                        text = " ($itemCount)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp
                        ),
                        color = TzaddikColors.NavyDeep
                    )
                }
            }
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (expanded) "Collapse section" else "Expand section",
                tint = TzaddikColors.GoldBorder
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.8.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(TzaddikColors.GoldBorder.copy(alpha = 0.55f), Color.Transparent)
                    )
                )
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            AppText(
                text = subtitle,
                enableTerms = false,
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.NavyMid,
            )
        }
        if (expanded) {
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
fun ParchmentDialog(
    onDismiss: () -> Unit,
    title: String? = null,
    showCloseIcon: Boolean = true,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .shadow(24.dp, DialogShape, ambientColor = TzaddikColors.GoldBorder)
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to TzaddikColors.ParchTop,
                            0.4f to TzaddikColors.ParchMid,
                            1f to TzaddikColors.ParchBase
                        )
                    ),
                    shape = DialogShape
                )
                .border(1.4.dp, TzaddikColors.GoldBorder.copy(alpha = 0.55f), DialogShape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 12.dp, start = 18.dp, end = 18.dp)
            ) {
                if (title != null || showCloseIcon) {
                    Box(Modifier.fillMaxWidth()) {
                        if (title != null) {
                            AppText(
                                title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                ),
                                color = TzaddikColors.GoldBorder,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp, vertical = 4.dp)
                            )
                        }
                        if (showCloseIcon) {
                            IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.TopEnd)) {
                                Icon(Icons.Default.Close, "Close", tint = TzaddikColors.GoldBorder)
                            }
                        }
                    }
                    if (title != null) {
                        GoldFlourishDivider(
                            modifier = Modifier.padding(top = 8.dp, bottom = 14.dp)
                        )
                    }
                }
                Column(Modifier.fillMaxWidth(), content = content)
                if (confirmButton != null || dismissButton != null) {
                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = TzaddikColors.GoldBorder.copy(alpha = 0.25f))
                    Row(
                        Modifier.fillMaxWidth().padding(top = 10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        dismissButton?.invoke()
                        if (dismissButton != null && confirmButton != null) Spacer(Modifier.width(8.dp))
                        confirmButton?.invoke()
                    }
                }
            }
        }
    }
}

@Composable
fun GoldButton(onClick: () -> Unit, text: String, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = TzaddikColors.GoldBorder,
            contentColor = Color.White
        ),
        border = BorderStroke(1.dp, TzaddikColors.GoldBright.copy(alpha = 0.6f)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
    ) {
        AppText(
            text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp,
                textAlign = TextAlign.Center
            ),
            enableTerms = false
        )
    }
}

@Composable
fun ParchmentTextButton(
    onClick: () -> Unit,
    text: String,
    contentColor: Color = TzaddikColors.GoldBorder,
) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(contentColor = contentColor),
    ) {
        AppText(
            text,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
            color = contentColor,
            enableTerms = false,
        )
    }
}

/**
 * Large − / + controls and preset sizes. [scale] is profile multiplier (1 = 100%).
 */
@Composable
fun TextScaleControl(
    scale: Float,
    onScaleChange: (Float) -> Unit,
    onAdjust: (Float) -> Unit,
    modifier: Modifier = Modifier,
    showPreview: Boolean = true
) {
    val clamped = TextScaleDefaults.coerce(scale)
    val canDecrease = clamped > TextScaleDefaults.MIN + 0.001f
    val canIncrease = clamped < TextScaleDefaults.MAX - 0.001f

    Column(modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { if (canDecrease) onAdjust(-TextScaleDefaults.STEP) },
                enabled = canDecrease,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        TzaddikColors.ParchMid.copy(alpha = if (canDecrease) 0.9f else 0.4f),
                        RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, TzaddikColors.GoldBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Smaller text",
                    tint = TzaddikColors.NavyDeep,
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppText(
                    TextScaleDefaults.percentLabel(clamped),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = TzaddikColors.NavyDeep,
                    enableTerms = false
                )
                AppText(
                    "Tap − or + · 10% per step",
                    style = MaterialTheme.typography.labelSmall,
                    color = TzaddikColors.TextMuted,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                onClick = { if (canIncrease) onAdjust(TextScaleDefaults.STEP) },
                enabled = canIncrease,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        TzaddikColors.ParchMid.copy(alpha = if (canIncrease) 0.9f else 0.4f),
                        RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, TzaddikColors.GoldBorder.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Larger text",
                    tint = TzaddikColors.NavyDeep,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "Small" to 0.85f,
                "Normal" to 1f,
                "Large" to 1.35f,
                "XL" to 1.75f,
                "Huge" to 2.2f
            ).forEach { (label, value) ->
                val selected = kotlin.math.abs(clamped - value) < 0.06f
                FilterChip(
                    selected = selected,
                    onClick = { onScaleChange(value) },
                    label = {
                        AppText(
                            label,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            enableTerms = false,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = TzaddikColors.GoldBright.copy(alpha = 0.4f)
                    )
                )
            }
        }
        if (showPreview) {
            Spacer(Modifier.height(12.dp))
            AppText(
                "Preview at this size",
                style = MaterialTheme.typography.labelMedium,
                color = TzaddikColors.TextMuted
            )
            AppText(
                "Morning Shema with its blessings",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = TzaddikColors.TextBrown
            )
            AppText(
                "Educational checklist — confirm practices with your rabbi.",
                style = MaterialTheme.typography.bodyMedium,
                color = TzaddikColors.TextMuted
            )
        }
    }
}
