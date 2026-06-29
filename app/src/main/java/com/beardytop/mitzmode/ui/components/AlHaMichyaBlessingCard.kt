package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.mitzmode.data.MeinShaloshLanguage
import com.beardytop.mitzmode.data.MeinShaloshSelection
import com.beardytop.mitzmode.data.MeinShaloshTextEngine

/**
 * Interactive Bracha Me'ein Shalosh (Al HaMichya) builder with Hebrew / English toggle.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlHaMichyaBlessingCard(
    modifier: Modifier = Modifier,
    fontScale: Float = 1f,
    showEnglish: Boolean? = null,
    onShowEnglishChange: ((Boolean) -> Unit)? = null
) {
    var internalEnglish by remember { mutableStateOf(false) }
    val englishOn = showEnglish ?: internalEnglish
    val setEnglish: (Boolean) -> Unit = onShowEnglishChange ?: { internalEnglish = it }
    var hasMezonot by remember { mutableStateOf(false) }
    var hasWine by remember { mutableStateOf(false) }
    var hasFruit by remember { mutableStateOf(false) }
    var isRoshChodesh by remember { mutableStateOf(false) }
    var isYomTov by remember { mutableStateOf(false) }

    val selection = MeinShaloshSelection(
        hasMezonot = hasMezonot,
        hasWine = hasWine,
        hasFruit = hasFruit,
        isRoshChodesh = isRoshChodesh,
        isYomTov = isYomTov
    )

    val language = if (englishOn) MeinShaloshLanguage.ENGLISH else MeinShaloshLanguage.HEBREW
    val blessingText = remember(selection, language) {
        MeinShaloshTextEngine.build(selection, language)
    }

    val baseFontSize = 23.sp
    val scaledSize = (baseFontSize.value * fontScale).sp
    val lineHeight = (34f * fontScale).sp

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

        if (showEnglish == null) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TranslatableText(
                    text = "English translation",
                    style = MaterialTheme.typography.labelLarge,
                    color = DialogTextPrimary
                )
                Switch(
                    checked = englishOn,
                    onCheckedChange = setEnglish
                )
            }
        }

        TranslatableText(
            text = "What did you have?",
            style = MaterialTheme.typography.labelMedium,
            color = DialogTextMuted
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SelectionChip("Mezonot (grain)", hasMezonot) { hasMezonot = it }
            SelectionChip("Wine / grape juice", hasWine) { hasWine = it }
            SelectionChip("Seven species fruit", hasFruit) { hasFruit = it }
        }

        TranslatableText(
            text = "Today is also:",
            style = MaterialTheme.typography.labelMedium,
            color = DialogTextMuted,
            modifier = Modifier.padding(top = 4.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            SelectionChip("Rosh Chodesh", isRoshChodesh) { isRoshChodesh = it }
            SelectionChip("Yom Tov", isYomTov) { isYomTov = it }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = DialogGoldBorder.copy(alpha = 0.25f),
            thickness = 0.8.dp
        )

        Text(
            text = blessingText,
            style = TextStyle(
                fontSize = scaledSize,
                lineHeight = lineHeight,
                textDirection = if (englishOn) TextDirection.Ltr else TextDirection.Rtl,
                color = DialogTextPrimary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp)
        )
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
                style = MaterialTheme.typography.labelMedium
            )
        }
    )
}
