package com.beardytop.mitzmode

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.beardytop.mitzmode.tzaddik.TzaddikBridge
import com.beardytop.mitzmode.tzaddik.TzaddikPermissionHost
import com.beardytop.beatzaddik.ui.components.HalachicTermOverlay
import com.beardytop.mitzmode.ui.MitzModeApp
import com.beardytop.mitzmode.ui.translation.ProvideAppTranslation
import com.beardytop.mitzmode.ui.theme.MitzModeTheme
import dagger.hilt.android.AndroidEntryPoint
import io.sentry.Sentry

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        TzaddikPermissionHost.register(this)
        TzaddikBridge.bindActivity(this)

        setContent {
            MitzModeTheme {
                ProvideAppTranslation {
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

        // After first frame is scheduled — Sentry may not be init'd yet; setTag is safe either way.
        Handler(Looper.getMainLooper()).post {
            runCatching { Sentry.setTag("screen", "MainActivity") }
        }
    }

    override fun onDestroy() {
        TzaddikBridge.unbindActivity(this)
        super.onDestroy()
    }
}
