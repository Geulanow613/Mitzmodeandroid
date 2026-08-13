package com.beardytop.beatzaddik.platform

/** One-shot elevation lookup when GPS does not report altitude. */
expect suspend fun fetchElevationMeters(latitude: Double, longitude: Double): Double?
