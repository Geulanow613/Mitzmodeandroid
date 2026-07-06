package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// Shared parchment + gold tokens for all dialogs.
internal val ParchmentTopColor   = Color(0xFFFFFBEE)
internal val ParchmentMidColor   = Color(0xFFFFF8E1)
internal val ParchmentBaseColor  = Color(0xFFFFF0CC)
internal val DialogGoldBorder    = Color(0xFFB8860B)
internal val DialogGoldBright    = Color(0xFFFFD56B)
internal val DialogTextPrimary   = Color(0xFF1A0A00)
internal val DialogTextMuted     = Color(0xFF5C4A2E)
internal val DialogShape         = RoundedCornerShape(22.dp)

/**
 * Beautiful reusable parchment-style dialog scaffold with optional gold close button.
 * Use this whenever you'd reach for an `AlertDialog` so we get a consistent feel.
 */
@Composable
fun ParchmentDialog(
    onDismiss: () -> Unit,
    title: String? = null,
    showCloseIcon: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    fullWidth: Float = 0.92f,
    enableHalachicTerms: Boolean = true,
    confirmButton: (@Composable () -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fullWidth)
                .shadow(elevation = 24.dp, shape = DialogShape, ambientColor = DialogGoldBorder, spotColor = DialogGoldBorder)
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to ParchmentTopColor,
                            0.4f to ParchmentMidColor,
                            1f to ParchmentBaseColor
                        )
                    ),
                    shape = DialogShape
                )
                .border(
                    width = 1.4.dp,
                    color = DialogGoldBorder.copy(alpha = 0.55f),
                    shape = DialogShape
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { /* swallow taps inside the parchment */ }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 12.dp, start = 18.dp, end = 18.dp)
            ) {
                // Title row with optional close
                if (title != null || showCloseIcon) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (title != null) {
                            TranslatableText(
                                text = title,
                                enableHalachicTerms = enableHalachicTerms,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                ),
                                color = DialogGoldBorder,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp, vertical = 4.dp)
                            )
                        }
                        if (showCloseIcon) {
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = DialogGoldBorder
                                )
                            }
                        }
                    }
                    if (title != null) {
                        // Decorative gold underline beneath title
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .padding(top = 6.dp, bottom = 12.dp)
                                .align(Alignment.CenterHorizontally),
                            color = DialogGoldBorder.copy(alpha = 0.55f),
                            thickness = 1.dp
                        )
                    }
                }

                // Scaffolded body
                Column(modifier = Modifier.fillMaxWidth()) {
                    content()
                }

                // Action row
                if (confirmButton != null || dismissButton != null) {
                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(
                        color = DialogGoldBorder.copy(alpha = 0.25f),
                        thickness = 0.8.dp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (dismissButton != null) {
                            dismissButton()
                            Spacer(Modifier.width(8.dp))
                        }
                        if (confirmButton != null) {
                            confirmButton()
                        }
                    }
                }
            }
        }
    }
}

/**
 * Pretty gold-gradient pill button used inside parchment dialogs.
 */
@Composable
fun GoldButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = DialogGoldBorder,
            contentColor = Color.White,
            disabledContainerColor = DialogGoldBorder.copy(alpha = 0.3f),
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        ),
        border = BorderStroke(1.dp, DialogGoldBright.copy(alpha = 0.6f)),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        ),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
    ) {
        TranslatableText(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.6.sp
            ),
            color = Color.White
        )
    }
}

/**
 * Subtle outline pill button for secondary dialog actions (e.g., Cancel).
 */
@Composable
fun ParchmentTextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = DialogGoldBorder)
    ) {
        TranslatableText(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            ),
            color = DialogGoldBorder
        )
    }
}
