package com.beardytop.beatzaddik.data

import beatzaddik.shared.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
suspend fun loadCityGeographyJson(): String = withContext(Dispatchers.Default) {
    Res.readBytes("files/city-geography.json").decodeToString()
}
