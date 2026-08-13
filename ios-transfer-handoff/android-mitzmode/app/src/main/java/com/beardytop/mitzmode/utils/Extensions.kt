package com.beardytop.mitzmode.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

fun Context.openUrl(url: String) {
    if (url.startsWith("mailto:", ignoreCase = true)) {
        openExternalUri(url)
        return
    }
    if (isNetworkAvailable()) {
        openExternalUri(url)
    } else {
        Toast.makeText(
            this,
            "No internet connection available. Please check your connection and try again.",
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun Context.openExternalUri(uri: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(
            this,
            "Unable to open link. Please try again later.",
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
} 