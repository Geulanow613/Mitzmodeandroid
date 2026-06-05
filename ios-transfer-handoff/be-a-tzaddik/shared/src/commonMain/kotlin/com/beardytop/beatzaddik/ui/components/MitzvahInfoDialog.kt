package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.beardytop.beatzaddik.domain.ResolvedChecklistItem
import com.beardytop.beatzaddik.platform.PlatformBackHandler
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

private val InfoDialogShape = RoundedCornerShape(22.dp)

@Composable
fun MitzvahInfoDialog(
    item: ResolvedChecklistItem,
    onDismiss: () -> Unit
) {
    PlatformBackHandler(onBack = onDismiss)
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .shadow(28.dp, InfoDialogShape, ambientColor = TzaddikColors.GoldBorder.copy(alpha = 0.4f))
                .clip(InfoDialogShape)
                .background(Color.White)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { }
        ) {
            Column(Modifier.fillMaxWidth()) {
                // Navy gradient header with title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(TzaddikColors.NavyDeep, TzaddikColors.NavyMid.copy(alpha = 0.9f))
                            )
                        )
                        .padding(top = 20.dp, bottom = 18.dp, start = 20.dp, end = 52.dp)
                ) {
                    Column {
                        Text(
                            item.displayTitle,
                            style = MaterialTheme.typography.titleLarge,
                            color = TzaddikColors.GoldBright,
                            fontWeight = FontWeight.SemiBold
                        )
                        // Gold accent underline
                        Box(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(0.3f)
                                .height(1.5.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(TzaddikColors.GoldBright.copy(alpha = 0.7f), Color.Transparent)
                                    )
                                )
                        )
                    }
                }

                // Parchment body
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(TzaddikColors.ParchTop, TzaddikColors.ParchBase)
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp)
                    ) {
                        MitzvahExplanationContent(
                            explanation = item.displayExplanation,
                            zmanHint = item.zmanHint,
                            zmanMakeupNote = item.zmanMakeupNote,
                            situational = item.def.situational,
                            learnMoreUrl = item.learnMoreUrl,
                            learnMoreLabel = item.learnMoreLabel,
                            resourceLinks = item.resourceLinks
                        )
                        Spacer(Modifier.height(8.dp))
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                            GoldButton(onClick = onDismiss, text = "Done")
                        }
                    }
                }
            }

            // Close icon — positioned top-right of the whole dialog
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            ) {
                Icon(Icons.Default.Close, "Close", tint = TzaddikColors.GoldBright)
            }
        }
    }
}
