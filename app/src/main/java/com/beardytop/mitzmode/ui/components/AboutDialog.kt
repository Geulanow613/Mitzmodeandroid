package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    ParchmentDialog(
        onDismiss = onDismiss,
        title = "About",
        confirmButton = { GoldButton(onClick = onDismiss, text = "Close") }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "✡",
                fontSize = 36.sp,
                color = DialogGoldBorder
            )

            TranslatableText(
                text = "This app is brought to you by",
                style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                color = DialogTextPrimary,
                modifier = Modifier.fillMaxWidth()
            )

            TranslatableText(
                text = "Beardy Top Productions",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                color = DialogGoldBorder,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(4.dp))

            LinkText(
                displayText = "www.beardy.top",
                url = "https://www.beardy.top",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = DialogGoldBorder
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
