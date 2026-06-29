package com.beardytop.mitzmode.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.data.BirkatHamazonText
import com.beardytop.mitzmode.ui.LocalTranslationViewModel
import com.beardytop.mitzmode.viewmodel.TranslationViewModel

@Composable
fun BirkatHamazonDialog(
    onDismiss: () -> Unit
) {
    val translationViewModel: TranslationViewModel =
        LocalTranslationViewModel.current ?: hiltViewModel()
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val isTranslationActive = translationEnabled && currentLanguage != "en"

    var showEnglish by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val sections = BirkatHamazonText.sections
    var fontScale by remember { mutableStateOf(1f) }
    var expandedCollapsibleTitles by remember { mutableStateOf(setOf<String>()) }
    
    val transformableState = rememberTransformableState { zoomChange, _, _ ->
        fontScale = (fontScale * zoomChange).coerceIn(0.5f, 3f)
    }

    val scaledFontSize = (MaterialTheme.typography.bodyLarge.fontSize.value * fontScale).sp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "(Nusach Ashkenaz)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                )

                // Header with close button and title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                    
                    Text(
                        text = "ברכת המזון",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                }

                // Toggle buttons for English and zoom
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { showEnglish = !showEnglish }) {
                        TranslatableText(
                            if (showEnglish) {
                                if (isTranslationActive) "Hide translation" else "Hide English"
                            } else {
                                if (isTranslationActive) "Show translation" else "Show English"
                            }
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { fontScale = (fontScale - 0.2f).coerceIn(0.5f, 3f) }
                        ) {
                            Text("A-", style = MaterialTheme.typography.titleMedium)
                        }
                        IconButton(
                            onClick = { fontScale = (fontScale + 0.2f).coerceIn(0.5f, 3f) }
                        ) {
                            Text("A+", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }

                // Scrollable content with font scaling
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .transformable(transformableState)
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        sections.forEach { section ->
                            val summary = section.collapsibleSummary
                            if (summary != null) {
                                val expanded =
                                    expandedCollapsibleTitles.contains(section.title)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = ripple()
                                        ) {
                                            expandedCollapsibleTitles =
                                                if (expanded) {
                                                    expandedCollapsibleTitles - section.title
                                                } else {
                                                    expandedCollapsibleTitles + section.title
                                                }
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = summary,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontSize = scaledFontSize
                                            ),
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        val summaryEn = section.collapsibleSummaryEnglish
                                        if (showEnglish && summaryEn != null) {
                                            TranslatableText(
                                                text = summaryEn,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = (MaterialTheme.typography.bodyMedium.fontSize.value * fontScale).sp
                                                ),
                                                textAlign = TextAlign.Start,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 4.dp),
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = if (expanded) {
                                            Icons.Default.ExpandLess
                                        } else {
                                            Icons.Default.ExpandMore
                                        },
                                        contentDescription = if (expanded) {
                                            "Collapse"
                                        } else {
                                            "Expand"
                                        }
                                    )
                                }
                                AnimatedVisibility(
                                    visible = expanded,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = section.hebrew,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontSize = scaledFontSize
                                            ),
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (showEnglish && section.english != null) {
                                            TranslatableText(
                                                text = section.english,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = (MaterialTheme.typography.bodyMedium.fontSize.value * fontScale).sp
                                                ),
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                                            )
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = section.hebrew,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = scaledFontSize
                                    ),
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                if (showEnglish && section.english != null) {
                                    TranslatableText(
                                        text = section.english,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = (MaterialTheme.typography.bodyMedium.fontSize.value * fontScale).sp
                                        ),
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                                    )
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }
    }
}
