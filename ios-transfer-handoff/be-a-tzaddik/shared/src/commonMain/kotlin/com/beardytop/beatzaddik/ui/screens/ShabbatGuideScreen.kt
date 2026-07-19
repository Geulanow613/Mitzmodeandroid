package com.beardytop.beatzaddik.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.beatzaddik.platform.PlatformBackHandler
import com.beardytop.beatzaddik.ui.components.AppText
import com.beardytop.beatzaddik.ui.components.GoldFlourishDivider
import com.beardytop.beatzaddik.ui.components.HalachicClickableText
import com.beardytop.beatzaddik.ui.components.HolyLightFlashOverlay
import com.beardytop.beatzaddik.ui.components.rememberHolyFlashController
import com.beardytop.beatzaddik.ui.components.LocalHalachicTermsEnabled
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import androidx.compose.runtime.CompositionLocalProvider
import kotlinx.datetime.Clock

// ── Navigation model ─────────────────────────────────────────────────────────

sealed class ShabbatPage {
    data object Hub : ShabbatPage()
    data object ShabbatYomTovPage : ShabbatPage()
    data class TermDetail(val topicId: String) : ShabbatPage()
    data object MelachotList : ShabbatPage()
    data class MelachaDetail(val melachaId: String) : ShabbatPage()
    data object HolidaysList : ShabbatPage()
    data class HolidayDetail(val holidayId: String) : ShabbatPage()
}

// ── Root composable ───────────────────────────────────────────────────────────

@Composable
fun ShabbatGuideScreen(
    initialAnchor: String? = null,
    onDismiss: () -> Unit,
    onToggleChecklistDebugMenu: (() -> Unit)? = null,
) {
    val backStack = remember { mutableStateListOf<ShabbatPage>(ShabbatPage.Hub) }
    val holyFlash = rememberHolyFlashController()

    // Jump to the right page for the requested anchor
    LaunchedEffect(initialAnchor) {
        if (initialAnchor == null) return@LaunchedEffect
        val page: ShabbatPage? = when {
            initialAnchor == "melachot" || initialAnchor == "melachot_list" ->
                ShabbatPage.MelachotList
            initialAnchor == "shabbat_overview" || initialAnchor == "yom_tov" ->
                ShabbatPage.ShabbatYomTovPage
            ShabbatGuideData.glossary.any { it.id == initialAnchor } ->
                ShabbatPage.TermDetail(initialAnchor)
            ShabbatGuideData.melachot.any { it.id == initialAnchor } ->
                ShabbatPage.MelachaDetail(initialAnchor)
            ShabbatGuideData.holidays.any { it.id == initialAnchor } ->
                ShabbatPage.HolidayDetail(initialAnchor)
            else -> null
        }
        page?.let { backStack.add(it) }
    }

    val current = backStack.last()
    val canGoBack = backStack.size > 1

    fun goBack() {
        if (canGoBack) backStack.removeAt(backStack.lastIndex) else onDismiss()
    }

    PlatformBackHandler(onBack = ::goBack)

    CompositionLocalProvider(LocalHalachicTermsEnabled provides false) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(TzaddikColors.NavyDeep, TzaddikColors.NavyMid))
            )
    ) {
        // Page content
        when (val page = current) {
            ShabbatPage.Hub -> HubPage(
                onOpenShabbatYomTov = { backStack.add(ShabbatPage.ShabbatYomTovPage) },
                onOpenTerm = { id -> backStack.add(ShabbatPage.TermDetail(id)) },
                onOpenMelachotList = { backStack.add(ShabbatPage.MelachotList) },
                onOpenHolidaysList = { backStack.add(ShabbatPage.HolidaysList) }
            )
            ShabbatPage.ShabbatYomTovPage -> ShabbatYomTovDetailPage(
                onOpenMelachotList = { backStack.add(ShabbatPage.MelachotList) }
            )
            is ShabbatPage.TermDetail -> {
                val topic = ShabbatGuideData.glossary.find { it.id == page.topicId }
                if (topic != null) TermDetailPage(topic)
            }
            ShabbatPage.MelachotList -> MelachotListPage(
                onOpenMelacha = { id -> backStack.add(ShabbatPage.MelachaDetail(id)) }
            )
            is ShabbatPage.MelachaDetail -> {
                val m = ShabbatGuideData.melachot.find { it.id == page.melachaId }
                val index = ShabbatGuideData.melachot.indexOfFirst { it.id == page.melachaId }
                if (m != null) MelachaDetailPage(
                    number = index + 1,
                    topic = m,
                    onToggleChecklistDebugMenu = onToggleChecklistDebugMenu,
                    onHolyFlash = holyFlash::trigger,
                )
            }
            ShabbatPage.HolidaysList -> HolidaysListPage(
                onOpenHoliday = { id -> backStack.add(ShabbatPage.HolidayDetail(id)) }
            )
            is ShabbatPage.HolidayDetail -> {
                val h = ShabbatGuideData.holidays.find { it.id == page.holidayId }
                if (h != null) HolidayDetailPage(h)
            }
        }

        // Floating top-right button: back OR close
        FloatingNavButton(
            isClose = !canGoBack,
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 48.dp, end = 12.dp),
            onClick = ::goBack
        )
        HolyLightFlashOverlay(
            visible = holyFlash.visible,
            alpha = holyFlash.alpha,
            sweepT = holyFlash.sweepT,
            variant = holyFlash.displayedVariant,
            modifier = Modifier.fillMaxSize(),
        )
    }
    }
}

