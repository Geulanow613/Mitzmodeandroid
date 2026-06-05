package com.beardytop.beatzaddik.platform

import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

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

    actual suspend fun getCurrentLocation(): LocationResult = LocationResult.Unavailable

    actual fun openAppSettings() {
        val url = NSURL.URLWithString(platform.UIKit.UIApplicationOpenSettingsURLString) ?: return
        if (UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url, emptyMap<Any?, Any?>(), null)
        }
    }
}
