package com.beardytop.beatzaddik

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.beardytop.beatzaddik.platform.KashrutNotifications
import com.beardytop.beatzaddik.platform.PlatformActivityHolder
import com.beardytop.beatzaddik.platform.PlatformLocationService
import com.beardytop.beatzaddik.platform.applyLauncherIcon
import com.beardytop.beatzaddik.platform.flushPendingLauncherAliasDisable
import com.beardytop.beatzaddik.platform.handleAppNavigationIntent
import com.beardytop.beatzaddik.platform.initKashrutNotifications
import com.beardytop.beatzaddik.platform.recordLauncherEntryIntent
import androidx.core.app.NotificationManagerCompat
import com.beardytop.beatzaddik.ui.components.HalachicTermOverlay
import com.beardytop.beatzaddik.android.translation.ProvideAppTranslation
import com.beardytop.beatzaddik.viewmodel.TranslationViewModel
import com.beardytop.beatzaddik.viewmodel.TranslationViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        val locationGranted = grants[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            grants[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (grants.keys.any { it == Manifest.permission.ACCESS_FINE_LOCATION || it == Manifest.permission.ACCESS_COARSE_LOCATION }) {
            PlatformLocationService.notifyPermissionResult(locationGranted)
        }
        if (grants.containsKey(Manifest.permission.POST_NOTIFICATIONS)) {
            KashrutNotifications.notifyPermissionResult(
                grants[Manifest.permission.POST_NOTIFICATIONS] == true,
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recordLauncherEntryIntent(this)
        PlatformActivityHolder.bind(this)
        enableEdgeToEdge()
        initKashrutNotifications(applicationContext)
        handleAppNavigationIntent(intent)
        val locationService = PlatformLocationService(applicationContext)
        PlatformLocationService.permissionRequestHandler = { requestNeededPermissions() }
        KashrutNotifications.permissionRequestHandler = { requestNotificationPermission() }
        requestNeededPermissions()

        val deps = runBlocking {
            AppDependencies.create(
                platformContext = applicationContext,
                locationService = locationService,
            )
        }
        setContent {
            val translationViewModel: TranslationViewModel = viewModel(
                factory = TranslationViewModelFactory(applicationContext),
            )
            ProvideAppTranslation(translationViewModel) {
                HalachicTermOverlay {
                    App(deps)
                }
            }
        }

        lifecycleScope.launch {
            applyLauncherIcon(deps.repository.profile.first().gender)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAppNavigationIntent(intent)
    }

    private fun requestNeededPermissions() {
        val needed = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            needed += Manifest.permission.ACCESS_FINE_LOCATION
            needed += Manifest.permission.ACCESS_COARSE_LOCATION
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            needed += Manifest.permission.POST_NOTIFICATIONS
        }
        if (needed.isNotEmpty()) permissionLauncher.launch(needed.toTypedArray())
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            return
        }
        KashrutNotifications.notifyPermissionResult(
            NotificationManagerCompat.from(this).areNotificationsEnabled(),
        )
    }

    override fun onStop() {
        flushPendingLauncherAliasDisable(this)
        super.onStop()
    }

    override fun onDestroy() {
        PlatformActivityHolder.unbind(this)
        super.onDestroy()
    }
}
