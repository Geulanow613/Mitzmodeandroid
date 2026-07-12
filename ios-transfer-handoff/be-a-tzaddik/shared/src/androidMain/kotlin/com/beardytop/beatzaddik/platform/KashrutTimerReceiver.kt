package com.beardytop.beatzaddik.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.beardytop.beatzaddik.domain.MealCategory

class KashrutTimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val category = intent?.getStringExtra(EXTRA_CATEGORY)?.let {
            runCatching { MealCategory.valueOf(it) }.getOrNull()
        } ?: return
        val showStatus = intent.getBooleanExtra(EXTRA_SHOW_STATUS, true)
        val sound = intent.getBooleanExtra(EXTRA_SOUND, false)
        val vibrate = intent.getBooleanExtra(EXTRA_VIBRATE, true)
        if (showStatus) {
            postKashrutFinishedNotification(context, category, sound = sound, vibrate = vibrate)
        } else {
            KashrutNotifications.dismissStatusNotification()
        }
    }

    companion object {
        const val EXTRA_CATEGORY = "category"
        const val EXTRA_SHOW_STATUS = "show_status"
        const val EXTRA_SOUND = "sound"
        const val EXTRA_VIBRATE = "vibrate"
        const val NOTIFICATION_ID = 9002
    }
}
