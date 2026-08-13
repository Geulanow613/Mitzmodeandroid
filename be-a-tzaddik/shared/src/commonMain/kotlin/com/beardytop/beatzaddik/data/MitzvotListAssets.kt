package com.beardytop.beatzaddik.data

import beatzaddik.shared.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

object MitzvotListAssets {
    @OptIn(ExperimentalResourceApi::class)
    suspend fun loadBundledJson(): String =
        Res.readBytes("files/mitzvotlistfull.json").decodeToString()
}
