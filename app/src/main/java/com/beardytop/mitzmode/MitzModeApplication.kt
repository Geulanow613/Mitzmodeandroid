package com.beardytop.mitzmode

import android.app.Application
import com.beardytop.beatzaddik.MitzModeApplicationConfig
import com.beardytop.beatzaddik.MitzModeApplicationHost

/** Android application shell — lifecycle logic lives in sharedmodule. */
class MitzModeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MitzModeApplicationHost.onCreate(
            this,
            MitzModeApplicationConfig(
                sentryDsn = BuildConfig.SENTRY_DSN,
                isDebug = BuildConfig.DEBUG,
            ),
        )
    }

    override fun onLowMemory() {
        super.onLowMemory()
        MitzModeApplicationHost.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        MitzModeApplicationHost.onTrimMemory(level)
    }

    override fun onTerminate() {
        MitzModeApplicationHost.onTerminate()
        super.onTerminate()
    }
}
