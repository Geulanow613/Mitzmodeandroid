package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.ui.LocalTranslationViewModel
import com.beardytop.mitzmode.data.LanguageInfo
import com.beardytop.mitzmode.viewmodel.TranslationViewModel
import com.beardytop.beatzaddik.ui.translation.BundledTranslationLanguages

@Composable
fun LanguageSelectionDialog(
    onDismiss: () -> Unit,
    onNonEnglishLanguageSelected: (languageCode: String, languageName: String) -> Unit = { _, _ -> },
    translationViewModel: TranslationViewModel =
        LocalTranslationViewModel.current ?: hiltViewModel()
) {
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val supportedLanguages = translationViewModel.getSupportedLanguages()
    
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
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Translatable note at the top
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TranslatableText(
                        text = "Translate the app!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    TranslatableText(
                        text = "(translations may not be perfect)",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }
                
                TranslatableText(
                    text = "Language Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // Translation toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TranslatableText(
                        text = "Enable Translation",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = translationEnabled,
                        onCheckedChange = { enabled ->
                            translationViewModel.setTranslationEnabled(enabled)
                        }
                    )
                }
                
                if (translationEnabled) {
                    TranslatableText(
                        text = "Select Language",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                    ) {
                        items(supportedLanguages) { language ->
                            LanguageItem(
                                language = language,
                                isSelected = currentLanguage == language.code,
                                onSelect = { 
                                    if (language.code != "en") {
                                        translationViewModel.setCurrentLanguage(language.code)
                                        onNonEnglishLanguageSelected(language.code, language.name)
                                    } else {
                                        translationViewModel.setCurrentLanguage(language.code)
                                    }
                                }
                            )
                        }
                    }
                }
                
                // Action buttons with cool gold styling
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color(0xFFFFD700),
                                spotColor = Color(0xFFFFA000)
                            )
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFFD700), // Gold
                                        Color(0xFFFFC107), // Amber
                                        Color(0xFFFF8F00)  // Dark orange
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 2.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFFFF176), // Light gold
                                        Color(0xFFFFB300)  // Dark gold
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF1A1A1A)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
                    ) {
                        TranslatableText(
                            text = "Close",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageItem(
    language: LanguageInfo,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onSelect
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFFFFD700), // Gold when selected
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Column {
            // Keep language native names as regular Text (not translatable)
            Text(
                text = language.nativeName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurface
            )
            // Keep English language names as regular Text (not translatable)
            Text(
                text = buildString {
                    append(language.name)
                    if (BundledTranslationLanguages.isBundled(language.code)) {
                        append(" · offline")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 