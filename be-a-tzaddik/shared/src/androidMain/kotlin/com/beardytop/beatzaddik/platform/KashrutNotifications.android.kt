package com.beardytop.beatzaddik.platform

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.MealCategory
import com.beardytop.beatzaddik.domain.UserProfile
import com.beardytop.beatzaddik.navigation.AppNavigation

private var appContext: Context? = null

private const val CHANNEL_STATUS = "kashrut_timer_status"
/** Sound + vibrate finish alert. */
private const val CHANNEL_DONE_SV = "kashrut_timer_done_sv"
/** Sound only. */
private const val CHANNEL_DONE_S = "kashrut_timer_done_s"
/** Vibrate only. */
private const val CHANNEL_DONE_V = "kashrut_timer_done_v"
/** Silent finish (banner only). */
private const val CHANNEL_DONE_QUIET = "kashrut_timer_done_quiet"
private const val ALARM_REQUEST_CODE = 9001
const val KASHRUT_STATUS_NOTIFICATION_ID = 9002

fun initKashrutNotifications(context: Context) {
    appContext = context.applicationContext
    ensureChannels(context.applicationContext)
}

private fun ensureChannels(ctx: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    nm.createNotificationChannel(
        NotificationChannel(
            CHANNEL_STATUS,
            "Kashrut timer status",
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = "Ongoing meat/dairy wait countdown in the notification bar"
            setShowBadge(false)
            setSound(null, null)
            enableVibration(false)
        }
    )
    fun doneChannel(
        id: String,
        name: String,
        importance: Int,
        sound: Boolean,
        vibrate: Boolean,
    ) = NotificationChannel(id, name, importance).apply {
        description = "Alert when the meat/dairy wait ends"
        enableVibration(vibrate)
        if (vibrate) {
            vibrationPattern = longArrayOf(0, 250, 150, 250)
        }
        if (sound) {
            setSound(
                Settings.System.DEFAULT_NOTIFICATION_URI,
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build(),
            )
        } else {
            setSound(null, null)
        }
    }
    nm.createNotificationChannel(
        doneChannel(CHANNEL_DONE_SV, "Kashrut timer (sound + vibrate)", NotificationManager.IMPORTANCE_DEFAULT, sound = true, vibrate = true),
    )
    nm.createNotificationChannel(
        doneChannel(CHANNEL_DONE_S, "Kashrut timer (sound)", NotificationManager.IMPORTANCE_DEFAULT, sound = true, vibrate = false),
    )
    nm.createNotificationChannel(
        doneChannel(CHANNEL_DONE_V, "Kashrut timer (vibrate)", NotificationManager.IMPORTANCE_DEFAULT, sound = false, vibrate = true),
    )
    nm.createNotificationChannel(
        doneChannel(CHANNEL_DONE_QUIET, "Kashrut timer (silent)", NotificationManager.IMPORTANCE_LOW, sound = false, vibrate = false),
    )
}

private fun doneChannelId(sound: Boolean, vibrate: Boolean): String = when {
    sound && vibrate -> CHANNEL_DONE_SV
    sound -> CHANNEL_DONE_S
    vibrate -> CHANNEL_DONE_V
    else -> CHANNEL_DONE_QUIET
}

private fun canPostNotifications(ctx: Context): Boolean {
    if (!NotificationManagerCompat.from(ctx).areNotificationsEnabled()) return false
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
    return ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
}

@android.annotation.SuppressLint("MissingPermission")
private fun safeNotify(ctx: Context, id: Int, notification: android.app.Notification) {
    if (!canPostNotifications(ctx)) return
    try {
        NotificationManagerCompat.from(ctx).notify(id, notification)
    } catch (_: SecurityException) {
        // User revoked POST_NOTIFICATIONS or notifications disabled.
    }
}

