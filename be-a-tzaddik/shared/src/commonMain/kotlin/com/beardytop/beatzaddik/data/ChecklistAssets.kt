package com.beardytop.beatzaddik.data

import beatzaddik.shared.generated.resources.Res
import com.beardytop.beatzaddik.domain.TextEncodingFixes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
suspend fun loadChecklistJson(): String = withContext(Dispatchers.Default) {
    TextEncodingFixes.repairJsonPayload(
        Res.readBytes("files/checklist-items.json").decodeToString(),
    )
}

@OptIn(ExperimentalResourceApi::class)
suspend fun loadNusachExtrasJson(): String = withContext(Dispatchers.Default) {
    Res.readBytes("files/nusach-extras.json").decodeToString()
}

@OptIn(ExperimentalResourceApi::class)
suspend fun loadHolidaysJson(): String = withContext(Dispatchers.Default) {
    runCatching { Res.readBytes("files/holidays-overlay.json").decodeToString() }
        .getOrElse { """{"version":1,"upcoming":[]}""" }
}
