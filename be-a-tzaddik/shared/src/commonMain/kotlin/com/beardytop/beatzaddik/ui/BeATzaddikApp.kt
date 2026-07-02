package com.beardytop.beatzaddik.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.beatzaddik.domain.AppDisclaimer
import com.beardytop.beatzaddik.domain.Gender
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.platform.PlatformBackHandler
import com.beardytop.beatzaddik.platform.exitApplication
import com.beardytop.beatzaddik.ui.components.AppText
import com.beardytop.beatzaddik.ui.components.GoldButton
import com.beardytop.beatzaddik.ui.components.HalachicTermOverlay
import com.beardytop.beatzaddik.ui.components.HolyLightBackground
import com.beardytop.beatzaddik.ui.components.LocationPermissionDialog
import com.beardytop.beatzaddik.ui.components.MitzModeBottomNav
import com.beardytop.beatzaddik.ui.components.ParchmentDialog
import com.beardytop.beatzaddik.ui.components.ParchmentTextButton
import com.beardytop.beatzaddik.ui.components.StarryScaffold
import com.beardytop.beatzaddik.ui.components.rememberCandlelightRewardController
import com.beardytop.beatzaddik.ui.components.rememberHolyFlashController
import com.beardytop.beatzaddik.ui.screens.AboutScreen
import com.beardytop.beatzaddik.ui.screens.OnboardingScreen
import com.beardytop.beatzaddik.ui.screens.SettingsScreen
import com.beardytop.beatzaddik.ui.screens.ShabbatRestScreen
import com.beardytop.beatzaddik.ui.screens.SplashScreen
import com.beardytop.beatzaddik.ui.screens.TimerScreen
import com.beardytop.beatzaddik.ui.screens.TodayScreen
import com.beardytop.beatzaddik.ui.theme.TzaddikTheme
import com.beardytop.beatzaddik.viewmodel.AppViewModel

@Composable
fun BeATzaddikApp(
    viewModel: AppViewModel,
    embeddedMode: Boolean = false,
    onRequestClose: () -> Unit = {},
    returnToMainIcon: (@Composable () -> Unit)? = null
) {
    val embeddedTitle = AppDisclaimer.EMBEDDED_APP_TITLE
    val profile by viewModel.profile.collectAsState()
    val prefsLoaded by viewModel.prefsLoaded.collectAsState()
    val showDisclaimer by viewModel.showDisclaimerDialog.collectAsState()
    val showLocationPermission by viewModel.showLocationPermissionDialog.collectAsState()
    val electronicsRest by viewModel.electronicsRest.collectAsState()
    var splashDone by rememberSaveable { mutableStateOf(embeddedMode) }

    TzaddikTheme(textScale = profile.textScale) {
        HalachicTermOverlay {
        if (!splashDone) {
            PlatformBackHandler {
                if (embeddedMode) onRequestClose()
            }
            SplashScreen(
                onFinished = { splashDone = true },
                isFemale = profile.gender == Gender.FEMALE,
                titleOverride = if (embeddedMode) embeddedTitle else null
            )
            return@HalachicTermOverlay
        }

        if (showDisclaimer) {
            PlatformBackHandler {
                if (embeddedMode) onRequestClose()
            }
            Box(Modifier.fillMaxSize()) {
                HolyLightBackground(Modifier.fillMaxSize())
                ParchmentDialog(
                    onDismiss = { },
                    title = AppDisclaimer.TITLE,
                    showCloseIcon = false,
                    confirmButton = {
                        GoldButton(onClick = { viewModel.acknowledgeDisclaimer() }, text = "Begin")
                    }
                ) {
                    DisclaimerBody(embeddedMode = embeddedMode)
                }
            }
            return@HalachicTermOverlay
        }
        if (!prefsLoaded) {
            Box(Modifier.fillMaxSize()) {
                HolyLightBackground(Modifier.fillMaxSize())
            }
            return@HalachicTermOverlay
        }
        when {
            !profile.onboardingComplete -> {
                Box(Modifier.fillMaxSize()) {
                    HolyLightBackground(Modifier.fillMaxSize())
                    OnboardingScreen(
                        viewModel = viewModel,
                        appTitle = if (embeddedMode) embeddedTitle else "Be a Tzaddik",
                        onBackFromFirstStep = if (embeddedMode) onRequestClose else null
                    )
                }
            }
            electronicsRest != null -> {
                var showRestSettings by rememberSaveable { mutableStateOf(false) }
                if (showRestSettings) {
                    PlatformBackHandler { showRestSettings = false }
                    Box(Modifier.fillMaxSize()) {
                        HolyLightBackground(Modifier.fillMaxSize())
                        Column(Modifier.fillMaxSize()) {
                            AppText(
                                "‹ Back",
                                color = TzaddikColors.GoldBright,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier
                                    .clickable { showRestSettings = false }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                            )
                            SettingsScreen(viewModel = viewModel)
                        }
                    }
                } else {
                    PlatformBackHandler { /* stay on rest screen */ }
                    ShabbatRestScreen(
                        period = electronicsRest!!,
                        timezoneId = profile.timezoneId,
                        onOpenSettings = { showRestSettings = true },
                    )
                }
            }
            else -> MainShell(
                viewModel = viewModel,
                embeddedMode = embeddedMode,
                onRequestClose = onRequestClose,
                returnToMainIcon = returnToMainIcon
            )
        }
        if (showLocationPermission) {
            PlatformBackHandler {
                viewModel.dismissLocationPermissionDialog()
            }
            LocationPermissionDialog(
                onOpenSettings = {
                    viewModel.dismissLocationPermissionDialog()
                    viewModel.openAppSettings()
                },
                onDismiss = { viewModel.dismissLocationPermissionDialog() }
            )
        }
        }
    }
}

