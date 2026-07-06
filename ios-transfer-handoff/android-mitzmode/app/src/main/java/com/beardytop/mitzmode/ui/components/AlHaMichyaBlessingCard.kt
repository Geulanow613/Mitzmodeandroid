package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.mitzmode.data.MeinShaloshLanguage
import com.beardytop.mitzmode.data.MeinShaloshSelection
import com.beardytop.mitzmode.data.MeinShaloshTextEngine

/**
 * Interactive Bracha Me'ein Shalosh (Al HaMichya) builder: Hebrew liturgy on top, with English below.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlHaMichyaBlessingCard(
    modifier: Modifier = Modifier,
    fontScale: Float = 1f,
    showTranslation: Boolean = false,
) {
    val translationLanguage = MeinShaloshLanguage.ENGLISH
    val layoutDirection = LayoutDirection.Ltr

    var hasMezonot by remember { mutableStateOf(false) }
    var hasWine by remember { mutableStateOf(false) }
    var hasFruit by remember { mutableStateOf(false) }
    var isRoshChodesh by remember { mutableStateOf(false) }
    var isPesach by remember { mutableStateOf(false) }
    var isSukkot by remember { mutableStateOf(false) }

    val selection = MeinShaloshSelection(
        hasMezonot = hasMezonot,
        hasWine = hasWine,
        hasFruit = hasFruit,
        isRoshChodesh = isRoshChodesh,
        isPesach = isPesach,
        isSukkot = isSukkot
    )

    val hebrewBlessingText = remember(selection) {
        if (!selection.hasAnyFood) {
            ""
        } else {
            MeinShaloshTextEngine.build(selection, MeinShaloshLanguage.HEBREW)
        }
    }
    val translatedBlessingText = remember(selection, translationLanguage) {
        if (!selection.hasAnyFood || translationLanguage == null) {
            ""
        } else {
            MeinShaloshTextEngine.build(selection, translationLanguage)
        }
    }
    val emptyHebrewPrompt = remember {
        MeinShaloshTextEngine.build(MeinShaloshSelection(), MeinShaloshLanguage.HEBREW)
    }

    val baseFontSize = 23.sp
    val scaledSize = (baseFontSize.value * fontScale).sp
    val translationFontSize = (baseFontSize.value * 0.9f * fontScale).sp
    val lineHeight = (34f * fontScale).sp
    val translationLineHeight = (30f * fontScale).sp

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TranslatableText(
            text = "Bracha Me'ein Shalosh (Al HaMichya)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = DialogGoldBorder
        )

        TranslatableText(
            text = "After Mezonot, wine/grape juice, and/or shivat ha-minim tree fruits — select what you had at this sitting.",
            style = MaterialTheme.typography.bodySmall,
            color = DialogTextMuted,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (!selection.hasAnyFood) {
            Text(
                text = emptyHebrewPrompt,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDirection = TextDirection.Rtl,
                    color = DialogTextMuted
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
            if (showTranslation && translationLanguage != null) {
                Text(
                    text = MeinShaloshTextEngine.build(
                        MeinShaloshSelection(),
                        translationLanguage,
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDirection = TextDirection.Ltr,
                        color = DialogTextMuted
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                )
            }
        }

        TranslatableText(
            text = "What did you have?",
            style = MaterialTheme.typography.labelMedium,
            color = DialogTextMuted
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SelectionChipWithHint(
                label = "Mezonot (grain)",
                hint = "Wheat, barley, rye, oats, spelt",
                selected = hasMezonot,
                onSelected = { hasMezonot = it }
            )
            SelectionChipWithHint(
                label = "Wine / grape juice",
                selected = hasWine,
                onSelected = { hasWine = it }
            )
            SelectionChipWithHint(
                label = "Seven species fruit",
                hint = "Grapes, figs, pomegranates, olives, dates",
                selected = hasFruit,
                onSelected = { hasFruit = it }
            )
        }

        TranslatableText(
            text = "Today is also:",
            style = MaterialTheme.typography.labelMedium,
            color = DialogTextMuted,
            modifier = Modifier.padding(top = 4.dp)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SelectionChip("Rosh Chodesh", isRoshChodesh) { isRoshChodesh = it }
            SelectionChip("Pesach", isPesach) {
                isPesach = it
                if (it) isSukkot = false
            }
            SelectionChip("Sukkot", isSukkot) {
                isSukkot = it
                if (it) isPesach = false
            }
        }

        if (selection.hasAnyFood) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = DialogGoldBorder.copy(alpha = 0.25f),
                thickness = 0.8.dp
            )

            Text(
                text = hebrewBlessingText,
                style = TextStyle(
                    fontSize = scaledSize,
                    lineHeight = lineHeight,
                    textDirection = TextDirection.Rtl,
                    color = DialogTextPrimary
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            )

            if (showTranslation && translationLanguage != null) {
                Text(
                    text = translatedBlessingText,
                    style = TextStyle(
                        fontSize = translationFontSize,
                        lineHeight = translationLineHeight,
                        textDirection = TextDirection.Ltr,
                        color = DialogTextMuted
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                )
            }
        }
    }
    }
}

@Composable
private fun SelectionChip(
    label: String,
    selected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = { onSelected(!selected) },
        label = {
            TranslatableText(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                enableHalachicTerms = false
            )
        }
    )
}

@Composable
private fun SelectionChipWithHint(
    label: String,
    hint: String? = null,
    selected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.widthIn(max = 200.dp)
    ) {
        FilterChip(
            selected = selected,
            onClick = { onSelected(!selected) },
            label = {
                TranslatableText(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    enableHalachicTerms = false
                )
            }
        )
        hint?.let {
            TranslatableText(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = DialogTextMuted.copy(alpha = 0.9f),
                textAlign = TextAlign.Start,
                enableHalachicTerms = false,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