// ── Floating button ───────────────────────────────────────────────────────────

@Composable
private fun FloatingNavButton(isClose: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(TzaddikColors.NavyDeep.copy(alpha = 0.82f))
            .border(1.dp, TzaddikColors.GoldBright.copy(alpha = 0.3f), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isClose) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = if (isClose) "Close" else "Back",
            tint = TzaddikColors.ParchTop,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ── Hub page ─────────────────────────────────────────────────────────────────

@Composable
private fun shabbatGuideContentPadding(top: Dp): PaddingValues {
    val safeBottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val navBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val insetBottom = maxOf(safeBottom, navBottom)
    // Full-screen dialogs sometimes report zero insets — reserve gesture-nav space.
    val bottom = 28.dp + if (insetBottom > 0.dp) insetBottom else 56.dp
    return PaddingValues(
        top = top,
        start = 16.dp,
        end = 16.dp,
        bottom = bottom,
    )
}

@Composable
private fun HubPage(
    onOpenShabbatYomTov: () -> Unit,
    onOpenTerm: (String) -> Unit,
    onOpenMelachotList: () -> Unit,
    onOpenHolidaysList: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = shabbatGuideContentPadding(top = 48.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "שַׁבָּת",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp, fontFamily = FontFamily.Serif),
                    color = TzaddikColors.GoldBright
                )
                AppText(
                    "Shabbat & Holidays",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = TzaddikColors.ParchTop,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                AppText("Tap any topic to read more",
                    style = MaterialTheme.typography.bodySmall,
                    color = TzaddikColors.GoldBright.copy(alpha = 0.6f))
                Spacer(Modifier.height(12.dp))
                GoldFlourishDivider(widthFraction = 0.45f)
            }
        }

        // Shabbat & Yom Tov overview card
        item {
            HubSectionLabel("Overview")
        }
        item {
            HubCard(
                title = "Shabbat & Yom Tov",
                subtitle = "What they are, key differences, and the 39 melachot",
                onClick = onOpenShabbatYomTov
            )
        }

        // Key terms
        item { HubSectionLabel("Key Terms") }
        items(ShabbatGuideData.glossary.filter {
            it.id !in listOf("shabbat_overview", "yom_tov")
        }) { term ->
            HubCard(
                title = term.title,
                subtitle = term.hebrewTitle,
                onClick = { onOpenTerm(term.id) }
            )
        }

        // Jewish Holidays
        item { HubSectionLabel("Jewish Holidays") }
        item {
            HubCard(
                title = "All holidays & fasts",
                subtitle = "Major & minor holidays, Yom Tov, and fast days",
                onClick = onOpenHolidaysList
            )
        }

        // 39 Melachot
        item { HubSectionLabel("The 39 Melachot") }
        item {
            HubCard(
                title = "Browse all 39 melachot",
                subtitle = "The 39 categories of creative labor forbidden on Shabbat",
                onClick = onOpenMelachotList
            )
        }
    }
}

