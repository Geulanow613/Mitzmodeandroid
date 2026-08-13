package com.beardytop.beatzaddik.domain

data class ManualCity(
    val id: String,
    val label: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneId: String,
    /** Approximate elevation in meters for zmanim sunrise/sunset adjustment. */
    val elevationMeters: Double? = null,
)
