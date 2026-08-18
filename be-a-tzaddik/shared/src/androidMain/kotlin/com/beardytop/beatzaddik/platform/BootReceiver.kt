package com.beardytop.beatzaddik.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.beardytop.beatzaddik.AppDependencies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * AlarmManager exact alarms do not survive reboot. Rebuild the kashrut schedule
 * once the device is up (app does not need to be opened first).
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED && action != ACTION_QUICKBOOT_POWERON) {
            return
        }
        val appContext = context.applicationContext
        val pending = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                initKashrutNotifications(appContext)
                val deps = AppDependencies.create(
                    platformContext = appContext,
                    locationService = PlatformLocationService(appContext),
                    embeddedMode = true,
                )
                NotificationAlarmRescheduler.rescheduleKashrut(deps)
            } catch (_: Throwable) {
                // Best-effort — next cold start / AppViewModel maintenance will retry.
            } finally {
                pending.finish()
            }
        }
    }

    companion object {
        /** Some OEM boot broadcasts (e.g. older HTC / Xiaomi). */
        private const val ACTION_QUICKBOOT_POWERON = "android.intent.action.QUICKBOOT_POWERON"
    }
}
