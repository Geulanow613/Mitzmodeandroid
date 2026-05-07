package com.beardytop.mitzmode.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.beardytop.mitzmode.utils.EmailConfig
import kotlinx.coroutines.launch

@Composable
fun AddMitzvahDialog(
    onDismiss: () -> Unit
) {
    var mitzvahText by remember { mutableStateOf("") }
    var submitterName by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    ParchmentDialog(
        onDismiss = { if (!isSubmitting) onDismiss() },
        title = "Submit a Mitzvah",
        dismissOnClickOutside = !isSubmitting,
        confirmButton = {
            GoldButton(
                onClick = {
                    if (mitzvahText.isNotBlank()) {
                        scope.launch {
                            isSubmitting = true
                            try {
                                val success = EmailConfig.sendMitzvahSubmission(
                                    context = context,
                                    mitzvahText = mitzvahText.trim(),
                                    submitterName = submitterName.trim().ifBlank { "Anonymous" }
                                )
                                if (success) {
                                    showSuccess = true
                                    mitzvahText = ""
                                    submitterName = ""
                                } else {
                                    showError = true
                                }
                            } catch (e: Exception) {
                                showError = true
                            } finally {
                                isSubmitting = false
                            }
                        }
                    }
                },
                text = if (isSubmitting) "Submitting..." else "Submit",
                enabled = mitzvahText.isNotBlank() && !isSubmitting
            )
        },
        dismissButton = {
            ParchmentTextButton(
                onClick = { if (!isSubmitting) onDismiss() },
                text = "Cancel"
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TranslatableText(
                text = "Have a great mitzvah idea? Share it with other users!",
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center
                ),
                color = DialogTextMuted,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            OutlinedTextField(
                value = mitzvahText,
                onValueChange = { mitzvahText = it },
                label = { TranslatableText("Mitzvah Description") },
                placeholder = { TranslatableText("e.g., Call your grandmother to check on her...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                enabled = !isSubmitting,
                colors = parchmentTextFieldColors()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = submitterName,
                onValueChange = { submitterName = it },
                label = { TranslatableText("Your Name/Initials (Optional)") },
                placeholder = { TranslatableText("e.g., Sarah B.") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isSubmitting,
                colors = parchmentTextFieldColors()
            )

            if (isSubmitting) {
                Spacer(modifier = Modifier.height(12.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = DialogGoldBorder
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Guidelines card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = DialogGoldBorder.copy(alpha = 0.06f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(
                        width = 0.8.dp,
                        color = DialogGoldBorder.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(14.dp)
            ) {
                Column {
                    TranslatableText(
                        text = "Guidelines",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = DialogGoldBorder
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TranslatableText(
                        text = "• Keep it positive and actionable\n" +
                              "• Make it accessible to everyone\n" +
                              "• Include specific details when helpful\n" +
                              "• Remember: small acts can have big impact!",
                        style = MaterialTheme.typography.bodySmall,
                        color = DialogTextPrimary
                    )
                }
            }
        }
    }

    if (showSuccess) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(3000)
            showSuccess = false
            onDismiss()
        }
        ParchmentDialog(
            onDismiss = {
                showSuccess = false
                onDismiss()
            },
            title = "Thank You!",
            confirmButton = {
                GoldButton(
                    onClick = {
                        showSuccess = false
                        onDismiss()
                    },
                    text = "Great!"
                )
            }
        ) {
            TranslatableText(
                text = "Your mitzvah has been submitted for review. It may appear in future updates!",
                style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                color = DialogTextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
    }

    if (showError) {
        ParchmentDialog(
            onDismiss = { showError = false },
            title = "Submission Failed",
            confirmButton = {
                GoldButton(onClick = { showError = false }, text = "OK")
            }
        ) {
            TranslatableText(
                text = "Sorry, we couldn't submit your mitzvah right now. Please try again later or check your internet connection.",
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                color = DialogTextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun parchmentTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = DialogGoldBorder,
    unfocusedBorderColor = DialogGoldBorder.copy(alpha = 0.45f),
    focusedLabelColor = DialogGoldBorder,
    unfocusedLabelColor = DialogTextMuted,
    cursorColor = DialogGoldBorder,
    focusedTextColor = DialogTextPrimary,
    unfocusedTextColor = DialogTextPrimary
)
