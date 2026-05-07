package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MitzvahCountDialog(
    onDismiss: () -> Unit
) {
    ParchmentDialog(
        onDismiss = onDismiss,
        title = "Mitzvah Count",
        confirmButton = { GoldButton(onClick = onDismiss, text = "OK") }
    ) {
        TranslatableText(
            text = "Keep going! Every mitzvah counts!",
            style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
            color = DialogTextPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}
