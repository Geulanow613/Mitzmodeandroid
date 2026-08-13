package com.beardytop.beatzaddik.platform

import android.content.Intent
import com.beardytop.beatzaddik.navigation.AppNavigation

/** Reads deep-link extras from a launch / [onNewIntent] and queues in-app navigation. */
fun handleAppNavigationIntent(intent: Intent?) {
    if (intent == null) return
    when (intent.getStringExtra(AppNavigation.EXTRA_OPEN_TAB)) {
        AppNavigation.OPEN_TAB_TIMER -> {
            AppNavigation.requestTimerTab()
            intent.removeExtra(AppNavigation.EXTRA_OPEN_TAB)
        }
    }
}
