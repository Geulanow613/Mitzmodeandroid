package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.beatzaddik.domain.AppDisclaimer
import com.beardytop.mitzmode.ui.LocalTranslationViewModel
import com.beardytop.mitzmode.viewmodel.TranslationViewModel
import kotlinx.coroutines.launch

@Composable
fun TranslationNoticeDialog(
    languageName: String,
    targetLanguageCode: String,
    onDismiss: () -> Unit,
    onUnderstood: () -> Unit,
    translationViewModel: TranslationViewModel =
        LocalTranslationViewModel.current ?: hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    var translatedTitle by remember { mutableStateOf(AppDisclaimer.TRANSLATION_NOTICE_TITLE) }
    var translatedSwitchedTo by remember { mutableStateOf("You've switched to") }
    var translatedDisclaimer by remember { mutableStateOf(AppDisclaimer.TRANSLATION_NOTICE_DISCLAIMER) }
    var translatedEnglishLabel by remember { mutableStateOf("English:") }
    var translatedGotIt by remember { mutableStateOf("Got it") }
    var translatedClose by remember { mutableStateOf("Close") }

    LaunchedEffect(targetLanguageCode) {
        if (targetLanguageCode != "en") {
            scope.launch {
                try {
                    translatedTitle = translationViewModel.translateTextToLanguage(
                        AppDisclaimer.TRANSLATION_NOTICE_TITLE,
                        targetLanguageCode
                    )
                    translatedSwitchedTo = translationViewModel.translateTextToLanguage(
                        "You've switched to",
                        targetLanguageCode
                    )
                    translatedDisclaimer = translationViewModel.translateTextToLanguage(
                        AppDisclaimer.TRANSLATION_NOTICE_DISCLAIMER,
                        targetLanguageCode
                    )
                    translatedEnglishLabel = translationViewModel.translateTextToLanguage(
                        "English:",
                        targetLanguageCode
                    )
                    translatedGotIt = translationViewModel.translateTextToLanguage("Got it", targetLanguageCode)
                    translatedClose = translationViewModel.translateTextToLanguage("Close", targetLanguageCode)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = translatedClose,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Information",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = translatedTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "$translatedSwitchedTo $languageName.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = translatedDisclaimer,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LinkText(
                    displayText = AppDisclaimer.FEEDBACK_EMAIL,
                    url = AppDisclaimer.FEEDBACK_EMAIL_MAILTO,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                if (targetLanguageCode != "en") {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = translatedEnglishLabel,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = AppDisclaimer.TRANSLATION_NOTICE_DISCLAIMER,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LinkText(
                        displayText = AppDisclaimer.FEEDBACK_EMAIL,
                        url = AppDisclaimer.FEEDBACK_EMAIL_MAILTO,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = onUnderstood,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(48.dp)
                ) {
                    Text(
                        text = translatedGotIt,
                        maxLines = 1,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
