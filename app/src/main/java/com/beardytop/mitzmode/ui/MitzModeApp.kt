package com.beardytop.mitzmode.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity
import com.beardytop.mitzmode.tzaddik.EmbeddedTzaddikChecklist
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beardytop.mitzmode.ui.LocalTranslationViewModel
import com.beardytop.mitzmode.viewmodel.TranslationViewModel
import com.beardytop.mitzmode.ui.components.*
import com.beardytop.mitzmode.viewmodel.MitzModeViewModel
import kotlinx.coroutines.delay

@Composable
fun MitzModeApp(
    viewModel: MitzModeViewModel = hiltViewModel()
) {
    val currentMitzvah by viewModel.currentMitzvah.collectAsStateWithLifecycle()
    val showVideo by viewModel.showVideo.collectAsStateWithLifecycle()
    val showLevelUp by viewModel.showLevelUp.collectAsStateWithLifecycle()
    val showMusicReward by viewModel.showMusicReward.collectAsStateWithLifecycle()
    val completedMitzvot by viewModel.completedMitzvot.collectAsStateWithLifecycle()
    val showTour by viewModel.showTour.collectAsStateWithLifecycle()
    var showMitzvahInfo by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showMitzvahCount by remember { mutableStateOf(false) }
    var showThankYou by remember { mutableStateOf(false) }
    var showBirkatHamazon by remember { mutableStateOf(false) }
    var showTefilat by remember { mutableStateOf(false) }
    var showBrachot by remember { mutableStateOf(false) }
    var showAddMitzvah by remember { mutableStateOf(false) }
    var showDailyMitzvot by remember { mutableStateOf(false) }
    var showMitzvahLevel by remember { mutableStateOf(false) }

    var showLanguageSelection by remember { mutableStateOf(false) }
    var showMusicPlayer by remember { mutableStateOf(false) }
    var translationNoticeTarget by remember { mutableStateOf<Pair<String, String>?>(null) }

    var currentTourStep by remember { mutableStateOf(0) }
    var tourStepBounds by remember { mutableStateOf<Map<Int, Rect>>(emptyMap()) }
    var rootLayoutCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val tourSteps = remember {
        listOf(
            TourStep(
                "Tap the Mitzvah Me button any time for a fresh mitzvah and some instant inspiration."
            ),
            TourStep(
                "New to Judaism—or ready to level up your day? 'Check' out the Daily Mitzvot Checklist! " +
                    "It's your smart Torah companion: GPS powers live zmanim so you know when to daven, observe holidays, light candles, and more! " +
                    "See the daily mitzvot available to you, tap any item for clear explanations on how to follow the Torah, and ride with Heavenly vibes!"
            ),
            TourStep(
                "Tap the menu (⋮) for blessings before and after food, Birkat Hamazon, the Traveler's Prayer, language settings, and more."
            ),
            TourStep("Have a mitzvah idea we should add? Submit it here—help grow the list for everyone."),
            TourStep("Track your streak here—see how many mitzvot you've completed and your current level.")
        )
    }

    // Don't auto-open menu during tour - just highlight the menu button
    // Close menu if tour starts while it's open
    LaunchedEffect(showTour) {
        if (showTour) {
            showMenu = false
        }
    }
    
    val context = LocalContext.current
    // Video completely disabled to prevent flickering issues

    LaunchedEffect(showThankYou) {
        if (showThankYou) {
            delay(2500)
            showThankYou = false
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { rootLayoutCoords = it }
    ) {
        // First layer: static gradient background on all devices (no looping video).
        LowEndDeviceBackground()
        
        // Second layer: Dialogs
        if (currentMitzvah != null) {
            MitzvahDialog(
                mitzvah = currentMitzvah!!,
                onDismiss = { viewModel.clearCurrentMitzvah() },
                onAccept = {
                    viewModel.onMitzvahAccepted()
                },
                onNext = { viewModel.onMitzvahButtonPressed() }
            )
        }

        // Third layer: Always-clickable controls (inset above gesture/nav bar)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
                // Detect full-screen phones and adjust padding
                val configuration = LocalConfiguration.current
                val isFullScreen = configuration.screenHeightDp > 700 // Detect tall screens
                val topPadding = if (isFullScreen) 32.dp else 8.dp
                val headerRowHeight = 54.dp
                val instructionTopPadding = topPadding + 8.dp + headerRowHeight + 28.dp

                // Instruction below top chrome (menu + counter), inset from sides
                TranslatableText(
                    text = "Tap the Mitzvah Me button for a mitzvah!",
                    enableHalachicTerms = false,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = instructionTopPadding)
                        .padding(horizontal = 72.dp),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.4.sp,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.45f),
                            offset = Offset(0f, 3f),
                            blurRadius = 8f
                        )
                    ),
                    color = Color(0xFFFFE082),
                    textAlign = TextAlign.Center
                )

                // Menu in top left (tour step 2) — glass-morphic chip
                val menuButtonSize = headerRowHeight
                val menuDropdownWidth = 280.dp
                val menuDropdownOffsetX = (menuButtonSize - menuDropdownWidth) / 2
                val menuBorderPx = with(LocalDensity.current) { 1.dp.toPx() }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .wrapContentSize(Alignment.TopStart)
                        // Align vertically with the top-right counter pill.
                        .padding(top = topPadding + 8.dp, start = 14.dp)
                        .then(
                            if (showTour) Modifier.onGloballyPositioned { coords ->
                                rootLayoutCoords?.let { root ->
                                    val pos = root.localPositionOf(coords, Offset.Zero)
                                    val sz = coords.size
                                    tourStepBounds = tourStepBounds + (2 to Rect(pos.x, pos.y, pos.x + sz.width, pos.y + sz.height))
                                }
                            } else Modifier
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .size(menuButtonSize)
                            .drawBehind {
                                // Draw from true center — avoids off-center gray octagon from Modifier.shadow().
                                val radius = size.minDimension / 2f
                                val center = Offset(size.width / 2f, size.height / 2f)
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.18f),
                                            Color.White.copy(alpha = 0.06f)
                                        ),
                                        center = center,
                                        radius = radius
                                    ),
                                    radius = radius,
                                    center = center
                                )
                                drawCircle(
                                    color = Color(0xFFFFD56B).copy(alpha = 0.55f),
                                    radius = radius - menuBorderPx / 2f,
                                    center = center,
                                    style = Stroke(width = menuBorderPx)
                                )
                            }
                            .clickable { if (!showTour) showMenu = true },
                        contentAlignment = Alignment.Center
                    ) {
                        MenuDotsIcon(tint = Color(0xFFFFE082))
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { if (!showTour) showMenu = false },
                        offset = DpOffset(menuDropdownOffsetX, 6.dp),
                        modifier = Modifier
                            .width(menuDropdownWidth)
                            .background(
                                color = Color(0xFFFFFBEE),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFFB8860B).copy(alpha = 0.55f),
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        DropdownMenuItem(
                            text = { TranslatableText("About") },
                            onClick = {
                                if (!showTour) {
                                    showMenu = false
                                    showAbout = true
                                }
                            },
                            enabled = !showTour
                        )
                        DropdownMenuItem(
                            text = { TranslatableText("Blessing After Meals") },
                            onClick = {
                                if (!showTour) {
                                    showMenu = false
                                    showBirkatHamazon = true
                                }
                            },
                            enabled = !showTour
                        )
                        DropdownMenuItem(
                            text = { TranslatableText("Traveler's Prayer") },
                            onClick = {
                                if (!showTour) {
                                    showMenu = false
                                    showTefilat = true
                                }
                            },
                            enabled = !showTour
                        )
                        DropdownMenuItem(
                            text = { TranslatableText("Blessings") },
                            onClick = {
                                if (!showTour) {
                                    showMenu = false
                                    showBrachot = true
                                }
                            },
                            enabled = !showTour
                        )

                        DropdownMenuItem(
                            text = { TranslatableText("🎵 Official App Song") },
                            onClick = {
                                if (!showTour) {
                                    showMenu = false
                                    showMusicPlayer = true
                                }
                            },
                            enabled = !showTour
                        )

                        DropdownMenuItem(
                            text = { TranslatableText("Language Settings") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Language"
                                )
                            },
                            onClick = {
                                if (!showTour) {
                                    showMenu = false
                                    showLanguageSelection = true
                                }
                            },
                            enabled = !showTour
                        )
                    }
                }

                // Mitzvah count in top right — elegant gold pill (tour step 4)
                val mitzvotCount = completedMitzvot.size
                val gold = Color(0xFFFFD56B)
                val goldDeep = Color(0xFFB8860B)
                val pillActive = mitzvotCount > 0
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = topPadding + 8.dp, end = 14.dp)
                        .then(
                            if (showTour) Modifier.onGloballyPositioned { coords ->
                                rootLayoutCoords?.let { root ->
                                    val pos = root.localPositionOf(coords, Offset.Zero)
                                    val sz = coords.size
                                    tourStepBounds = tourStepBounds + (4 to Rect(pos.x, pos.y, pos.x + sz.width, pos.y + sz.height))
                                }
                            } else Modifier
                        )
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(50),
                            spotColor = gold
                        )
                        .clip(RoundedCornerShape(50))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = if (pillActive) 0.20f else 0.10f),
                                    Color.White.copy(alpha = if (pillActive) 0.08f else 0.04f)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    gold.copy(alpha = if (pillActive) 0.85f else 0.4f),
                                    gold.copy(alpha = if (pillActive) 0.5f else 0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable { showMitzvahLevel = true }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed Mitzvot",
                            tint = if (pillActive) gold else gold.copy(alpha = 0.5f),
                            modifier = Modifier.size(26.dp)
                        )
                        Text(
                            text = "$mitzvotCount",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp,
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.45f),
                                    offset = Offset(0f, 1f),
                                    blurRadius = 3f
                                )
                            ),
                            color = if (pillActive) gold else gold.copy(alpha = 0.55f)
                        )
                    }
                }

                // Center Mitzvah Button — nudged above bottom CTAs and nav bar
                val buttonModifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
                    .padding(bottom = 72.dp)

                Box(
                    modifier = buttonModifier
                        .then(
                            if (showTour) Modifier.onGloballyPositioned { coords ->
                                rootLayoutCoords?.let { root ->
                                    val pos = root.localPositionOf(coords, Offset.Zero)
                                    val sz = coords.size
                                    tourStepBounds = tourStepBounds + (0 to Rect(pos.x, pos.y, pos.x + sz.width, pos.y + sz.height))
                                }
                            } else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    MitzvahButton(
                        onClick = { 
                            viewModel.onMitzvahButtonPressed()
                        }
                    )
                }

                // Bottom action stack — checklist is primary (not in ⋮ menu); tour step 1
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-20).dp)
                            .then(
                                if (showTour) Modifier.onGloballyPositioned { coords ->
                                    rootLayoutCoords?.let { root ->
                                        val pos = root.localPositionOf(coords, Offset.Zero)
                                        val sz = coords.size
                                        tourStepBounds = tourStepBounds + (
                                            1 to Rect(pos.x, pos.y, pos.x + sz.width, pos.y + sz.height)
                                        )
                                    }
                                } else Modifier
                            )
                            .shadow(
                                elevation = 16.dp,
                                shape = RoundedCornerShape(50),
                                spotColor = Color(0xFFFFD56B)
                            )
                            .clip(RoundedCornerShape(50))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFFF3B5),
                                        Color(0xFFFFE082),
                                        Color(0xFFFFD56B),
                                        Color(0xFFE0AB2F)
                                    )
                                )
                            )
                            .border(
                                width = 1.6.dp,
                                color = Color(0xFFFFF8D6),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable { showDailyMitzvot = true }
                            .padding(horizontal = 24.dp, vertical = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TranslatableText(
                            "Daily Mitzvot Checklist",
                            enableHalachicTerms = false,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.55.sp,
                                textAlign = TextAlign.Center,
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.32f),
                                    offset = Offset(0f, 1.8f),
                                    blurRadius = 2.8f
                                )
                            ),
                            color = Color(0xFF1A3D72)
                        )
                    }

                    // Add a Mitzvah (tour step 3)
                    Box(
                        modifier = Modifier
                            .then(
                                if (showTour) Modifier.onGloballyPositioned { coords ->
                                    rootLayoutCoords?.let { root ->
                                        val pos = root.localPositionOf(coords, Offset.Zero)
                                        val sz = coords.size
                                        tourStepBounds = tourStepBounds + (
                                            3 to Rect(pos.x, pos.y, pos.x + sz.width, pos.y + sz.height)
                                        )
                                    }
                                } else Modifier
                            )
                    ) {
                        OutlinedButton(
                            onClick = { showAddMitzvah = true },
                            modifier = Modifier.wrapContentWidth(),
                            shape = RoundedCornerShape(50),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.4.dp,
                                color = Color(0xFFFFD56B).copy(alpha = 0.8f)
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White.copy(alpha = 0.08f),
                                contentColor = Color(0xFFFFE082)
                            ),
                            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 12.dp)
                        ) {
                            TranslatableText(
                                "Add a Mitzvah",
                                enableHalachicTerms = false,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.6.sp
                                ),
                                color = Color(0xFFFFE082)
                            )
                        }
                    }

                    // What's a Mitzvah?
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(50),
                                spotColor = Color(0xFFFFD56B)
                            )
                            .clip(RoundedCornerShape(50))
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFFFD56B),
                                        Color(0xFFD4A024),
                                        Color(0xFFB8860B)
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFFFFE082).copy(alpha = 0.8f),
                                shape = RoundedCornerShape(50)
                            )
                            .clickable { showMitzvahInfo = true }
                            .padding(horizontal = 24.dp, vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TranslatableText(
                            "What's a Mitzvah?",
                            enableHalachicTerms = false,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.6.sp,
                                textAlign = TextAlign.Center,
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.35f),
                                    offset = Offset(0f, 1.5f),
                                    blurRadius = 3f
                                )
                            ),
                            color = Color.White
                        )
                    }
                }
            }

        // Fourth layer: Dialogs
        if (showAbout) {
            AboutDialog(
                onDismiss = { showAbout = false }
            )
        }
        
        showVideo?.let { videoNumber ->
            RewardVideoDialog(
                videoNumber = videoNumber,
                onDismiss = {
                    viewModel.onVideoComplete()
                    viewModel.onLevelUpComplete() // Also dismiss level up when video ends
                },
                levelName = showLevelUp // Pass the level name to show overlay
            )
        }
        
        // Show thank you message
        AnimatedVisibility(
            visible = showThankYou,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                TranslatableText(
                    text = "Thanks for keeping it holy!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 64.dp)
                )
            }
        }
        
        if (showMitzvahInfo) {
            MitzvahInfoDialog(
                onDismiss = { showMitzvahInfo = false }
            )
        }

        if (showBirkatHamazon) {
            BirkatHamazonDialog(
                onDismiss = { showBirkatHamazon = false }
            )
        }

        if (showTefilat) {
            TefilatHaderechDialog(onDismiss = { showTefilat = false })
        }

        if (showBrachot) {
            BrachotDialog(onDismiss = { showBrachot = false })
        }
        
        // Be a Tzaddik checklist — full embedded app when selected from menu
        if (showDailyMitzvot && !showTour) {
            val activity = LocalContext.current as ComponentActivity
            EmbeddedTzaddikChecklist(
                activity = activity,
                onDismiss = { showDailyMitzvot = false }
            )
        }
        
        // Show level dialog when clicking mitzvah count
        if (showMitzvahLevel) {
            val count = completedMitzvot.size
            val currentLevel = when (count) {
                0 -> "Secular"
                in 1..9 -> "Beginner"
                in 10..49 -> "Ba'al Teshuva"
                in 50..99 -> "Master Cholent Chef"
                in 100..199 -> "Aspiring Kiddush Maker"
                in 200..299 -> "Assistant Gabbai"
                in 300..399 -> "Guy who hands out candy at shul"
                in 400..499 -> "Western Wall Reveler"
                in 500..599 -> "Sofer"
                in 600..699 -> "Tzaddik"
                in 700..799 -> "Living Sefer Torah"
                in 800..899 -> "Eliyahu HaNavi"
                in 900..999 -> "King David"
                in 1000..1799 -> "Moshiach!!!"
                in 1800..Int.MAX_VALUE -> "Mitz Mode!"
                else -> "Secular"  // Default case for 0 or negative
            }
            MitzvahLevelDialog(
                count = count,
                currentLevel = currentLevel,
                onDismiss = { showMitzvahLevel = false },
                onRequestFinalRewardVideo = {
                    showMitzvahLevel = false
                    viewModel.requestFinalRewardVideoReplay()
                }
            )
        }

        if (showAddMitzvah) {
            AddMitzvahDialog(
                onDismiss = { showAddMitzvah = false }
            )
        }



        if (showLanguageSelection) {
            LanguageSelectionDialog(
                onDismiss = { showLanguageSelection = false },
                onNonEnglishLanguageSelected = { code, name ->
                    translationNoticeTarget = code to name
                }
            )
        }

        translationNoticeTarget?.let { (code, name) ->
            TranslationNoticeDialog(
                languageName = name,
                targetLanguageCode = code,
                onDismiss = { translationNoticeTarget = null },
                onUnderstood = { translationNoticeTarget = null }
            )
        }
        
        // Music reward dialog
        if (showMusicReward) {
            MusicPlayerDialog(
                onDismiss = { viewModel.onMusicRewardComplete() }
            )
        }
        
        // Manual music player dialog
        if (showMusicPlayer) {
            MusicPlayerDialog(
                onDismiss = { showMusicPlayer = false }
            )
        }

        // Tour: 0 = Mitzvah Me, 1 = Daily Mitzvot Checklist, 2 = ⋮ menu, 3 = Add a Mitzvah, 4 = level
        if (showTour) {
            AppTourOverlay(
                currentStep = currentTourStep,
                steps = tourSteps,
                stepBounds = tourStepBounds,
                onNext = {
                    if (currentTourStep < tourSteps.lastIndex) {
                        currentTourStep++
                    } else {
                        viewModel.completeTour()
                    }
                },
                onBack = {
                    if (currentTourStep > 0) {
                        currentTourStep--
                    }
                },
                onSkip = {
                    showMenu = false
                    viewModel.skipTour() 
                },
                onDismiss = { 
                    showMenu = false
                    viewModel.skipTour() 
                }
            )
        }
    }
} 