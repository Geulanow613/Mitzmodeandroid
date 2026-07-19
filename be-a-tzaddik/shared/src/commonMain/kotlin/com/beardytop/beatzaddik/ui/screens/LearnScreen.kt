package com.beardytop.beatzaddik.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.beardytop.beatzaddik.domain.ChecklistDebugScenarios
import com.beardytop.beatzaddik.domain.LearnHubCatalog
import com.beardytop.beatzaddik.domain.forDebugCalendar
import com.beardytop.beatzaddik.ui.components.ChecklistDebugMenu
import com.beardytop.beatzaddik.ui.components.GoldFlourishDivider
import com.beardytop.beatzaddik.ui.components.LocalHalachicTermsEnabled
import com.beardytop.beatzaddik.ui.components.ParchmentContentCard
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.viewmodel.AppViewModel

@Composable
fun LearnScreen(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier,
) {
    val day by viewModel.dayChecklists.collectAsState()
    val upcoming by viewModel.upcomingHolidays.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val debugOverride by viewModel.checklistDebugOverride.collectAsState()
    val debugMenuVisible by viewModel.checklistDebugMenuVisible.collectAsState()
    val header = day?.header
    val displayTimezone = remember(debugOverride, profile.timezoneId) {
        val scenario = debugOverride?.scenarioId?.let { ChecklistDebugScenarios.byId(it) }
        profile.forDebugCalendar(scenario).timezoneId
    }
    val snapshot = remember(
        header?.civilDateLabel,
        header?.hebrewDateLabel,
        header?.parshaLabel,
        header?.todayOccasionLabel,
        header?.todayOccasionGuideAnchor,
        header?.omerDay,
        upcoming,
        debugOverride?.epochMillis,
    ) {
        LearnHubCatalog.snapshot(
            parshaLabel = header?.parshaLabel,
            todayOccasionLabel = header?.todayOccasionLabel,
            todayOccasionGuideAnchor = header?.todayOccasionGuideAnchor,
            omerDay = header?.omerDay,
            upcomingHolidays = upcoming,
        )
    }
    val uriHandler = LocalUriHandler.current
    val openLink: (String) -> Unit = { url -> runCatching { uriHandler.openUri(url) } }

    CompositionLocalProvider(LocalHalachicTermsEnabled provides false) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Learn",
                style = MaterialTheme.typography.headlineSmall,
                color = TzaddikColors.GoldBright,
            )
            Text(
                text = "English Torah from trusted sites — opens in your browser.",
                style = MaterialTheme.typography.bodyMedium,
                color = TzaddikColors.ParchTop.copy(alpha = 0.88f),
            )
            GoldFlourishDivider()

            if (debugMenuVisible) {
                ChecklistDebugMenu(
                    viewModel = viewModel,
                    activeOverride = debugOverride,
                    timezoneId = displayTimezone,
                )
            }

            if (debugOverride != null && header != null) {
                Text(
                    text = "Simulated: ${header.civilDateLabel} · ${header.hebrewDateLabel}" +
                        (header.parshaLabel?.let { " · Parsha $it" } ?: "") +
                        (header.todayOccasionLabel?.let { " · $it" } ?: ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = TzaddikColors.GoldBright.copy(alpha = 0.9f),
                )
            }

            LearnSectionHeader("This week")
            snapshot.thisWeek.forEach { item ->
                LearnLinkCard(item = item, onOpen = openLink)
            }

            LearnSectionHeader("Classes")
            snapshot.watch.forEach { item ->
                LearnLinkCard(item = item, onOpen = openLink)
            }

            if (snapshot.browse.isNotEmpty()) {
                LearnSectionHeader("Browse more")
                snapshot.browse.forEach { item ->
                    LearnLinkCard(item = item, onOpen = openLink)
                }
            }

            Text(
                text = "All lessons belong to their publishers. Mitz Mode only links out — nothing is copied or hosted here.",
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.ParchTop.copy(alpha = 0.65f),
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
            )
        }
    }
}

/** Section titles on the starry navy background — light gold, not navy-on-navy. */
@Composable
private fun LearnSectionHeader(title: String) {
    Column(modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(18.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(TzaddikColors.GoldBright, TzaddikColors.GoldBorder),
                        ),
                        shape = RoundedCornerShape(2.dp),
                    ),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp,
                ),
                color = TzaddikColors.GoldBright,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .height(0.8.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(TzaddikColors.GoldBorder.copy(alpha = 0.55f), Color.Transparent),
                    ),
                ),
        )
    }
}

@Composable
private fun LearnLinkCard(
    item: LearnHubCatalog.LinkItem,
    onOpen: (String) -> Unit,
    emphasized: Boolean = false,
) {
    ParchmentContentCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen(item.url) },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (emphasized) {
                    Text(
                        text = "FEATURED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                        ),
                        color = TzaddikColors.GoldBright,
                    )
                }
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TzaddikColors.NavyDeep,
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TzaddikColors.TextMuted,
                )
                Text(
                    text = "${item.sourceLabel} ↗",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = TzaddikColors.NavyMid,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                contentDescription = "Open in browser",
                tint = TzaddikColors.GoldBright.copy(alpha = 0.85f),
                modifier = Modifier.size(22.dp),
            )
        }
    }
}
