package com.beardytop.beatzaddik.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import beatzaddik.shared.generated.resources.Res
import beatzaddik.shared.generated.resources.mitzvahbutton
import com.beardytop.beatzaddik.AppDependencies
import com.beardytop.beatzaddik.domain.AppDisclaimer
import com.beardytop.beatzaddik.domain.AppMode
import com.beardytop.beatzaddik.domain.Gender
import com.beardytop.beatzaddik.domain.MitzvahDefinitionText
import com.beardytop.beatzaddik.mitzmode.UnifiedMitzModeSession
import com.beardytop.beatzaddik.navigation.AppNavigation
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.platform.PlatformBackHandler
import com.beardytop.beatzaddik.platform.exitApplication
import com.beardytop.beatzaddik.ui.components.AppRatingPromptDialog
import com.beardytop.beatzaddik.ui.components.AppText
import com.beardytop.beatzaddik.ui.components.FinalRewardVideoOverlay
import com.beardytop.beatzaddik.ui.components.GoldButton
import com.beardytop.beatzaddik.ui.components.HalachicTermOverlay
import com.beardytop.beatzaddik.ui.components.LocalRegisterChecklistDebugToggle
import com.beardytop.beatzaddik.ui.components.HolyLightBackground
import com.beardytop.beatzaddik.ui.components.LearnWebScreen
import com.beardytop.beatzaddik.ui.components.LocationPermissionDialog
import com.beardytop.beatzaddik.ui.components.NotificationPermissionDialog
import com.beardytop.beatzaddik.ui.components.MitzModeBottomNav
import com.beardytop.beatzaddik.ui.components.MitzModeCountPillOverlay
import com.beardytop.beatzaddik.ui.components.MitzvahGeneratorDialog
import com.beardytop.beatzaddik.ui.components.ParchmentDialog
import com.beardytop.beatzaddik.ui.components.ParchmentTextButton
import com.beardytop.beatzaddik.ui.components.ProvideInAppBrowser
import com.beardytop.beatzaddik.ui.components.StarryScaffold
import com.beardytop.beatzaddik.ui.components.rememberCandlelightRewardController
import com.beardytop.beatzaddik.ui.components.rememberHolyFlashController
import com.beardytop.beatzaddik.ui.screens.AboutScreen
import com.beardytop.beatzaddik.ui.screens.BlessingsScreen
import com.beardytop.beatzaddik.ui.screens.OnboardingScreen
import com.beardytop.beatzaddik.ui.screens.SettingsScreen
import com.beardytop.beatzaddik.ui.screens.ShabbatRestScreen
import com.beardytop.beatzaddik.ui.screens.SplashScreen
import com.beardytop.beatzaddik.ui.screens.StatusScreen
import com.beardytop.beatzaddik.ui.screens.TimerScreen
import com.beardytop.beatzaddik.ui.screens.TodayScreen
import com.beardytop.beatzaddik.ui.theme.TzaddikTheme
import com.beardytop.beatzaddik.ui.tour.LocalTourTargetReporter
import com.beardytop.beatzaddik.ui.tour.MitzModeAppTourOverlay
import com.beardytop.beatzaddik.ui.tour.TourTarget
import com.beardytop.beatzaddik.ui.tour.TourTargetReporter
import com.beardytop.beatzaddik.ui.tour.mitzModeAppTourSteps
import com.beardytop.beatzaddik.viewmodel.AppViewModel
import org.jetbrains.compose.resources.painterResource

