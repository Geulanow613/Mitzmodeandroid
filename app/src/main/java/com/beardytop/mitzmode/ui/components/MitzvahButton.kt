package com.beardytop.mitzmode.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.os.VibrationEffect
import android.os.Vibrator
import android.graphics.BitmapFactory
import android.content.Context
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.getSystemService
import android.os.Build
import android.os.VibratorManager
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.ui.LocalTranslationViewModel
import com.beardytop.mitzmode.viewmodel.TranslationViewModel

// Function to get the proper "Mitzvah Me" translation for different languages
@Composable
fun getMitzvahMeTranslation(
    currentLanguage: String,
    translationViewModel: TranslationViewModel
): String {
    return when (currentLanguage) {
        "he" -> "מצווה אותי" // "Mitzvah me" in Hebrew (imperative)
        "es" -> "Dame Mitzvá" // "Give me Mitzvah" in Spanish (imperative)
        "fr" -> "Mitzvah-moi" // "Mitzvah me" in French (imperative)
        "de" -> "Mitzvah mich" // "Mitzvah me" in German (imperative)
        "it" -> "Mitzvah me" // "Mitzvah me" in Italian (imperative)
        "pt" -> "Me dê Mitzvá" // "Give me Mitzvah" in Portuguese (imperative)
        "ru" -> "Дай мне Мицву" // "Give me Mitzvah" in Russian (imperative)
        "ar" -> "أعطني ميتزفا" // "Give me Mitzvah" in Arabic (imperative)
        "zh" -> "给我善行" // "Give me Mitzvah" in Chinese (imperative)
        "ja" -> "ミツバーをください" // "Give me Mitzvah" in Japanese (imperative)
        else -> "Mitzvah Me" // Default English
    }
}

@Composable
fun MitzvahButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    translationViewModel: TranslationViewModel =
        LocalTranslationViewModel.current ?: hiltViewModel()
) {
    val context = LocalContext.current
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Get translation state
    val translationEnabled by translationViewModel.translationEnabled.collectAsState()
    val currentLanguage by translationViewModel.currentLanguage.collectAsState()
    val shouldShowTranslatable = translationEnabled && currentLanguage != "en"
    
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatX"
    )
    
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    
    // Enhanced animations for spectacular translatable button
    val spectacularGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "spectacularGlow"
    )
    
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )
    
    val starRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "starRotation"
    )
    
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    // Load original image for English
    val originalBitmap = remember {
        context.assets.open("mitzvah-button.png").use { 
            BitmapFactory.decodeStream(it)
        }.asImageBitmap()
    }

    if (shouldShowTranslatable) {
        // Show spectacular translatable button
        Box(
            modifier = modifier
                .size(180.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offsetX
                    translationY = offsetY
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(50)
                    }
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            // Triple-layer outer glow with pulsing effect
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFD700).copy(alpha = spectacularGlowAlpha * 0.3f),
                                Color(0xFFFFC107).copy(alpha = spectacularGlowAlpha * 0.2f),
                                Color.Transparent
                            ),
                            radius = 300f
                        )
                    )
            )
            
            // Second glow layer
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFD700).copy(alpha = spectacularGlowAlpha * 0.5f),
                                Color(0xFFFF8F00).copy(alpha = spectacularGlowAlpha * 0.3f),
                                Color.Transparent
                            ),
                            radius = 250f
                        )
                    )
            )
            
            // Main button with enhanced gradients
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = CircleShape,
                        ambientColor = Color(0xFF1565C0),
                        spotColor = Color(0xFFFFD700)
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF2196F3), // Brighter blue
                                Color(0xFF1976D2), // Rich blue
                                Color(0xFF1565C0), // Medium blue
                                Color(0xFF0D47A1), // Dark blue
                                Color(0xFF002171)  // Deep navy
                            ),
                            radius = 220f
                        )
                    )
                    .border(
                        width = 5.dp,
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFFFFD700), // Gold
                                Color(0xFFFFC107), // Amber
                                Color(0xFFFF8F00), // Orange
                                Color(0xFFFFD700), // Gold again for seamless loop
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Shimmer effect overlay
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.3f),
                                    Color.Transparent
                                ),
                                start = androidx.compose.ui.geometry.Offset(shimmerOffset - 100f, 0f),
                                end = androidx.compose.ui.geometry.Offset(shimmerOffset + 100f, 100f)
                            )
                        )
                )
                
                // Enhanced inner ring with animated gradient
                Box(
                    modifier = Modifier
                        .size(145.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF64B5F6), // Light blue
                                    Color(0xFF42A5F5), // Medium blue
                                    Color(0xFF2196F3), // Bright blue
                                    Color(0xFF1976D2)  // Darker blue
                                ),
                                radius = 160f
                            )
                        )
                        .border(
                            width = 3.dp,
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    Color(0xFFFFD700).copy(alpha = 0.9f),
                                    Color(0xFFFFF176).copy(alpha = 0.7f),
                                    Color(0xFFFFD700).copy(alpha = 0.9f)
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    TranslatableText(
                        text = "Mitzvah Me",
                        enableHalachicTerms = false,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 19.sp,
                            letterSpacing = 1.2.sp,
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = Color.Black.copy(alpha = 0.5f),
                                offset = androidx.compose.ui.geometry.Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        translationViewModel = translationViewModel,
                    )
                    
                    // Spectacular rotating stars with different speeds
                    Text(
                        text = "✡",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                        color = Color(0xFFFFD700).copy(alpha = spectacularGlowAlpha * 0.9f),
                        modifier = Modifier
                            .offset(x = (-50).dp, y = (-15).dp)
                            .rotate(starRotation)
                            .scale(pulseScale)
                    )
                    Text(
                        text = "✡",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 20.sp),
                        color = Color(0xFFFFF176).copy(alpha = spectacularGlowAlpha * 0.8f),
                        modifier = Modifier
                            .offset(x = 50.dp, y = (-15).dp)
                            .rotate(-starRotation * 0.7f)
                            .scale(pulseScale * 0.9f)
                    )
                    Text(
                        text = "✡",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 22.sp),
                        color = Color(0xFFFFB300).copy(alpha = spectacularGlowAlpha * 0.9f),
                        modifier = Modifier
                            .offset(x = 0.dp, y = 40.dp)
                            .rotate(starRotation * 0.5f)
                            .scale(pulseScale * 1.1f)
                    )
                    
                    // Additional floating star particles
                    Text(
                        text = "✦",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFFFD700).copy(alpha = spectacularGlowAlpha * 0.6f),
                        modifier = Modifier
                            .offset(x = (-30).dp, y = 25.dp)
                            .rotate(starRotation * 1.5f)
                            .scale(pulseScale * 0.7f)
                    )
                    Text(
                        text = "✦",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFFFF176).copy(alpha = spectacularGlowAlpha * 0.5f),
                        modifier = Modifier
                            .offset(x = 35.dp, y = 20.dp)
                            .rotate(-starRotation * 1.2f)
                            .scale(pulseScale * 0.8f)
                    )
                }
            }
        }
    } else {
        // Show original English button
        Image(
            bitmap = originalBitmap,
            contentDescription = "Mitzvah Button",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .size(180.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offsetX
                    translationY = offsetY
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(50)
                    }
                    onClick()
                }
        )
    }
} 