@Composable
private fun HubSectionLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.width(3.dp).height(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(TzaddikColors.GoldBright)
        )
        Spacer(Modifier.width(8.dp))
        AppText(
            text,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.GoldBright.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun HubCard(title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(TzaddikColors.NavyMid.copy(alpha = 0.55f))
            .border(1.dp, TzaddikColors.GoldBright.copy(alpha = 0.18f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            AppText(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = TzaddikColors.ParchTop
            )
            if (subtitle.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                val subtitleStyle = MaterialTheme.typography.bodySmall
                val subtitleColor = TzaddikColors.ParchTop.copy(alpha = 0.6f)
                if (subtitle.any { it in 'A'..'Z' || it in 'a'..'z' }) {
                    AppText(subtitle, style = subtitleStyle, color = subtitleColor)
                } else {
                    Text(subtitle, style = subtitleStyle, color = subtitleColor)
                }
            }
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
            tint = TzaddikColors.GoldBright.copy(alpha = 0.55f),
            modifier = Modifier.size(20.dp))
    }
}

// ── Shabbat & Yom Tov detail page ────────────────────────────────────────────

@Composable
private fun ShabbatYomTovDetailPage(onOpenMelachotList: () -> Unit) {
    val shabbatTopic = ShabbatGuideData.glossary.find { it.id == "shabbat_overview" }
    val yomTovTopic = ShabbatGuideData.glossary.find { it.id == "yom_tov" }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = shabbatGuideContentPadding(top = 56.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            PageTitle(title = "Shabbat & Yom Tov", hebrew = "שַׁבָּת וְיוֹם טוֹב")
        }

        shabbatTopic?.let {
            item { SubHeading("What is Shabbat?") }
            item { BodyCard(it.body) }
        }

        yomTovTopic?.let {
            item { SubHeading("What is Yom Tov?") }
            item { BodyCard(it.body) }
        }

        item { SubHeading("Key Differences") }
        item {
            BodyCard("The list below covers only the differences that are clearly and directly stated in classical sources. Yom Tov law has many nuances — always ask your rabbi for specific situations.")
            Spacer(Modifier.height(4.dp))
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Brush.verticalGradient(listOf(TzaddikColors.ParchTop, TzaddikColors.ParchBase)))
                    .border(1.dp, TzaddikColors.GoldBorder.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TzaddikColors.NavyDeep.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    AppText(
                        "Activity",
                        modifier = Modifier.weight(1.2f),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TzaddikColors.NavyDeep
                    )
                    AppText(
                        "Shabbat",
                        modifier = Modifier.weight(0.9f),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TzaddikColors.NavyDeep
                    )
                    AppText(
                        "Yom Tov",
                        modifier = Modifier.weight(1.3f),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TzaddikColors.NavyDeep
                    )
                }
                HorizontalDivider(color = TzaddikColors.GoldBorder.copy(alpha = 0.28f))
                ShabbatGuideData.shabbatYomTovComparison.forEachIndexed { i, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (i % 2 == 0) TzaddikColors.ParchTop
                                else TzaddikColors.ParchMid.copy(alpha = 0.5f)
                            )
                            .padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        AppText(
                            row.activity,
                            modifier = Modifier.weight(1.2f),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = TzaddikColors.NavyDeep
                        )
                        AppText(
                            row.shabbat,
                            modifier = Modifier.weight(0.9f),
                            style = MaterialTheme.typography.bodySmall,
                            color = TzaddikColors.TextBrown
                        )
                        AppText(
                            row.yomTov,
                            modifier = Modifier.weight(1.3f),
                            style = MaterialTheme.typography.bodySmall,
                            color = TzaddikColors.TextBrown
                        )
                    }
                    if (i < ShabbatGuideData.shabbatYomTovComparison.lastIndex) {
                        HorizontalDivider(color = TzaddikColors.GoldBorder.copy(alpha = 0.12f))
                    }
                }
            }
        }

        item { SubHeading("The 39 Melachot") }
        item {
            BodyCard(
                "The 39 categories of creative labor (melachot) forbidden on Shabbat are derived from the types of work performed in constructing the Mishkan (Tabernacle) in the desert. The Talmud lists them in Tractate Shabbat 73a.\n\nEach melacha has practical applications in modern life, from cooking and writing to carrying and electricity. Tap below to browse all 39."
            )
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TzaddikColors.GoldBorder.copy(alpha = 0.18f))
                    .border(1.dp, TzaddikColors.GoldBright.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .clickable(onClick = onOpenMelachotList)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AppText(
                    "Browse the 39 Melachot",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TzaddikColors.GoldBright
                )
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                    tint = TzaddikColors.GoldBright, modifier = Modifier.size(20.dp))
            }
        }
        item { DisclaimerNote() }
    }
}

// ── Term detail page ──────────────────────────────────────────────────────────

