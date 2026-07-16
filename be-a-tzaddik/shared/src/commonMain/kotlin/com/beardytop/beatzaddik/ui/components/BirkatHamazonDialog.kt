package com.beardytop.beatzaddik.ui.components

import com.beardytop.beatzaddik.ui.theme.TzaddikColors

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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.beardytop.beatzaddik.domain.liturgy.BirkatHamazonText

@Composable
fun BirkatHamazonDialog(
    onDismiss: () -> Unit
) {
    val showLiturgyTranslation = true

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
            color = TzaddikColors.ParchMid,
            contentColor = TzaddikColors.TextBrown,
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AppText(
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

                // Toggle translation (not needed when UI language is Hebrew — text is already Hebrew)
                if (showLiturgyTranslation) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = { showEnglish = !showEnglish }) {
                            AppText(
                                if (showEnglish) "Hide translation" else "Show translation"
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
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center
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
                                    Text(
                                        text = summary,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = scaledFontSize
                                        ),
                                        textAlign = TextAlign.Start,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = if (expanded) {
                                            Icons.Default.KeyboardArrowUp
                                        } else {
                                            Icons.Default.KeyboardArrowDown
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
                                        section.instruction?.let { note ->
                                            Text(
                                                text = note,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = (MaterialTheme.typography.bodyMedium.fontSize.value * fontScale).sp
                                                ),
                                                textAlign = TextAlign.Start,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 8.dp),
                                            )
                                        }
                                        Text(
                                            text = section.hebrew,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontSize = scaledFontSize
                                            ),
                                            textAlign = TextAlign.End,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (showEnglish && showLiturgyTranslation && section.english != null) {
                                            LiturgyTranslationText(
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
                                section.instruction?.let { note ->
                                    Text(
                                        text = note,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = (MaterialTheme.typography.bodyMedium.fontSize.value * fontScale).sp
                                        ),
                                        textAlign = TextAlign.Start,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp),
                                    )
                                }
                                Text(
                                    text = section.hebrew,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = scaledFontSize
                                    ),
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                if (showEnglish && showLiturgyTranslation && section.english != null) {
                                    LiturgyTranslationText(
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