actual object KashrutNotifications {
    /** Set from MainActivity / TzaddikPermissionHost to launch the system permission dialog. */
    var permissionRequestHandler: (() -> Unit)? = null

    private var pendingPermissionResult: ((Boolean) -> Unit)? = null

    fun notifyPermissionResult(granted: Boolean) {
        val cb = pendingPermissionResult
        pendingPermissionResult = null
        cb?.invoke(granted && areNotificationsAllowed())
    }

    actual fun areNotificationsAllowed(): Boolean {
        val ctx = appContext ?: return false
        return canPostNotifications(ctx)
    }

    actual fun requestPermission(onResult: (granted: Boolean) -> Unit) {
        if (areNotificationsAllowed()) {
            onResult(true)
            return
        }
        val ctx = appContext
        if (ctx == null) {
            onResult(false)
            return
        }
        // Pre-13: no runtime permission — only the app-level notifications switch.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            onResult(false)
            return
        }
        pendingPermissionResult = onResult
        val handler = permissionRequestHandler
        if (handler != null) {
            handler.invoke()
        } else {
            pendingPermissionResult = null
            onResult(false)
        }
    }

    actual fun openNotificationSettings() {
        val ctx = appContext ?: return
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, ctx.packageName)
            putExtra("app_package", ctx.packageName)
            putExtra("app_uid", ctx.applicationInfo.uid)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            ctx.startActivity(intent)
        } catch (_: Exception) {
            ctx.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", ctx.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                },
            )
        }
    }

    actual fun schedule(wait: KashrutWait, profile: UserProfile) {
        val ctx = appContext ?: return
        ensureChannels(ctx)
        val alarm = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, KashrutTimerReceiver::class.java).apply {
            putExtra(KashrutTimerReceiver.EXTRA_CATEGORY, wait.category.name)
            putExtra(
                KashrutTimerReceiver.EXTRA_SHOW_STATUS,
                profile.showKashrutTimerNotification,
            )
            putExtra(KashrutTimerReceiver.EXTRA_SOUND, profile.kashrutTimerSound)
            putExtra(KashrutTimerReceiver.EXTRA_VIBRATE, profile.kashrutTimerVibrate)
        }
        val pending = PendingIntent.getBroadcast(
            ctx,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        alarm.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            wait.endsAtEpochMillis,
            pending,
        )
        if (profile.showKashrutTimerNotification) {
            postOngoing(ctx, wait, profile.kashrutTimerSound, profile.kashrutTimerVibrate)
        } else {
            dismissStatusNotification()
        }
    }

    actual fun cancel() {
        val ctx = appContext ?: return
        val intent = Intent(ctx, KashrutTimerReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            ctx,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        (ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pending)
        dismissStatusNotification()
    }

    actual fun showFinished(wait: KashrutWait, profile: UserProfile) {
        val ctx = appContext ?: return
        ensureChannels(ctx)
        val intent = Intent(ctx, KashrutTimerReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            ctx,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        (ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pending)
        if (profile.showKashrutTimerNotification) {
            postFinished(ctx, wait.category, profile.kashrutTimerSound, profile.kashrutTimerVibrate)
        } else {
            dismissStatusNotification()
        }
    }

    actual fun dismissStatusNotification() {
        val ctx = appContext ?: return
        NotificationManagerCompat.from(ctx).cancel(KASHRUT_STATUS_NOTIFICATION_ID)
    }

    private fun postOngoing(
        ctx: Context,
        wait: KashrutWait,
        sound: Boolean,
        vibrate: Boolean,
    ) {
        val now = System.currentTimeMillis()
        if (wait.endsAtEpochMillis <= now) {
            postFinished(ctx, wait.category, sound, vibrate)
            return
        }
        val allowed = oppositeLabel(wait.category)
        val notification = NotificationCompat.Builder(ctx, CHANNEL_STATUS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Kashrut timer")
            .setContentText("Waiting before $allowed")
            .setContentIntent(launchAppPendingIntent(ctx))
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSilent(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setUsesChronometer(true)
            .setChronometerCountDown(true)
            .setWhen(wait.endsAtEpochMillis)
            .setShowWhen(true)
            .build()
        safeNotify(ctx, KASHRUT_STATUS_NOTIFICATION_ID, notification)
    }

    private fun postFinished(
        ctx: Context,
        category: MealCategory,
        sound: Boolean,
        vibrate: Boolean,
    ) {
        val allowed = oppositeLabel(category)
        val channelId = doneChannelId(sound, vibrate)
        val builder = NotificationCompat.Builder(ctx, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Kashrut timer")
            .setContentText("You can now eat $allowed")
            .setContentIntent(launchAppPendingIntent(ctx))
            .setOngoing(false)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
        if (!sound && !vibrate) {
            builder.setSilent(true)
        } else {
            var defaults = 0
            if (sound) defaults = defaults or NotificationCompat.DEFAULT_SOUND
            if (vibrate) defaults = defaults or NotificationCompat.DEFAULT_VIBRATE
            builder.setDefaults(defaults)
            if (!sound) builder.setSound(null)
            if (!vibrate) builder.setVibrate(null)
        }
        safeNotify(ctx, KASHRUT_STATUS_NOTIFICATION_ID, builder.build())
    }

    private fun launchAppPendingIntent(ctx: Context): PendingIntent {
        val launch = (ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)
            ?: Intent()).apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
            putExtra(AppNavigation.EXTRA_OPEN_TAB, AppNavigation.OPEN_TAB_TIMER)
            addFlags(
                Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK,
            )
        }
        return PendingIntent.getActivity(
            ctx,
            /* requestCode */ 9100,
            launch,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun oppositeLabel(category: MealCategory): String = when (category) {
        MealCategory.MEAT -> "dairy"
        MealCategory.DAIRY -> "meat"
    }
}

fun postKashrutFinishedNotification(
    ctx: Context,
    category: MealCategory,
    sound: Boolean = false,
    vibrate: Boolean = true,
) {
    ensureChannels(ctx)
    val allowed = when (category) {
        MealCategory.MEAT -> "dairy"
        MealCategory.DAIRY -> "meat"
    }
    val launch = (ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)
        ?: Intent()).apply {
        action = Intent.ACTION_MAIN
        addCategory(Intent.CATEGORY_LAUNCHER)
        putExtra(AppNavigation.EXTRA_OPEN_TAB, AppNavigation.OPEN_TAB_TIMER)
        addFlags(
            Intent.FLAG_ACTIVITY_SINGLE_TOP or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK,
        )
    }
    val content = PendingIntent.getActivity(
        ctx,
        /* requestCode */ 9100,
        launch,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
    val channelId = doneChannelId(sound, vibrate)
    val builder = NotificationCompat.Builder(ctx, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Kashrut timer")
        .setContentText("You can now eat $allowed")
        .setContentIntent(content)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
    if (!sound && !vibrate) {
        builder.setSilent(true)
    } else {
        var defaults = 0
        if (sound) defaults = defaults or NotificationCompat.DEFAULT_SOUND
        if (vibrate) defaults = defaults or NotificationCompat.DEFAULT_VIBRATE
        builder.setDefaults(defaults)
        if (!sound) builder.setSound(null)
        if (!vibrate) builder.setVibrate(null)
    }
    safeNotify(ctx, KASHRUT_STATUS_NOTIFICATION_ID, builder.build())
}
