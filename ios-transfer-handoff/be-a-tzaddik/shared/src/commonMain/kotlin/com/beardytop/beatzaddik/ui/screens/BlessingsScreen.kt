package com.beardytop.beatzaddik.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.ui.components.BirkatHamazonDialog
import com.beardytop.beatzaddik.ui.components.BrachotDialog
import com.beardytop.beatzaddik.ui.components.GoldFlourishDivider
import com.beardytop.beatzaddik.ui.components.LocalHalachicTermsEnabled
import com.beardytop.beatzaddik.ui.components.ParchmentContentCard
import com.beardytop.beatzaddik.ui.components.TefilatHaderechDialog
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

private enum class BlessingDialog {
    BirkatHamazon,
    TefilatHaderech,
    Brachot,
}

@Composable
fun BlessingsScreen() {
    var openDialog by remember { mutableStateOf<BlessingDialog?>(null) }

    // No glossary underlines anywhere on this hub — titles/subtitles stay clean.
    CompositionLocalProvider(LocalHalachicTermsEnabled provides false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Blessings",
                style = MaterialTheme.typography.headlineSmall,
                color = TzaddikColors.GoldBright,
            )
            // Light parchment tone — TextBrown is invisible on the navy sky background.
            Text(
                text = "Birkat Hamazon, Traveler's Prayer, and everyday blessings.",
                style = MaterialTheme.typography.bodyMedium,
                color = TzaddikColors.ParchTop.copy(alpha = 0.88f),
            )
            GoldFlourishDivider()

            BlessingHubCard(
                title = "Birkat Hamazon",
                subtitle = "Grace after meals",
                onClick = { openDialog = BlessingDialog.BirkatHamazon },
            )
            BlessingHubCard(
                title = "Traveler's Prayer",
                subtitle = "Tefilat Haderech",
                onClick = { openDialog = BlessingDialog.TefilatHaderech },
            )
            BlessingHubCard(
                title = "Blessings",
                subtitle = "Everyday brachot · Me'ein Shalosh",
                onClick = { openDialog = BlessingDialog.Brachot },
            )
        }
    }

    when (openDialog) {
        BlessingDialog.BirkatHamazon -> BirkatHamazonDialog(onDismiss = { openDialog = null })
        BlessingDialog.TefilatHaderech -> TefilatHaderechDialog(onDismiss = { openDialog = null })
        BlessingDialog.Brachot -> BrachotDialog(onDismiss = { openDialog = null })
        null -> Unit
    }
}

@Composable
private fun BlessingHubCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    ParchmentContentCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = TzaddikColors.NavyDeep,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = TzaddikColors.TextMuted,
        )
    }
}
