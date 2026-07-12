package com.beardytop.mitzmode

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.beardytop.mitzmode.tzaddik.TzaddikBridge
import com.beardytop.mitzmode.tzaddik.TzaddikPermissionHost
import com.beardytop.beatzaddik.platform.handleAppNavigationIntent
import com.beardytop.beatzaddik.ui.components.HalachicTermOverlay
import com.beardytop.mitzmode.ui.AfterFirstFrame
import com.beardytop.mitzmode.ui.MitzModeRoot
import com.beardytop.mitzmode.ui.translation.ProvideAppTranslation
import com.beardytop.mitzmode.ui.theme.MitzModeTheme
import dagger.hilt.android.AndroidEntryPoint
import io.sentry.Sentry

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleAppNavigationIntent(intent)

        // Must register before STARTED; keep lightweight.
        TzaddikPermissionHost.register(this)

        // Permission dialog must be wired before checklist can request GPS.
        TzaddikBridge.bindActivity(this@MainActivity)

        setContent {
            MitzModeTheme {
                ProvideAppTranslation {
                    AfterFirstFrame {
                        HalachicTermOverlay {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = MaterialTheme.colorScheme.background
                            ) {
                                MitzModeRoot()
                            }
                        }
                    }
                }
            }
        }

        window.decorView.post {
            runCatching { Sentry.setTag("screen", "MainActivity") }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAppNavigationIntent(intent)
    }

    override fun onDestroy() {
        TzaddikBridge.unbindActivity(this)
        super.onDestroy()
    }
}
