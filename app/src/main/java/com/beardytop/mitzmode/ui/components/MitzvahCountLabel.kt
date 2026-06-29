package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.beardytop.mitzmode.ui.theme.Cinzel

@Composable
fun MitzvahCountLabel(
    count: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineLarge,
    color: Color = Color.Unspecified,
    brush: Brush? = null,
    fontFamily: FontFamily = Cinzel,
) {
    val resolvedStyle = style.copy(
        fontFamily = fontFamily,
        textAlign = TextAlign.Center,
        brush = brush,
    )
    if (count == 1) {
        TranslatableText(
            text = "1 Mitzvah",
            modifier = modifier,
            style = resolvedStyle,
            color = color,
            textAlign = TextAlign.Center,
            enableHalachicTerms = false,
        )
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$count ",
                style = resolvedStyle,
                color = if (brush == null && color != Color.Unspecified) color else style.color,
            )
            TranslatableText(
                text = "Mitzvot",
                style = resolvedStyle,
                color = color,
                textAlign = TextAlign.Center,
                enableHalachicTerms = false,
            )
        }
    }
}
