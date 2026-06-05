package com.beardytop.beatzaddik.platform

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.UserProfile

private var appContext: Context? = null

fun initKashrutNotifications(context: Context) {
    appContext = context.applicationContext
}

actual object KashrutNotifications {
    actual fun schedule(wait: KashrutWait, profile: UserProfile) {
        val ctx = appContext ?: return
        val alarm = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, KashrutTimerReceiver::class.java).apply {
            putExtra(KashrutTimerReceiver.EXTRA_CATEGORY, wait.category.name)
        }
        val pending = PendingIntent.getBroadcast(
            ctx,
            9001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarm.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            wait.endsAtEpochMillis,
            pending
        )
    }

    actual fun cancel() {
        val ctx = appContext ?: return
        val intent = Intent(ctx, KashrutTimerReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            ctx,
            9001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        (ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pending)
    }
}
