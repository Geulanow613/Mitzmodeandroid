package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.domain.LocationTimezone
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.Foundation.NSTimeZone
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
private class SingleLocationDelegate(
    private val onResult: (CLLocation?) -> Unit,
) : NSObject(), CLLocationManagerDelegateProtocol {

    private var delivered = false

    private fun deliver(location: CLLocation?) {
        if (delivered) return
        delivered = true
        onResult(location)
    }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val location = didUpdateLocations.lastOrNull() as? CLLocation
        deliver(location)
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        deliver(null)
    }
}

actual class PlatformLocationService {
    actual var onPermissionResult: ((granted: Boolean) -> Unit)? = null

    private val locationManager = CLLocationManager()

    actual fun hasLocationPermission(): Boolean {
        return when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> true
            else -> false
        }
    }

    actual fun requestLocationPermission() {
        when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> {
                onPermissionResult?.invoke(true)
                onPermissionResult = null
            }
            kCLAuthorizationStatusNotDetermined -> locationManager.requestWhenInUseAuthorization()
            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> {
                onPermissionResult?.invoke(false)
                onPermissionResult = null
            }
            else -> {
                onPermissionResult?.invoke(false)
                onPermissionResult = null
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun getCurrentLocation(): LocationResult {
        if (!hasLocationPermission()) {
            return LocationResult.PermissionDenied
        }

        return suspendCancellableCoroutine { continuation ->
            val manager = CLLocationManager()
            val delegate = SingleLocationDelegate { location ->
                if (!continuation.isActive) return@SingleLocationDelegate
                if (location == null) {
                    continuation.resume(LocationResult.Unavailable)
                    return@SingleLocationDelegate
                }
                val deviceTz = NSTimeZone.localTimeZone.name
                val tz = LocationTimezone.resolve(location.coordinate.latitude, location.coordinate.longitude, deviceTz)
                val hasAltitude = location.verticalAccuracy >= 0.0
                continuation.resume(
                    LocationResult.Success(
                        LocationSuccess(
                            latitude = location.coordinate.latitude,
                            longitude = location.coordinate.longitude,
                            timezoneId = tz,
                            label = null,
                            elevationMeters = if (hasAltitude) location.altitude else null,
                            hasAltitudeReading = hasAltitude,
                        )
                    )
                )
            }
            manager.delegate = delegate
            manager.desiredAccuracy = kCLLocationAccuracyHundredMeters
            manager.requestLocation()

            continuation.invokeOnCancellation {
                manager.delegate = null
                manager.stopUpdatingLocation()
            }
        }
    }

    actual fun openAppSettings() {
        val url = NSURL.URLWithString(platform.UIKit.UIApplicationOpenSettingsURLString) ?: return
        if (UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url, emptyMap<Any?, Any?>(), null)
        }
    }
}
