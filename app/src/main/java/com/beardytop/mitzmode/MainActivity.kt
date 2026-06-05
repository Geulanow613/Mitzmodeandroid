package com.beardytop.mitzmode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.beardytop.mitzmode.tzaddik.TzaddikBridge
import com.beardytop.mitzmode.tzaddik.TzaddikPermissionHost
import com.beardytop.beatzaddik.ui.components.HalachicTermOverlay
import com.beardytop.mitzmode.ui.LocalTranslationViewModel
import com.beardytop.mitzmode.ui.MitzModeApp
import com.beardytop.mitzmode.ui.translation.ProvideAppTranslation
import com.beardytop.mitzmode.ui.theme.MitzModeTheme
import com.beardytop.mitzmode.viewmodel.TranslationViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import io.sentry.Sentry

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        TzaddikPermissionHost.register(this)
        TzaddikBridge.bindActivity(this)
        
        // Initialize crash reporting only if we have network
        if (isNetworkAvailable()) {
            try {
                Sentry.setTag("screen", "MainActivity")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        setContent {
            MitzModeTheme {
                val translationViewModel: TranslationViewModel = hiltViewModel()
                CompositionLocalProvider(LocalTranslationViewModel provides translationViewModel) {
                    ProvideAppTranslation(translationViewModel) {
                        HalachicTermOverlay {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                MitzModeApp()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        TzaddikBridge.unbindActivity(this)
        super.onDestroy()
    }
    
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}