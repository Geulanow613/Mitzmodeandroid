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
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.viewmodel.TranslationViewModel

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    val translationViewModel: TranslationViewModel = hiltViewModel()
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()

    val introText = if (translationEnabled && currentLanguage != "en") {
        when (currentLanguage) {
            "he" -> "האפליקציה הזו מובאת לכם על ידי"
            "es" -> "Esta aplicación es presentada por"
            "fr" -> "Cette application vous est proposée par"
            "de" -> "Diese App wird Ihnen präsentiert von"
            "it" -> "Questa app è presentata da"
            "pt" -> "Este aplicativo é apresentado por"
            "ru" -> "Это приложение представлено"
            "ar" -> "هذا التطبيق مقدم لكم من"
            "zh" -> "此应用由以下公司为您呈现："
            "ja" -> "このアプリは以下によって提供されています："
            else -> "This app is brought to you by"
        }
    } else {
        "This app is brought to you by"
    }

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
                text = introText,
                style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                color = DialogTextPrimary,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
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
