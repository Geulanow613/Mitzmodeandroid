package com.beardytop.beatzaddik.ui.screens

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
import com.beardytop.beatzaddik.ui.components.AppText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.beardytop.beatzaddik.domain.AppDisclaimer
import com.beardytop.beatzaddik.ui.components.AboutContent
import com.beardytop.beatzaddik.ui.components.GoldFlourishDivider
import com.beardytop.beatzaddik.ui.components.ParchmentContentCard
import com.beardytop.beatzaddik.ui.theme.TzaddikColors

@Composable
fun AboutScreen(
    appTitle: String = "Be a Tzaddik",
    modifier: Modifier = Modifier,
    onWhatsAMitzvah: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        AppText(
            "About",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.GoldBright,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        )

        ParchmentContentCard {
            AppText(
                appTitle,
                enableTerms = false,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.NavyDeep
            )
            Spacer(Modifier.height(8.dp))
            GoldFlourishDivider()
            Spacer(Modifier.height(12.dp))
            AppText(
                AppDisclaimer.TITLE,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TzaddikColors.NavyDeep
            )
            Spacer(Modifier.height(8.dp))
            AboutContent(
                modifier = Modifier.fillMaxWidth(),
                onWhatsAMitzvah = onWhatsAMitzvah,
            )
        }
    }
}