@Composable
private fun DisclaimerBody(embeddedMode: Boolean) {
    var fontScale by remember { mutableFloatStateOf(1f) }
    val scrollState = rememberScrollState()
    val scaled: (TextStyle) -> TextStyle = { style ->
        style.copy(fontSize = (style.fontSize.value * fontScale).sp)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppText(
                "Text size",
                enableTerms = false,
                style = MaterialTheme.typography.labelSmall,
                color = TzaddikColors.TextMuted
            )
            TextButton(
                onClick = { fontScale = (fontScale - 0.1f).coerceIn(0.85f, 2f) },
                enabled = fontScale > 0.85f
            ) {
                Text("A−", color = TzaddikColors.NavyMid)
            }
            TextButton(
                onClick = { fontScale = (fontScale + 0.1f).coerceIn(0.85f, 2f) },
                enabled = fontScale < 2f
            ) {
                Text("A+", color = TzaddikColors.NavyMid)
            }
        }
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val scrollMax = if (maxHeight < 9000.dp) {
                (maxHeight * 0.52f).coerceIn(260.dp, 480.dp)
            } else {
                440.dp
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = scrollMax)
                    .verticalScroll(scrollState)
            ) {
                AppText(
                    AppDisclaimer.WELCOME_HEADLINE,
                    enableTerms = false,
                    style = scaled(MaterialTheme.typography.titleLarge),
                    color = TzaddikColors.NavyDeep,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(14.dp))
                AppText(
                    AppDisclaimer.welcomeIntro(embeddedMode),
                    enableTerms = false,
                    style = scaled(MaterialTheme.typography.bodyMedium),
                    color = TzaddikColors.TextBrown,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
                AppText(
                    AppDisclaimer.STARTUP_BODY,
                    enableTerms = false,
                    style = scaled(MaterialTheme.typography.bodyMedium),
                    color = TzaddikColors.TextBrown,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun MainShell(
    viewModel: AppViewModel,
    embeddedMode: Boolean = false,
    onRequestClose: () -> Unit = {},
    returnToMainIcon: (@Composable () -> Unit)? = null
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var scrollSettingsToKashrut by remember { mutableStateOf(false) }
    val holyFlash = rememberHolyFlashController()
    val candlelightReward = rememberCandlelightRewardController()
    var showExitConfirm by remember { mutableStateOf(false) }

    val settingsTabIndex = if (embeddedMode) 3 else 2
    val aboutTabIndex = if (embeddedMode) 4 else 3
    val returnMainTabIndex = if (embeddedMode) 2 else -1
    val embeddedTitle = AppDisclaimer.EMBEDDED_APP_TITLE
    val appTitle = if (embeddedMode) embeddedTitle else "Be a Tzaddik"

    LaunchedEffect(Unit) {
        viewModel.candlelightReward.collect { candlelightReward.trigger() }
    }

    // Registered first; inner screens (guide, dialogs) register later and take priority.
    PlatformBackHandler(enabled = !showExitConfirm) {
        if (tab != 0) {
            tab = 0
        } else {
            showExitConfirm = true
        }
    }

    if (showExitConfirm) {
        PlatformBackHandler { showExitConfirm = false }
        ParchmentDialog(
            onDismiss = { showExitConfirm = false },
            title = if (embeddedMode) "Return to Mitz Mode?" else "Exit app?",
            showCloseIcon = false,
            dismissButton = {
                ParchmentTextButton(onClick = { showExitConfirm = false }, text = "Cancel")
            },
            confirmButton = {
                GoldButton(
                    onClick = {
                        if (embeddedMode) onRequestClose() else exitApplication()
                    },
                    text = if (embeddedMode) "Back" else "Exit"
                )
            }
        ) {
            AppText(
                if (embeddedMode) {
                    "Are you sure you want to leave the checklist?"
                } else {
                    "Are you sure you want to quit?"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = TzaddikColors.TextBrown
            )
        }
    }

    StarryScaffold(
        holyFlash = holyFlash,
        candlelightReward = candlelightReward,
        bottomBar = {
                MitzModeBottomNav(
                    selectedTab = tab,
                    onTabSelected = { selected ->
                        if (embeddedMode && selected == returnMainTabIndex) {
                            onRequestClose()
                        } else {
                            tab = selected
                        }
                    },
                    tabs = buildList {
                        add("Today" to { Icon(Icons.Default.CheckCircle, null) })
                        add("Timer" to { Icon(Icons.Default.Timer, null) })
                        if (embeddedMode) {
                            add("" to {
                                if (returnToMainIcon != null) {
                                    returnToMainIcon()
                                } else {
                                    Icon(Icons.Default.Home, null)
                                }
                            })
                        }
                        add("Settings" to { Icon(Icons.Default.Settings, null) })
                        add("About" to { Icon(Icons.Default.Info, null) })
                    }
                )
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (tab) {
                0 -> TodayScreen(
                    viewModel, holyFlash,
                    onOpenTimer = { tab = 1 },
                    onOpenSettings = { tab = settingsTabIndex },
                )
                1 -> TimerScreen(
                    viewModel = viewModel,
                    onOpenKashrutSettings = {
                        scrollSettingsToKashrut = true
                        tab = settingsTabIndex
                    }
                )
                settingsTabIndex -> SettingsScreen(
                    viewModel = viewModel,
                    scrollToKashrut = scrollSettingsToKashrut,
                    onScrollTargetConsumed = { scrollSettingsToKashrut = false }
                )
                aboutTabIndex -> AboutScreen(appTitle = appTitle)
            }
        }
    }
}