@Composable
fun BeATzaddikApp(
    viewModel: AppViewModel,
    deps: AppDependencies,
    appMode: AppMode = AppMode.Standalone,
    onRequestClose: () -> Unit = {},
    returnToMainIcon: (@Composable () -> Unit)? = null,
    externalMitzvotCount: Int = 0,
    externalChecklistChecked: ((itemId: String, title: String, dayKey: String) -> Unit)? = null,
) {
    val embeddedMode = appMode == AppMode.Embedded
    val unifiedMode = appMode == AppMode.Unified
    val embeddedTitle = AppDisclaimer.EMBEDDED_APP_TITLE
    val profile by viewModel.profile.collectAsState()
    val prefsLoaded by viewModel.prefsLoaded.collectAsState()
    val showDisclaimer by viewModel.showDisclaimerDialog.collectAsState()
    val showLocationPermission by viewModel.showLocationPermissionDialog.collectAsState()
    val showNotificationPermission by viewModel.showNotificationPermissionDialog.collectAsState()
    val electronicsRest by viewModel.electronicsRest.collectAsState()
    var splashDone by rememberSaveable { mutableStateOf(embeddedMode || unifiedMode) }

    val mitzSession = remember(deps.platformContext, unifiedMode) {
        if (unifiedMode) UnifiedMitzModeSession(deps.platformContext) else null
    }

    TzaddikTheme(textScale = profile.textScale) {
        ProvideInAppBrowser {
        HalachicTermOverlay {
        val registerChecklistDebugToggle = LocalRegisterChecklistDebugToggle.current
        DisposableEffect(viewModel, registerChecklistDebugToggle) {
            registerChecklistDebugToggle?.invoke { viewModel.toggleChecklistDebugMenu() }
            onDispose { registerChecklistDebugToggle?.invoke(null) }
        }
        if (!splashDone) {
            PlatformBackHandler {
                if (embeddedMode) onRequestClose()
            }
            SplashScreen(
                onFinished = { splashDone = true },
                isFemale = profile.gender == Gender.FEMALE,
                titleOverride = when {
                    unifiedMode -> "Mitz Mode"
                    embeddedMode -> embeddedTitle
                    else -> null
                }
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
                    DisclaimerBody(embeddedMode = embeddedMode || unifiedMode)
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
                        appTitle = when {
                            unifiedMode -> "Mitz Mode"
                            embeddedMode -> embeddedTitle
                            else -> "Be a Tzaddik"
                        },
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
                appMode = appMode,
                onRequestClose = onRequestClose,
                returnToMainIcon = returnToMainIcon,
                mitzSession = mitzSession,
                externalMitzvotCount = externalMitzvotCount,
                externalChecklistChecked = externalChecklistChecked,
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
        if (showNotificationPermission) {
            PlatformBackHandler {
                viewModel.dismissNotificationPermissionDialog()
            }
            NotificationPermissionDialog(
                onOpenSettings = {
                    viewModel.dismissNotificationPermissionDialog()
                    viewModel.openNotificationSettings()
                },
                onDismiss = { viewModel.dismissNotificationPermissionDialog() },
            )
        }
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
    appMode: AppMode,
    onRequestClose: () -> Unit = {},
    returnToMainIcon: (@Composable () -> Unit)? = null,
    mitzSession: UnifiedMitzModeSession?,
    externalMitzvotCount: Int = 0,
    externalChecklistChecked: ((itemId: String, title: String, dayKey: String) -> Unit)? = null,
) {
    val embeddedMode = appMode == AppMode.Embedded
    val unifiedMode = appMode == AppMode.Unified
    val profile by viewModel.profile.collectAsState()
    val halachicDayKey by viewModel.halachicDayKey.collectAsState()
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var scrollSettingsToKashrut by remember { mutableStateOf(false) }
    var showWhatsAMitzvah by remember { mutableStateOf(false) }
    val holyFlash = rememberHolyFlashController()
    val candlelightReward = rememberCandlelightRewardController()
    var showExitConfirm by remember { mutableStateOf(false) }

    val tourSteps = remember { mitzModeAppTourSteps() }
    var tourStep by rememberSaveable { mutableIntStateOf(0) }
    var tourBounds by remember { mutableStateOf<Map<TourTarget, Rect>>(emptyMap()) }
    var overlayOrigin by remember { mutableStateOf(Offset.Zero) }
    val showAppTour = unifiedMode && profile.onboardingComplete && !profile.appTourCompleted
    val tourReporter = TourTargetReporter { target, bounds ->
        tourBounds = tourBounds + (target to bounds)
    }

    LaunchedEffect(showAppTour, tourStep) {
        if (!showAppTour) return@LaunchedEffect
        val forced = tourSteps.getOrNull(tourStep)?.forceTab
        if (forced != null && tab != forced) tab = forced
    }

    val pendingNavTab by AppNavigation.pendingTab.collectAsState()
    LaunchedEffect(pendingNavTab) {
        val target = AppNavigation.consumePendingTab() ?: return@LaunchedEffect
        tab = target
    }

    val countFlow = remember(mitzSession) {
        mitzSession?.count ?: kotlinx.coroutines.flow.MutableStateFlow(0)
    }
    val currentFlow = remember(mitzSession) {
        mitzSession?.currentMitzvah ?: kotlinx.coroutines.flow.MutableStateFlow(null)
    }
    val finalRewardFlow = remember(mitzSession) {
        mitzSession?.showFinalReward ?: kotlinx.coroutines.flow.MutableStateFlow(false)
    }
    val sessionCount by countFlow.collectAsState()
    val currentMitzvah by currentFlow.collectAsState()
    val showFinalReward by finalRewardFlow.collectAsState()
    val showRatingPrompt by viewModel.showRatingPrompt.collectAsState()

    val mitzvotCount = when {
        unifiedMode -> sessionCount
        else -> externalMitzvotCount
    }

    // Unified: 0 Today, 1 Timer, 2 Blessings, 3 MitzMode(action), 4 Settings, 5 Learn, 6 About
    // Embedded: 0 Today, 1 Timer, 2 Return, 3 Settings, 4 About
    // Standalone: 0 Today, 1 Timer, 2 Settings, 3 About
    val settingsTabIndex = when (appMode) {
        AppMode.Unified -> 4
        AppMode.Embedded -> 3
        AppMode.Standalone -> 2
    }
    val aboutTabIndex = when (appMode) {
        AppMode.Unified -> 6
        AppMode.Embedded -> 4
        AppMode.Standalone -> 3
    }
    val blessingsTabIndex = if (unifiedMode) 2 else -1
    val mitzModeActionIndex = if (unifiedMode) 3 else -1
    val learnTabIndex = if (unifiedMode) 5 else -1
    val returnMainTabIndex = if (embeddedMode) 2 else -1
    var showCertificate by remember { mutableStateOf(false) }

    val appTitle = when (appMode) {
        AppMode.Unified -> "Mitz Mode"
        AppMode.Embedded -> AppDisclaimer.EMBEDDED_APP_TITLE
        AppMode.Standalone -> "Be a Tzaddik"
    }

    LaunchedEffect(Unit) {
        viewModel.candlelightReward.collect { candlelightReward.trigger() }
    }

    PlatformBackHandler(enabled = !showExitConfirm && !showAppTour) {
        when {
            showCertificate -> showCertificate = false
            tab != 0 -> tab = 0
            else -> showExitConfirm = true
        }
    }
    if (showAppTour) {
        PlatformBackHandler {
            if (tourStep > 0) tourStep-- else viewModel.completeAppTour()
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { overlayOrigin = it.positionInRoot() },
    ) {
    CompositionLocalProvider(
        LocalTourTargetReporter provides if (showAppTour) tourReporter else null,
    ) {
    StarryScaffold(
        holyFlash = holyFlash,
        candlelightReward = candlelightReward,
        bottomBar = {
                MitzModeBottomNav(
                    selectedTab = tab,
                    onTabSelected = { selected ->
                        if (showAppTour) return@MitzModeBottomNav
                        when {
                            unifiedMode && selected == mitzModeActionIndex -> {
                                mitzSession?.openGenerator()
                            }
                            embeddedMode && selected == returnMainTabIndex -> {
                                onRequestClose()
                            }
                            else -> tab = selected
                        }
                    },
                    onTabPositioned = if (showAppTour && unifiedMode) {
                        { index, bounds ->
                            when (index) {
                                1 -> tourReporter.report(TourTarget.NavTimer, bounds)
                                2 -> tourReporter.report(TourTarget.NavBless, bounds)
                                3 -> tourReporter.report(TourTarget.NavMitzvah, bounds)
                            }
                        }
                    } else {
                        null
                    },
                    tabs = buildList {
                        add("Today" to { Icon(Icons.Default.CheckCircle, null) })
                        add("Timer" to { Icon(Icons.Default.Timer, null) })
                        if (unifiedMode) {
                            add("Bless" to { Icon(Icons.Default.MenuBook, null) })
                            add("" to {
                                if (returnToMainIcon != null) {
                                    returnToMainIcon()
                                } else {
                                    Image(
                                        painter = painterResource(Res.drawable.mitzvahbutton),
                                        contentDescription = "Mitzvah",
                                        modifier = Modifier.size(72.dp),
                                        contentScale = ContentScale.Fit,
                                    )
                                }
                            })
                        }
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
                        if (unifiedMode) {
                            add("Learn" to { Icon(Icons.Default.School, null) })
                        }
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
                    onChecklistItemChecked = { id, title ->
                        if (unifiedMode) {
                            mitzSession?.recordChecklistCheck(id, title, halachicDayKey)
                        } else {
                            externalChecklistChecked?.invoke(id, title, halachicDayKey)
                        }
                    },
                    mitzvotCount = if (unifiedMode) mitzvotCount else null,
                    onDebugSetMitzvotCount = if (unifiedMode) {
                        { n -> mitzSession?.debugSetCount(n) }
                    } else {
                        null
                    },
                    onDebugShowRatingPrompt = { viewModel.debugShowRatingPrompt() },
                )
                1 -> TimerScreen(
                    viewModel = viewModel,
                    onOpenKashrutSettings = {
                        scrollSettingsToKashrut = true
                        tab = settingsTabIndex
                    }
                )
                blessingsTabIndex -> BlessingsScreen()
                learnTabIndex -> LearnWebScreen()
                settingsTabIndex -> SettingsScreen(
                    viewModel = viewModel,
                    scrollToKashrut = scrollSettingsToKashrut,
                    onScrollTargetConsumed = { scrollSettingsToKashrut = false }
                )
                aboutTabIndex -> AboutScreen(
                    appTitle = appTitle,
                    onWhatsAMitzvah = if (unifiedMode) {
                        { showWhatsAMitzvah = true }
                    } else null,
                )
            }
        }
    }

        val showPill = (unifiedMode || (embeddedMode && externalChecklistChecked != null))
        if (showPill) {
            MitzModeCountPillOverlay(
                mitzvotCount = mitzvotCount,
                alwaysVisible = unifiedMode && tab == aboutTabIndex,
                onClick = if (unifiedMode && !showAppTour) {
                    { showCertificate = true }
                } else {
                    null
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 14.dp),
            )
        }

        if (showCertificate && unifiedMode) {
            PlatformBackHandler { showCertificate = false }
            androidx.compose.ui.window.Dialog(
                onDismissRequest = { showCertificate = false },
                properties = androidx.compose.ui.window.DialogProperties(
                    usePlatformDefaultWidth = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true,
                ),
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(TzaddikColors.ParchBase)
                        .statusBarsPadding(),
                ) {
                    StatusScreen(
                        mitzvotCount = mitzvotCount,
                        onReplayFinalReward = {
                            showCertificate = false
                            mitzSession?.requestFinalRewardReplay()
                        },
                    )
                    TextButton(
                        onClick = { showCertificate = false },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                    ) {
                        Text("Close", color = TzaddikColors.GoldBright)
                    }
                }
            }
        }

        val mitzvah = currentMitzvah
        if (unifiedMode && mitzvah != null && !showAppTour) {
            MitzvahGeneratorDialog(
                mitzvah = mitzvah,
                onAccept = { mitzSession?.acceptCurrent() },
                onNext = { mitzSession?.nextMitzvah() },
                onDismiss = { mitzSession?.dismissGenerator() },
            )
        }

        if (showWhatsAMitzvah) {
            ParchmentDialog(
                onDismiss = { showWhatsAMitzvah = false },
                title = MitzvahDefinitionText.TITLE,
                confirmButton = {
                    GoldButton(onClick = { showWhatsAMitzvah = false }, text = "Close")
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 480.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    AppText(
                        MitzvahDefinitionText.BODY,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TzaddikColors.TextBrown,
                        enableTerms = false,
                    )
                }
            }
        }

        FinalRewardVideoOverlay(
            visible = unifiedMode && showFinalReward,
            onComplete = { mitzSession?.onFinalRewardComplete() },
        )

        if (showRatingPrompt && !showAppTour && !showExitConfirm && !showWhatsAMitzvah) {
            AppRatingPromptDialog(
                onDismissForLater = { viewModel.dismissRatingPromptForLater() },
                onCompleted = { viewModel.markRatingPromptCompleted() },
            )
        }

        if (showAppTour) {
            MitzModeAppTourOverlay(
                currentStep = tourStep,
                steps = tourSteps,
                targetBounds = tourBounds,
                overlayOriginInRoot = overlayOrigin,
                onNext = {
                    if (tourStep < tourSteps.lastIndex) {
                        tourStep++
                    } else {
                        viewModel.completeAppTour()
                        tab = 0
                    }
                },
                onBack = { if (tourStep > 0) tourStep-- },
                onSkip = {
                    viewModel.completeAppTour()
                    tab = 0
                },
                onDismiss = {
                    viewModel.completeAppTour()
                    tab = 0
                },
            )
        }
    } // CompositionLocalProvider
    }
}
