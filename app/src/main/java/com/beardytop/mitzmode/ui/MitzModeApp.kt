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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.ui.components.*
import com.beardytop.mitzmode.utils.DeviceCapabilityChecker
import com.beardytop.mitzmode.viewmodel.MitzModeViewModel
import kotlinx.coroutines.delay
import com.beardytop.mitzmode.viewmodel.TranslationViewModel
import com.beardytop.mitzmode.ui.components.getMitzvahMeTranslation

@Composable
fun MitzModeApp(
    viewModel: MitzModeViewModel = hiltViewModel()
) {
    val currentMitzvah by viewModel.currentMitzvah.collectAsState()
    val showVideo by viewModel.showVideo.collectAsState()
    val showLevelUp by viewModel.showLevelUp.collectAsState()
    val showMusicReward by viewModel.showMusicReward.collectAsState()
    val completedMitzvot by viewModel.completedMitzvot.collectAsState()
    val showTour by viewModel.showTour.collectAsState()
    println("DEBUG: showVideo value: $showVideo")
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
    var isSparkleAnimating by remember { mutableStateOf(false) }

    var currentTourStep by remember { mutableStateOf(0) }
    var tourStepBounds by remember { mutableStateOf<Map<Int, Rect>>(emptyMap()) }
    var rootLayoutCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val tourSteps = remember {
        listOf(
            TourStep("Tap the Mitzvah Button any time for a random mitzvah suggestion!"),
            TourStep("Tap the menu button (three dots) to access powerful resources! The Daily Mitzvot Checklist is your complete guide to living according to Torah—perfect whether you're just starting your journey or deepening your practice. You'll also find blessings and other helpful tools in this menu!"),
            TourStep("Can you think of any more mitzvot we should add to the list? Submit them here!"),
            TourStep("You can check how many mitzvot you've completed here, and your current level.")
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
        // First layer: Background - Check device capability for video
        val context = LocalContext.current
        val canHandleVideo = remember { DeviceCapabilityChecker.canHandleVideoBackground(context) }
        
        if (canHandleVideo) {
            // Video background for capable devices
            VideoBackground(
                videoAsset = "background.mp4",
                isPlaying = true
            )
            // Semi-transparent overlay for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            )
        } else {
            // Beautiful animated gradient background for devices that can't handle video
            GradientBackground()
        }
        
        // Main content (hero title)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.13f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 28.dp)
            ) {
                val translationViewModel: TranslationViewModel = hiltViewModel()
                val translationEnabled by translationViewModel.translationEnabled.collectAsState()
                val currentLanguage by translationViewModel.currentLanguage.collectAsState()

                val mitzvahMeText = if (translationEnabled && currentLanguage != "en") {
                    getMitzvahMeTranslation(currentLanguage, translationViewModel)
                } else {
                    "Mitzvah Me"
                }

                val instructionText = if (translationEnabled && currentLanguage != "en") {
                    when (currentLanguage) {
                        "he" -> "לחץ על כפתור $mitzvahMeText כדי לעשות מצווה!"
                        "es" -> "¡Presiona el botón $mitzvahMeText para realizar una mitzvá!"
                        "fr" -> "Appuyez sur le bouton $mitzvahMeText pour faire une mitzvah!"
                        "de" -> "Drücke den $mitzvahMeText Button für eine Mitzvah!"
                        "it" -> "Premi il pulsante $mitzvahMeText per fare una mitzvah!"
                        "pt" -> "Pressione o botão $mitzvahMeText para fazer uma mitzvá!"
                        "ru" -> "Нажмите кнопку $mitzvahMeText для выполнения мицвы!"
                        "ar" -> "اضغط على زر $mitzvahMeText لأداء ميتزفا!"
                        "zh" -> "点击${mitzvahMeText}按钮做善行！"
                        "ja" -> "${mitzvahMeText}ボタンを押してミツバーを行おう！"
                        else -> "Tap the $mitzvahMeText button for a mitzvah!"
                    }
                } else {
                    "Tap the Mitzvah Me button for a mitzvah!"
                }

                // Hero title — gold gradient, drop shadow
                Text(
                    text = instructionText,
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
            }
        }

        // Second layer: Dialogs
        if (currentMitzvah != null) {
            MitzvahDialog(
                mitzvah = currentMitzvah!!,
                onDismiss = { viewModel.clearCurrentMitzvah() },
                onAccept = {
                    viewModel.onMitzvahAccepted()
                    isSparkleAnimating = true
                    println("DEBUG: Mitzvah accepted!")
                },
                onNext = { viewModel.onMitzvahButtonPressed() }
            )
        }

        // Third layer: Always-clickable controls
        Box(modifier = Modifier.fillMaxSize()) {
                // Detect full-screen phones and adjust padding
                val configuration = LocalConfiguration.current
                val isFullScreen = configuration.screenHeightDp > 700 // Detect tall screens
                val topPadding = if (isFullScreen) 32.dp else 8.dp
                
                // Menu in top left (tour step 1) — glass-morphic chip
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = topPadding, start = 14.dp)
                        .then(
                            if (showTour) Modifier.onGloballyPositioned { coords ->
                                rootLayoutCoords?.let { root ->
                                    val pos = root.localPositionOf(coords, Offset.Zero)
                                    val sz = coords.size
                                    tourStepBounds = tourStepBounds + (1 to Rect(pos.x, pos.y, pos.x + sz.width, pos.y + sz.height))
                                }
                            } else Modifier
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                spotColor = Color(0xFFFFD56B)
                            )
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.18f),
                                        Color.White.copy(alpha = 0.06f)
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = Color(0xFFFFD56B).copy(alpha = 0.55f),
                                shape = CircleShape
                            )
                            .clickable { if (!showTour) showMenu = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color(0xFFFFE082),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { if (!showTour) showMenu = false },
                        modifier = Modifier
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
                            text = { TranslatableText("Daily Mitzvot Checklist") },
                            onClick = {
                                if (!showTour) {
                                    showMenu = false
                                    showDailyMitzvot = true
                                }
                            },
                            enabled = !showTour
                        )
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

                // Mitzvah count in top right — elegant gold pill (tour step 3)
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
                                    tourStepBounds = tourStepBounds + (3 to Rect(pos.x, pos.y, pos.x + sz.width, pos.y + sz.height))
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

                // Center Mitzvah Button - using centered layout since video is disabled
                val buttonModifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)

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

                // Add a Mitzvah button (tour step 2) — refined outline pill
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 76.dp)
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
                    OutlinedButton(
                        onClick = { showAddMitzvah = true },
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.4.dp,
                            color = Color(0xFFFFD56B).copy(alpha = 0.8f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White.copy(alpha = 0.08f),
                            contentColor = Color(0xFFFFE082)
                        ),
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                    ) {
                        TranslatableText(
                            "Add a Mitzvah",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.6.sp
                            ),
                            color = Color(0xFFFFE082)
                        )
                    }
                }

                // What's a Mitzvah? button — primary gold-gradient pill
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 18.dp)
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
                        .padding(horizontal = 28.dp, vertical = 12.dp)
                ) {
                    TranslatableText(
                        "What's a Mitzvah?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.6.sp,
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
        
        // Show checklist dialog when selected from menu (render before tour overlay during tour)
        if (showDailyMitzvot && !showTour) {
            DailyMitzvotChecklist(
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
                in 1000..Int.MAX_VALUE -> "Moshiach!!!"
                else -> "Secular"  // Default case for 0 or negative
            }
            MitzvahLevelDialog(
                count = count,
                currentLevel = currentLevel,
                onDismiss = { showMitzvahLevel = false }
            )
        }

        if (showAddMitzvah) {
            AddMitzvahDialog(
                onDismiss = { showAddMitzvah = false }
            )
        }



        if (showLanguageSelection) {
            LanguageSelectionDialog(
                onDismiss = { showLanguageSelection = false }
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

        // First-time app tour (step 0 = Mitzvah button, 1 = menu/dropdown, 2 = Add a Mitzvah, 3 = level)
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