package com.beardytop.beatzaddik.ui.components

import com.beardytop.beatzaddik.ui.theme.TzaddikColors

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

/** Mitz Mode liturgy shows English source text only. */
@Composable
fun LiturgyTranslationText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    textAlign: TextAlign? = TextAlign.Start,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    Text(
        text = text,
        style = style,
        modifier = modifier,
        textAlign = textAlign,
        color = color,
    )
}

