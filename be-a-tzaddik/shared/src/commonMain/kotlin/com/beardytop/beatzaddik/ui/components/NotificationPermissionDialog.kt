package com.beardytop.beatzaddik.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

@Composable
fun NotificationPermissionDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit,
) {
    ParchmentDialog(
        onDismiss = onDismiss,
        title = "Notifications Needed",
        confirmButton = {
            GoldButton(
                onClick = onOpenSettings,
                text = "Open Settings",
                modifier = Modifier.width(190.dp),
            )
        },
        dismissButton = {
            ParchmentTextButton(onClick = onDismiss, text = "Close")
        },
    ) {
        AppText(
            "Notification permission was not granted.",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = TzaddikColors.NavyDeep,
        )
        Spacer(Modifier.height(8.dp))
        AppText(
            "To show the kashrut timer in your notification bar, allow notifications for this app.",
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.TextBrown,
        )
        Spacer(Modifier.height(10.dp))
        AppText(
            "Tap \"Open Settings,\" then turn notifications on for this app. After that, turn " +
                "\"Show timer status in notification bar\" on again in Settings.",
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted,
        )
        Spacer(Modifier.height(8.dp))
    }
}