@Composable
private fun TermDetailPage(topic: GuideTopic) {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = shabbatGuideContentPadding(top = 56.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PageTitle(title = topic.title, hebrew = topic.hebrewTitle)
        }
        item {
            BodyCard(topic.body)
        }
        topic.learnMoreUrl?.let { url ->
            item {
                AppText(
                    "Learn more ↗",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TzaddikColors.GoldBright.copy(alpha = 0.8f),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { uriHandler.openUri(url) }
                )
            }
        }
        item { DisclaimerNote() }
    }
}

// ── Melachot list page ────────────────────────────────────────────────────────

@Composable
private fun MelachotListPage(onOpenMelacha: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = shabbatGuideContentPadding(top = 56.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            PageTitle(title = "The 39 Melachot", hebrew = "ל\"ט מְלָאכוֹת")
        }
        item {
            BodyCard(
                "The 39 categories of creative labor forbidden on Shabbat. Tap any to read a full explanation."
            )
            Spacer(Modifier.height(4.dp))
        }
        items(ShabbatGuideData.melachot, key = { it.id }) { melacha ->
            val index = ShabbatGuideData.melachot.indexOf(melacha)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TzaddikColors.NavyMid.copy(alpha = 0.5f))
                    .border(1.dp, TzaddikColors.GoldBright.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .clickable { onOpenMelacha(melacha.id) }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Number badge
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(TzaddikColors.GoldBright.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "${index + 1}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TzaddikColors.GoldBright
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    AppText(
                        melacha.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = TzaddikColors.ParchTop
                    )
                    if (melacha.hebrewTitle.isNotBlank()) {
                        Text(
                            melacha.hebrewTitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Serif
                            ),
                            color = TzaddikColors.GoldBright.copy(alpha = 0.5f)
                        )
                    }
                }
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null,
                    tint = TzaddikColors.GoldBright.copy(alpha = 0.45f),
                    modifier = Modifier.size(18.dp))
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

// ── Melacha detail page ───────────────────────────────────────────────────────

@Composable
private fun MelachaDetailPage(
    number: Int,
    topic: GuideTopic,
    onToggleChecklistDebugMenu: (() -> Unit)? = null,
    onHolyFlash: () -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current
    val weavingTapTracker = remember { OregWeavingTapTracker() }
    val onWeavingTap = if (topic.id == "oreg" && onToggleChecklistDebugMenu != null) {
        {
            if (weavingTapTracker.registerTap()) {
                onToggleChecklistDebugMenu()
                onHolyFlash()
            }
        }
    } else {
        null
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = shabbatGuideContentPadding(top = 56.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            // Number badge + title
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(TzaddikColors.GoldBorder.copy(alpha = 0.22f))
                        .border(1.dp, TzaddikColors.GoldBright.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$number",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TzaddikColors.GoldBright
                    )
                }
                Spacer(Modifier.height(10.dp))
                PageTitle(
                    title = topic.title,
                    hebrew = topic.hebrewTitle,
                    onSubtitleTap = onWeavingTap,
                )
            }
        }
        item {
            BodyCard(topic.body)
        }
        topic.learnMoreUrl?.let { url ->
            item {
                AppText(
                    "Learn more ↗",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TzaddikColors.GoldBright.copy(alpha = 0.8f),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { uriHandler.openUri(url) }
                )
            }
        }
        item { DisclaimerNote() }
    }
}

// ── Holidays list page ────────────────────────────────────────────────────────

