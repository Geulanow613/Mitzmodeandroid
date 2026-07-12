package com.beardytop.mitzmode

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.beardytop.mitzmode.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import io.sentry.Sentry
import io.sentry.android.core.SentryAndroid
import io.sentry.protocol.User
import com.jakewharton.threetenabp.AndroidThreeTen
import com.beardytop.mitzmode.util.VideoManager
import com.beardytop.mitzmode.util.SentryUtil
import com.beardytop.mitzmode.tzaddik.TzaddikBridge
import android.os.Handler
import android.os.Looper
import android.os.MessageQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class MitzModeApplication : Application() {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val sentryInitialized = AtomicBoolean(false)
    private val sentryInitScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        
        // Initialize ThreeTenABP first (doesn't require network)
        AndroidThreeTen.init(this)

        // Warm checklist deps after first frames — keeps cold start responsive.
        mainHandler.postDelayed({ TzaddikBridge.preload(this) }, 1_500L)
        
        // Init Sentry after the UI is up — keeps crash reporting, avoids ~1s main-thread block at launch.
        scheduleSentryStartup()
    }

    private fun scheduleSentryStartup() {
        if (!isNetworkAvailable()) {
            Log.d("MitzModeApplication", "No network available, skipping Sentry initialization")
            return
        }
        mainHandler.postDelayed({ tryInitializeSentryWhenIdle() }, SENTRY_INIT_DELAY_MS)
        // Fallback if the user keeps scrolling — still init Sentry, just not mid-gesture.
        mainHandler.postDelayed({ tryInitializeSentry(force = true) }, SENTRY_FORCE_INIT_MS)
    }

    private fun tryInitializeSentryWhenIdle() {
        if (sentryInitialized.get()) return
        Looper.myQueue().addIdleHandler(
            MessageQueue.IdleHandler {
                tryInitializeSentry(force = true)
                false
            }
        )
    }

    private fun tryInitializeSentry(force: Boolean) {
        if (!sentryInitialized.compareAndSet(false, true)) return
        // Sentry init can block the main thread on some devices/emulators.
        // Run it off the UI thread; if it fails, we continue without it.
        sentryInitScope.launch {
            initializeSentry()
            try {
                SentryUtil.startAnrWatchdog(startupGracePeriodMs = 5_000L)
                Log.d("MitzModeApplication", "ANR watchdog started successfully")
            } catch (e: Exception) {
                Log.e("MitzModeApplication", "Failed to start ANR watchdog", e)
            }
        }
    }

    companion object {
        /** Earliest Sentry init — after cold-start JIT / first frame. */
        private const val SENTRY_INIT_DELAY_MS = 10_000L
        /** Latest Sentry init — avoids waiting forever if the user scrolls continuously. */
        private const val SENTRY_FORCE_INIT_MS = 30_000L
    }
    
    override fun onLowMemory() {
        super.onLowMemory()
        Log.w("MitzModeApplication", "Low memory detected - releasing video resources")
        
        // Log memory pressure to Sentry
        SentryUtil.logMessage(
            "Low memory event triggered",
            "warning",
            mapOf(
                "memory_event" to "low_memory",
                "action" to "releasing_resources"
            )
        )
        
        // Release all video resources when system is low on memory
        try {
            VideoManager.getInstance(this).onLowMemory()
        } catch (e: Exception) {
            Log.e("MitzModeApplication", "Error handling low memory", e)
            SentryUtil.logError(e, mapOf("context" to "low_memory_handling"))
        }
        
        // Force garbage collection
        System.gc()
    }
    
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        
        val levelName = when (level) {
            ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN -> "UI_HIDDEN"
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE -> "RUNNING_MODERATE"
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW -> "RUNNING_LOW"
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> "RUNNING_CRITICAL"
            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND -> "BACKGROUND"
            ComponentCallbacks2.TRIM_MEMORY_MODERATE -> "MODERATE"
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE -> "COMPLETE"
            else -> "UNKNOWN_$level"
        }
        
        Log.d("MitzModeApplication", "Memory trim requested: $levelName")
        
        // Log memory pressure events (only for significant pressure, not routine UI changes)
        // UI_HIDDEN is a normal lifecycle event and should not be logged to Sentry
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // Only log UI_HIDDEN to Android Log, not Sentry (reduces noise)
            Log.d("MitzModeApplication", "UI hidden - normal app lifecycle event")
        } else if (
            level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW ||
            level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL ||
            level == ComponentCallbacks2.TRIM_MEMORY_BACKGROUND ||
            level == ComponentCallbacks2.TRIM_MEMORY_MODERATE ||
            level == ComponentCallbacks2.TRIM_MEMORY_COMPLETE
        ) {
            val logLevel = when (level) {
                ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
                ComponentCallbacks2.TRIM_MEMORY_MODERATE,
                ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> "warning"
                ComponentCallbacks2.TRIM_MEMORY_BACKGROUND,
                ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW -> "info"
                else -> "debug"
            }
            
            SentryUtil.logMessage(
                "Memory pressure detected: $levelName",
                logLevel,
                mapOf(
                    "memory_event" to "trim_memory",
                    "trim_level" to levelName,
                    "level_value" to level.toString()
                )
            )
        }
        
        // Trim levels are not monotonic (e.g. UI_HIDDEN=20 > RUNNING_CRITICAL=15), so never use >=.
        when (level) {
            ComponentCallbacks2.TRIM_MEMORY_COMPLETE,
            ComponentCallbacks2.TRIM_MEMORY_MODERATE,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL -> {
                try {
                    VideoManager.getInstance(this).onLowMemory()
                    System.gc()
                } catch (e: Exception) {
                    Log.e("MitzModeApplication", "Error during critical memory trim", e)
                }
            }
            ComponentCallbacks2.TRIM_MEMORY_BACKGROUND,
            ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW -> {
                // Do not release the main background ExoPlayer here: Android sends
                // TRIM_MEMORY_BACKGROUND whenever the user leaves the app, which would
                // tear down the wallpaper video and leave a blank surface on return.
                // Heavy release remains for COMPLETE / MODERATE / RUNNING_CRITICAL above.
                Log.d("MitzModeApplication", "Memory trim ($levelName) — keeping background video player")
            }
        }
    }
    
    override fun onTerminate() {
        super.onTerminate()
        
        // Stop ANR watchdog when app terminates
        try {
            SentryUtil.stopAnrWatchdog()
            Log.d("MitzModeApplication", "ANR watchdog stopped")
        } catch (e: Exception) {
            Log.e("MitzModeApplication", "Error stopping ANR watchdog", e)
        }
    }
    
    private fun isNetworkAvailable(): Boolean {
        return try {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (e: Exception) {
            Log.e("MitzModeApplication", "Error checking network availability", e)
            false
        }
    }
    
    private fun initializeSentry() {
        try {
            val dsn = BuildConfig.SENTRY_DSN
            if (dsn.isBlank() || dsn == "YOUR_SENTRY_DSN_HERE" || !dsn.startsWith("https://")) {
                Log.d("MitzModeApplication", "No valid Sentry DSN configured, skipping initialization")
                return
            }
            SentryAndroid.init(this) { options ->
                options.dsn = BuildConfig.SENTRY_DSN
                options.isEnableAutoSessionTracking = true
                options.connectionTimeoutMillis = 3000
                options.readTimeoutMillis = 3000
                options.isAnrEnabled = true
                options.anrTimeoutIntervalMillis = 4000
                // ART method tracing used by profiling can crash on some devices/emulators
                // (tlsPtr_.method_trace_buffer check failed). See getsentry/sentry-java#3653.
                options.profilesSampleRate = 0.0
                options.isEnableUserInteractionTracing = false
                options.isEnableFramesTracking = !BuildConfig.DEBUG
                options.maxBreadcrumbs = if (BuildConfig.DEBUG) 30 else 100
                if (BuildConfig.DEBUG) {
                    options.tracesSampleRate = 0.0
                    // Fewer integrations = faster init; crash/ANR reporting unchanged.
                    options.isEnableSystemEventBreadcrumbs = false
                    options.isEnableNetworkEventBreadcrumbs = false
                } else {
                    options.tracesSampleRate = 0.1
                }
            }
            
            // Add additional context
            Sentry.configureScope { scope ->
                // Add device info
                scope.setTag("device_manufacturer", android.os.Build.MANUFACTURER)
                scope.setTag("device_model", android.os.Build.MODEL)
                scope.setTag("android_version", android.os.Build.VERSION.RELEASE)
                scope.setTag("app_version", packageManager.getPackageInfo(packageName, 0).versionName ?: "unknown")
            }

            // Note: We'll add user tracking later when we implement user authentication
            // For now, we'll just track anonymous usage
            Sentry.setUser(User().apply {
                ipAddress = "{{auto}}"
                // You can also add other anonymous user data if needed:
                // username = "anonymous"
                // id = UUID.randomUUID().toString()
            })
            
            Log.d("MitzModeApplication", "Sentry initialized successfully")
        } catch (e: Exception) {
            // If Sentry fails to initialize, just continue without it
            Log.e("MitzModeApplication", "Failed to initialize Sentry: ${e.message}")
            e.printStackTrace()
        }
    }
} 