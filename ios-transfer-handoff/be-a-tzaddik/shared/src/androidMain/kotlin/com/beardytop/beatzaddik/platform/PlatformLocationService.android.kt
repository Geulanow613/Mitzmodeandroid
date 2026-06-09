package com.beardytop.beatzaddik.platform

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import java.util.TimeZone
import kotlin.coroutines.resume

actual class PlatformLocationService(private val context: Context) {

    actual var onPermissionResult: ((granted: Boolean) -> Unit)? = null

    actual fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED
    }

    actual fun requestLocationPermission() {
        if (hasLocationPermission()) {
            onPermissionResult?.invoke(true)
            onPermissionResult = null
            return
        }
        permissionRequestHandler?.invoke()
    }

    actual fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    actual suspend fun getCurrentLocation(): LocationResult {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fine != PackageManager.PERMISSION_GRANTED && coarse != PackageManager.PERMISSION_GRANTED) {
            return LocationResult.PermissionDenied
        }

        return suspendCancellableCoroutine { cont ->
            val client = LocationServices.getFusedLocationProviderClient(context)
            val token = CancellationTokenSource()
            client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, token.token)
                .addOnSuccessListener { loc ->
                    if (loc == null) {
                        cont.resume(LocationResult.Unavailable)
                        return@addOnSuccessListener
                    }
                    val tz = TimeZone.getDefault().id
                    val label = try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                                ?.firstOrNull()?.locality
                        } else {
                            @Suppress("DEPRECATION")
                            Geocoder(context, Locale.getDefault())
                                .getFromLocation(loc.latitude, loc.longitude, 1)
                                ?.firstOrNull()?.locality
                        }
                    } catch (_: Exception) {
                        null
                    }
                    val hasAltitude = loc.hasAltitude()
                    cont.resume(
                        LocationResult.Success(
                            LocationSuccess(
                                latitude = loc.latitude,
                                longitude = loc.longitude,
                                timezoneId = tz,
                                label = label,
                                elevationMeters = if (hasAltitude) loc.altitude else null,
                                hasAltitudeReading = hasAltitude,
                            )
                        )
                    )
                }
                .addOnFailureListener {
                    cont.resume(LocationResult.Unavailable)
                }
        }
    }

    companion object {
        /** Set from [com.beardytop.beatzaddik.MainActivity] to launch the system permission dialog. */
        var permissionRequestHandler: (() -> Unit)? = null

        fun notifyPermissionResult(granted: Boolean) {
            // Handler is set on the singleton service instance created in MainActivity.
            lastInstance?.let { service ->
                service.onPermissionResult?.invoke(granted)
                service.onPermissionResult = null
            }
        }

        private var lastInstance: PlatformLocationService? = null
    }

    init {
        lastInstance = this
    }
}
