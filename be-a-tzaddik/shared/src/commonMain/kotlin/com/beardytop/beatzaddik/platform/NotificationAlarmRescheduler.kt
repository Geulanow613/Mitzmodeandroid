package com.beardytop.beatzaddik.platform

import com.beardytop.beatzaddik.AppDependencies
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock

/**
 * Rebuilds the kashrut AlarmManager / local notification schedule after process death or reboot.
 * Shared by [com.beardytop.beatzaddik.viewmodel.AppViewModel] startup and Android [BootReceiver].
 */
object NotificationAlarmRescheduler {
    suspend fun rescheduleKashrut(deps: AppDependencies) {
        val p = deps.repository.profile.first()
        val wait = deps.repository.kashrutWait.first()
        val now = Clock.System.now().toEpochMilliseconds()
        when {
            wait == null -> deps.kashrut.cancelNotification()
            wait.endsAtEpochMillis <= now -> deps.kashrut.showFinishedNotification(wait, p, deps.calendar, now)
            else -> deps.kashrut.scheduleEndNotification(wait, p, deps.calendar)
        }
    }
}
