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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
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
    
    // State for translated texts
    var translatedTitle by remember { mutableStateOf("Translation Notice") }
    var translatedSwitchedTo by remember { mutableStateOf("You've switched to") }
    var translatedPleaseNote by remember { mutableStateOf("Please note:") }
    var translatedPoint1 by remember { mutableStateOf("• Text is translated from English using Google Translate") }
    var translatedPoint2 by remember { mutableStateOf("• Translations may not be perfect or culturally accurate") }
    var translatedPoint3 by remember { mutableStateOf("• All website links lead to English-only content") }
    var translatedPoint4 by remember { mutableStateOf("• For the best experience, consider using English") }
    var translatedCancel by remember { mutableStateOf("Cancel") }
    var translatedGotIt by remember { mutableStateOf("Got it") }
    var translatedClose by remember { mutableStateOf("Close") }
    
    // Translate texts when the dialog is shown
    LaunchedEffect(targetLanguageCode) {
        if (targetLanguageCode != "en") {
            scope.launch {
                try {
                    translatedTitle = translationViewModel.translateTextToLanguage("Translation Notice", targetLanguageCode)
                    translatedSwitchedTo = translationViewModel.translateTextToLanguage("You've switched to", targetLanguageCode)
                    translatedPleaseNote = translationViewModel.translateTextToLanguage("Please note:", targetLanguageCode)
                    translatedPoint1 = translationViewModel.translateTextToLanguage("• Text is translated from English using Google Translate", targetLanguageCode)
                    translatedPoint2 = translationViewModel.translateTextToLanguage("• Translations may not be perfect or culturally accurate", targetLanguageCode)
                    translatedPoint3 = translationViewModel.translateTextToLanguage("• All website links lead to English-only content", targetLanguageCode)
                    translatedPoint4 = translationViewModel.translateTextToLanguage("• For the best experience, consider using English", targetLanguageCode)
                    translatedCancel = translationViewModel.translateTextToLanguage("Cancel", targetLanguageCode)
                    translatedGotIt = translationViewModel.translateTextToLanguage("Got it", targetLanguageCode)
                    translatedClose = translationViewModel.translateTextToLanguage("Close", targetLanguageCode)
                } catch (e: Exception) {
                    // If translation fails, keep the English text
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
                // Close button in top right corner
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
                
                // Info icon
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Information",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                // Title
                Text(
                    text = translatedTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Main message
                Text(
                    text = "$translatedSwitchedTo $languageName.\n$translatedPleaseNote",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Translation info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = translatedPoint1,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = translatedPoint2,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = translatedPoint3,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = translatedPoint4,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                
                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = translatedCancel,
                            maxLines = 1,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    
                    Button(
                        onClick = onUnderstood,
                        modifier = Modifier
                            .weight(1f)
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
} 