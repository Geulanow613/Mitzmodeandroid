package com.beardytop.beatzaddik.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.KashrutWaitTimes
import com.beardytop.beatzaddik.domain.MealCategory
import com.beardytop.beatzaddik.ui.components.GoldButton
import com.beardytop.beatzaddik.ui.components.GoldFlourishDivider
import com.beardytop.beatzaddik.ui.components.ParchmentTextButton
import com.beardytop.beatzaddik.ui.theme.TzaddikColors
import com.beardytop.beatzaddik.viewmodel.AppViewModel
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

@Composable
fun TimerScreen(
    viewModel: AppViewModel,
    onOpenKashrutSettings: () -> Unit = {}
) {
    val wait by viewModel.kashrutWait.collectAsState()
    val profile by viewModel.profile.collectAsState()
    val nowMillis by produceState(Clock.System.now().toEpochMilliseconds()) {
        while (true) {
            value = Clock.System.now().toEpochMilliseconds()
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen title
        Text(
            "Kashrut Timer",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TzaddikColors.GoldBright
        )
        Text(
            "Track your meat / dairy separation",
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.ParchTop.copy(alpha = 0.88f),
            textAlign = TextAlign.Center
        )

        GoldFlourishDivider(widthFraction = 0.45f)

        // Info panel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(TzaddikColors.NavyDeep, TzaddikColors.NavyMid)
                    )
                )
                .border(1.dp, TzaddikColors.GoldBorder.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "Your wait times",
                style = MaterialTheme.typography.labelLarge,
                color = TzaddikColors.GoldBright,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            WaitTimeRow(
                emoji = "🥩",
                text = "After meat: wait ${profile.meatToDairyHours()} hours before dairy"
            )
            WaitTimeRow(
                emoji = "🧀",
                text = "After dairy: wait ${KashrutWaitTimes.formatDairyToMeatWait(profile.dairyToMeatWaitMinutes())} before meat"
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Wait times reflect your family custom (minhag). Tap below to adjust.",
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.ParchTop.copy(alpha = 0.78f)
            )
            ParchmentTextButton(
                onClick = onOpenKashrutSettings,
                text = "Adjust wait times →"
            )
        }

        // Active timer or meal log buttons
        AnimatedVisibility(
            visible = wait != null,
            enter = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.9f),
            exit = fadeOut(tween(200)) + scaleOut(tween(200))
        ) {
            wait?.let { ActiveTimerCard(it, nowMillis, onClear = { viewModel.clearKashrut() }) }
        }

        AnimatedVisibility(
            visible = wait == null,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(200))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "What did you just eat?",
                    style = MaterialTheme.typography.titleMedium,
                    color = TzaddikColors.ParchTop,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                GoldButton(
                    onClick = { viewModel.logMeal(MealCategory.MEAT) },
                    text = "I ate meat",
                    modifier = Modifier.fillMaxWidth()
                )
                GoldButton(
                    onClick = { viewModel.logMeal(MealCategory.DAIRY) },
                    text = "I ate dairy",
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Tap to start the countdown until you may eat the other category.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TzaddikColors.ParchTop.copy(alpha = 0.78f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun WaitTimeRow(emoji: String, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 16.sp)
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = TzaddikColors.ParchTop,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun ActiveTimerCard(
    wait: KashrutWait,
    nowMillis: Long,
    onClear: () -> Unit
) {
    val remaining = (wait.endsAtEpochMillis - nowMillis).coerceAtLeast(0)
    val totalMs = wait.endsAtEpochMillis - (wait.endsAtEpochMillis - remaining)
    val progress by animateFloatAsState(
        targetValue = if (wait.endsAtEpochMillis - nowMillis > 0) {
            remaining.toFloat() / (wait.endsAtEpochMillis - (nowMillis - remaining)).coerceAtLeast(1).toFloat()
        } else 0f,
        animationSpec = tween(800)
    )

    val hours = remaining / 3_600_000L
    val minutes = (remaining % 3_600_000L) / 60_000L
    val seconds = (remaining % 60_000L) / 1_000L
    val isDone = remaining == 0L

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(TzaddikColors.NavyDeep, TzaddikColors.NavyMid)
                )
            )
            .border(1.dp, TzaddikColors.GoldBright.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            when (wait.category) {
                MealCategory.MEAT -> "Waiting before dairy"
                MealCategory.DAIRY -> "Waiting before meat"
            },
            style = MaterialTheme.typography.titleSmall,
            color = TzaddikColors.GoldBright.copy(alpha = 0.85f),
            letterSpacing = 1.2.sp
        )

        if (isDone) {
            Text(
                "✓ You may now eat",
                style = MaterialTheme.typography.headlineSmall,
                color = TzaddikColors.GoldBright,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                "%02d:%02d:%02d".format(hours, minutes, seconds),
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = TzaddikColors.GoldBright,
                letterSpacing = 2.sp
            )
            Text(
                when (wait.category) {
                    MealCategory.MEAT -> "remaining until dairy"
                    MealCategory.DAIRY -> "remaining until meat"
                },
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.GoldBright.copy(alpha = 0.55f)
            )
        }

        ParchmentTextButton(
            onClick = onClear,
            text = "Cancel timer"
        )
    }
}
