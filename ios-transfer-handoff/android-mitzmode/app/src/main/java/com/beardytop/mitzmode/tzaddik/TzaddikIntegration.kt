package com.beardytop.mitzmode.tzaddik

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.beardytop.beatzaddik.App
import com.beardytop.beatzaddik.AppDependencies
import com.beardytop.mitzmode.R
import com.beardytop.beatzaddik.platform.PlatformActivityHolder
import com.beardytop.beatzaddik.platform.PlatformLocationService
import com.beardytop.beatzaddik.platform.initKashrutNotifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object TzaddikBridge {
    private val mutex = Mutex()
    private val _deps = MutableStateFlow<AppDependencies?>(null)
    val depsFlow: StateFlow<AppDependencies?> = _deps.asStateFlow()

    var deps: AppDependencies?
        get() = _deps.value
        private set(value) {
            _deps.value = value
        }

    private var locationService: PlatformLocationService? = null
    private val preloadScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    @Volatile
    private var preloadStarted = false

    /** Start loading checklist deps at app launch so opening the embed is near-instant. */
    fun preload(application: Application) {
        if (_deps.value != null || preloadStarted) return
        preloadStarted = true
        initKashrutNotifications(application.applicationContext)
        if (locationService == null) {
            locationService = PlatformLocationService(application.applicationContext)
        }
        preloadScope.launch {
            runCatching { ensureDependencies(application.applicationContext) }
        }
    }

    fun bindActivity(activity: ComponentActivity) {
        PlatformActivityHolder.bind(activity)
        initKashrutNotifications(activity.applicationContext)
        if (locationService == null) {
            locationService = PlatformLocationService(activity.applicationContext)
        }
        PlatformLocationService.permissionRequestHandler = {
            TzaddikPermissionHost.requestFromActivity(activity)
        }
        preload(activity.application)
    }

    fun unbindActivity(activity: ComponentActivity) {
        PlatformActivityHolder.unbind(activity)
    }

    suspend fun ensureDependencies(context: Context): AppDependencies {
        _deps.value?.let { return it }
        return mutex.withLock {
            _deps.value ?: run {
                val appContext = context.applicationContext
                val service = locationService ?: PlatformLocationService(appContext).also {
                    locationService = it
                }
                AppDependencies.create(
                    platformContext = appContext,
                    locationService = service,
                    embeddedMode = true,
                ).also { deps = it }
            }
        }
    }

    suspend fun ensureDependencies(activity: ComponentActivity): AppDependencies =
        ensureDependencies(activity.applicationContext)
}

object TzaddikPermissionHost {
    private var launcher: ActivityResultLauncher<Array<String>>? = null
    private var pendingActivity: ComponentActivity? = null

    fun register(activity: ComponentActivity) {
        pendingActivity = activity
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { grants ->
            val locationGranted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (grants.keys.any {
                    it == Manifest.permission.ACCESS_FINE_LOCATION ||
                        it == Manifest.permission.ACCESS_COARSE_LOCATION
                }
            ) {
                PlatformLocationService.notifyPermissionResult(locationGranted)
            }
        }
    }

    fun requestFromActivity(activity: ComponentActivity) {
        val needed = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            needed += Manifest.permission.ACCESS_FINE_LOCATION
            needed += Manifest.permission.ACCESS_COARSE_LOCATION
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            needed += Manifest.permission.POST_NOTIFICATIONS
        }
        if (needed.isNotEmpty()) {
            launcher?.launch(needed.toTypedArray())
            return
        }
        val locationGranted =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
        if (locationGranted) {
            PlatformLocationService.notifyPermissionResult(true)
        }
    }
}

@Composable
fun EmbeddedTzaddikChecklist(
    activity: ComponentActivity,
    onDismiss: () -> Unit
) {
    val deps by TzaddikBridge.depsFlow.collectAsState()

    LaunchedEffect(activity) {
        TzaddikBridge.ensureDependencies(activity)
    }

    LaunchedEffect(deps) {
        if (deps != null) {
            TzaddikPermissionHost.requestFromActivity(activity)
        }
    }

    val ready = deps
    if (ready == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .navigationBarsPadding()
        ) {
            App(
                deps = ready,
                embeddedMode = true,
                onRequestClose = onDismiss,
                returnToMainIcon = { MitzModeSilverEmbossedIcon() }
            )
        }
    }
}

@Composable
private fun MitzModeSilverEmbossedIcon() {
    Image(
        painter = painterResource(id = R.drawable.app_icon),
        contentDescription = "Return to Mitz Mode",
        modifier = Modifier
            .width(91.dp)
            .height(56.dp),
        contentScale = ContentScale.Fit,
        alpha = 0.80f
    )
}
