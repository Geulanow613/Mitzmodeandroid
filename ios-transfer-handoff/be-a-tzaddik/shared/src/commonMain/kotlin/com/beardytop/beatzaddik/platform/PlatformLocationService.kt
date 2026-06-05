package com.beardytop.beatzaddik.platform

data class LocationSuccess(
    val latitude: Double,
    val longitude: Double,
    val timezoneId: String,
    val label: String?
)

sealed class LocationResult {
    data class Success(val data: LocationSuccess) : LocationResult()
    data object PermissionDenied : LocationResult()
    data object Unavailable : LocationResult()
}

expect class PlatformLocationService {
    /** Called after [requestLocationPermission] when the user responds to the system dialog. */
    var onPermissionResult: ((granted: Boolean) -> Unit)?

    /** True if fine or coarse location is already granted. */
    fun hasLocationPermission(): Boolean

    /** Shows the system location permission dialog when not yet granted. */
    fun requestLocationPermission()

    suspend fun getCurrentLocation(): LocationResult

    /** Opens the OS app-settings page so the user can grant location permission. */
    fun openAppSettings()
}
