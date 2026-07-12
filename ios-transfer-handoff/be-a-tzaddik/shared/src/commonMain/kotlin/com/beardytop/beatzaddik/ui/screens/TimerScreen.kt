package com.beardytop.beatzaddik.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.beardytop.beatzaddik.ui.components.AppText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.beardytop.beatzaddik.ui.translation.rememberAppTranslatedTemplate
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
        AppText(
            "Kashrut Timer",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = TzaddikColors.GoldBright
        )
        AppText(
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
            AppText(
                "Your wait times",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.GoldBright
            )
            Spacer(Modifier.height(4.dp))
            val meatHours = profile.meatToDairyHours()
            val dairyBadge = KashrutWaitTimes.waitBadgeForDairyToMeatMinutes(profile.dairyToMeatWaitMinutes())
            val dairyWait = rememberAppTranslatedTemplate(dairyBadge.templateKey, dairyBadge.args)
            WaitTimeRow(
                emoji = "🥩",
                text = rememberAppTranslatedTemplate(
                    "After meat: wait {hours} hours before dairy",
                    mapOf("hours" to meatHours.toString()),
                ),
            )
            WaitTimeRow(
                emoji = "🧀",
                text = rememberAppTranslatedTemplate(
                    "After dairy: wait {wait} before meat",
                    mapOf("wait" to dairyWait),
                ),
            )
            Spacer(Modifier.height(4.dp))
            AppText(
                "Wait times reflect your family custom (minhag). Tap below to adjust.",
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.ParchTop.copy(alpha = 0.78f)
            )
            ParchmentTextButton(
                onClick = onOpenKashrutSettings,
                text = "Adjust wait times + other settings →"
            )
        }

        // Active timer or meal log buttons
        AnimatedVisibility(
            visible = wait != null,
            enter = fadeIn(tween(300)) + scaleIn(tween(300), initialScale = 0.9f),
            exit = fadeOut(tween(200)) + scaleOut(tween(200))
        ) {
            wait?.let {
                ActiveTimerCard(
                    wait = it,
                    nowMillis = nowMillis,
                    showNotificationHint = profile.showKashrutTimerNotification,
                    onCancel = { viewModel.cancelKashrut() },
                    onRestart = { viewModel.restartKashrut() },
                    onClear = { viewModel.clearKashrut() },
                    onOpenNotificationSettings = onOpenKashrutSettings,
                )
            }
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
                AppText(
                    "What did you just eat?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = TzaddikColors.ParchTop,
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
                AppText(
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
        AppText(
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
    showNotificationHint: Boolean,
    onCancel: () -> Unit,
    onRestart: () -> Unit,
    onClear: () -> Unit,
    onOpenNotificationSettings: () -> Unit,
) {
    val remaining = (wait.endsAtEpochMillis - nowMillis).coerceAtLeast(0)
    val hours = remaining / 3_600_000L
    val minutes = (remaining % 3_600_000L) / 60_000L
    val seconds = (remaining % 60_000L) / 1_000L
    val isDone = remaining == 0L
    val allowedFood = when (wait.category) {
        MealCategory.MEAT -> "dairy"
        MealCategory.DAIRY -> "meat"
    }

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
        AppText(
            when (wait.category) {
                MealCategory.MEAT -> "Waiting before dairy"
                MealCategory.DAIRY -> "Waiting before meat"
            },
            style = MaterialTheme.typography.titleSmall.copy(letterSpacing = 1.2.sp),
            color = TzaddikColors.GoldBright.copy(alpha = 0.85f)
        )

        if (isDone) {
            AppText(
                "✓ You can now eat $allowedFood",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = TzaddikColors.GoldBright,
                textAlign = TextAlign.Center
            )
            GoldButton(
                onClick = onClear,
                text = "Clear",
                modifier = Modifier.fillMaxWidth()
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
            AppText(
                when (wait.category) {
                    MealCategory.MEAT -> "remaining until dairy"
                    MealCategory.DAIRY -> "remaining until meat"
                },
                style = MaterialTheme.typography.bodySmall,
                color = TzaddikColors.GoldBright.copy(alpha = 0.55f)
            )
            if (showNotificationHint) {
                AppText(
                    "Also shown in your notification bar",
                    style = MaterialTheme.typography.bodySmall,
                    color = TzaddikColors.GoldBright.copy(alpha = 0.45f),
                    textAlign = TextAlign.Center,
                )
            } else {
                ParchmentTextButton(
                    onClick = onOpenNotificationSettings,
                    text = "Show in notification bar →"
                )
            }
            GoldButton(
                onClick = onRestart,
                text = "Restart timer",
                modifier = Modifier.fillMaxWidth()
            )
            ParchmentTextButton(
                onClick = onCancel,
                text = "Cancel timer"
            )
        }
    }
}
