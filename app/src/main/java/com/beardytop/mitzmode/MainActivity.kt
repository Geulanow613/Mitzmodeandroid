package com.beardytop.mitzmode

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.beardytop.beatzaddik.MitzModeActivityContent
import com.beardytop.beatzaddik.MitzModeActivityHost

/** Android launcher shell — all product UI lives in sharedmodule. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var composeSplashReady = false
        splashScreen.setKeepOnScreenCondition { !composeSplashReady }

        MitzModeActivityHost.onCreate(this, intent)

        setContent {
            MitzModeActivityContent(
                onStartupLoadingVisible = { composeSplashReady = true },
            )
        }

        window.decorView.post { MitzModeActivityHost.tagMainActivityScreen() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        MitzModeActivityHost.onNewIntent(intent)
    }

    override fun onDestroy() {
        MitzModeActivityHost.onDestroy(this)
        super.onDestroy()
    }
}