@Composable
private fun HolidaysListPage(onOpenHoliday: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = shabbatGuideContentPadding(top = 56.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            PageTitle(title = "Jewish Holidays", hebrew = "יָמִים טוֹבִים")
        }
        item {
            BodyCard("Tap any holiday to learn more about it — its significance, observances, and customs.")
            Spacer(Modifier.height(4.dp))
        }

        val major = ShabbatGuideData.holidays.filter { it.id in ShabbatGuideData.majorHolidayIds }
        val minor = ShabbatGuideData.holidays.filter { it.id !in ShabbatGuideData.majorHolidayIds }

        if (major.isNotEmpty()) {
            item { HubSectionLabel("Major Holidays & Yom Tov") }
            items(major, key = { it.id }) { holiday ->
                HubCard(title = holiday.title, subtitle = holiday.hebrewTitle, onClick = { onOpenHoliday(holiday.id) })
            }
        }
        if (minor.isNotEmpty()) {
            item { HubSectionLabel("Minor Holidays & Fast Days") }
            items(minor, key = { it.id }) { holiday ->
                HubCard(title = holiday.title, subtitle = holiday.hebrewTitle, onClick = { onOpenHoliday(holiday.id) })
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

// ── Holiday detail page ───────────────────────────────────────────────────────

@Composable
private fun HolidayDetailPage(topic: GuideTopic) {
    val uriHandler = LocalUriHandler.current
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = shabbatGuideContentPadding(top = 56.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { PageTitle(title = topic.title, hebrew = topic.hebrewTitle) }
        item { BodyCard(topic.body) }
        topic.learnMoreUrl?.let { url ->
            item {
                AppText(
                    "Learn more ↗",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TzaddikColors.GoldBright.copy(alpha = 0.8f),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { uriHandler.openUri(url) }
                )
            }
        }
        item { DisclaimerNote() }
    }
}

// ── Shared components ─────────────────────────────────────────────────────────

private const val OREG_DEBUG_TAP_COUNT = 10
private const val OREG_DEBUG_MAX_GAP_MS = 500L

private class OregWeavingTapTracker {
    private var tapCount = 0
    private var lastTapMs = 0L

    fun registerTap(): Boolean {
        val now = Clock.System.now().toEpochMilliseconds()
        tapCount = if (lastTapMs == 0L || now - lastTapMs <= OREG_DEBUG_MAX_GAP_MS) {
            tapCount + 1
        } else {
            1
        }
        lastTapMs = now
        if (tapCount >= OREG_DEBUG_TAP_COUNT) {
            tapCount = 0
            lastTapMs = 0L
            return true
        }
        return false
    }
}

@Composable
private fun PageTitle(
    title: String,
    hebrew: String,
    onSubtitleTap: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hebrew.isNotBlank()) {
            Text(
                hebrew,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                ),
                color = TzaddikColors.GoldBright.copy(alpha = 0.75f)
            )
            Spacer(Modifier.height(4.dp))
        }
        if (onSubtitleTap != null && " — " in title) {
            val parts = title.split(" — ", limit = 2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AppText(
                    "${parts[0]} — ",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = TzaddikColors.ParchTop,
                    textAlign = TextAlign.Center,
                )
                AppText(
                    parts[1],
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = TzaddikColors.ParchTop,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable(onClick = onSubtitleTap),
                )
            }
        } else {
            AppText(
                title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.ParchTop,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(10.dp))
        GoldFlourishDivider(widthFraction = 0.4f)
    }
}

@Composable
private fun SubHeading(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Box(
            Modifier.width(3.dp).height(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(TzaddikColors.GoldBright)
        )
        Spacer(Modifier.width(8.dp))
        AppText(
            text,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.GoldBright
        )
    }
}

@Composable
private fun BodyCard(text: String) {
    val translated = rememberAppTranslatedText(text)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.verticalGradient(listOf(TzaddikColors.ParchTop, TzaddikColors.ParchBase)))
            .border(1.dp, TzaddikColors.GoldBorder.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(16.dp)
    ) {
        translated.split("\n\n").forEach { paragraph ->
            val lines = paragraph.lines()
            val isBulletBlock = lines.any { it.trim().startsWith("•") }
            if (isBulletBlock) {
                lines.forEach { line ->
                    val t = line.trim()
                    if (t.startsWith("•")) {
                        Row(Modifier.padding(bottom = 4.dp)) {
                            Text(
                                "•",
                                color = TzaddikColors.GoldBorder,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(end = 6.dp, top = 1.dp)
                            )
                            HalachicClickableText(
                                text = t.removePrefix("•").trim(),
                                style = MaterialTheme.typography.bodySmall,
                                color = TzaddikColors.TextBrown,
                                enableTerms = false,
                            )
                        }
                    } else if (t.isNotBlank()) {
                        HalachicClickableText(
                            text = t,
                            style = MaterialTheme.typography.bodySmall,
                            color = TzaddikColors.TextBrown,
                            enableTerms = false,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                    }
                }
            } else {
                HalachicClickableText(
                    text = paragraph.trim(),
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = TzaddikColors.TextBrown,
                    enableTerms = false,
                    modifier = Modifier.padding(bottom = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun DisclaimerNote() {
    AppText(
        "Always consult a qualified Orthodox rabbi for personal halachic guidance.",
        style = MaterialTheme.typography.labelSmall,
        color = TzaddikColors.GoldBright.copy(alpha = 0.45f),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 4.dp)
    )
}
