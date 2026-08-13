package com.beardytop.beatzaddik.data

import beatzaddik.shared.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
suspend fun loadBundledTranslationsJson(languageCode: String): String = withContext(Dispatchers.Default) {
    Res.readBytes("files/translations/$languageCode.json").decodeToString()
}
