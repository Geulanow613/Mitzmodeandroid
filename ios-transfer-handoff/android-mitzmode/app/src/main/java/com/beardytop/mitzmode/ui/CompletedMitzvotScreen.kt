package com.beardytop.mitzmode.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import com.beardytop.mitzmode.data.Mitzvah
import com.beardytop.mitzmode.ui.components.LowEndDeviceBackground
import com.beardytop.mitzmode.ui.components.TranslatableText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedMitzvotScreen(
    completedMitzvot: List<Mitzvah>,
    onBack: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LowEndDeviceBackground()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    TranslatableText(
                        text = if (completedMitzvot.size == 1) "One Mitzvah!" else "Mazel Tov!",
                        color = Color(0xFF39FF14),
                        fontSize = 24.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF39FF14)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(completedMitzvot) { mitzvah ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.5f)
                        )
                    ) {
                        TranslatableText(
                            text = mitzvah.text,
                            color = Color(0xFF39FF14),
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 18.sp,
                        )
                    }
                }
            }
        }
    }
}
