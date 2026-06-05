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
fun LocationPermissionDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    ParchmentDialog(
        onDismiss = onDismiss,
        title = "Location Access Needed",
        confirmButton = {
            GoldButton(
                onClick = onOpenSettings,
                text = "Open Settings",
                modifier = Modifier.width(190.dp)
            )
        },
        dismissButton = {
            ParchmentTextButton(onClick = onDismiss, text = "Close")
        }
    ) {
        AppText(
            "Location permission was not granted.",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = TzaddikColors.NavyDeep
        )
        Spacer(Modifier.height(8.dp))
        AppText(
            "To let the app calculate accurate prayer times (zmanim) for your location, " +
                "you need to allow location access in your phone's settings.",
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.TextBrown
        )
        Spacer(Modifier.height(10.dp))
        AppText(
            "Tap \"Open Phone Settings,\" then go to Permissions → Location and set it to " +
                "\"Allow while using the app.\"",
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted
        )
        Spacer(Modifier.height(8.dp))
    }
}
