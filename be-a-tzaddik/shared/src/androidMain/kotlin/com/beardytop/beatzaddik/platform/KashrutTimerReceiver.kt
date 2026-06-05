package com.beardytop.beatzaddik.platform

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.beardytop.beatzaddik.domain.MealCategory

class KashrutTimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val category = intent?.getStringExtra(EXTRA_CATEGORY)?.let {
            runCatching { MealCategory.valueOf(it) }.getOrNull()
        }
        val channelId = "kashrut_timer"
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(
            NotificationChannel(channelId, "Kashrut timer", NotificationManager.IMPORTANCE_DEFAULT)
        )
        val message = when (category) {
            MealCategory.MEAT -> "You may eat dairy now (confirm with your rabbi)."
            MealCategory.DAIRY -> "You may eat meat now (confirm with your rabbi)."
            else -> "Kashrut waiting period ended."
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Be a Tzaddik")
            .setContentText(message)
            .setAutoCancel(true)
            .build()
        nm.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val EXTRA_CATEGORY = "category"
        const val NOTIFICATION_ID = 9002
    }
}
