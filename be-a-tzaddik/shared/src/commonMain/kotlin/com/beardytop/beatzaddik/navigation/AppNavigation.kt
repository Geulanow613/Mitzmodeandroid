package com.beardytop.beatzaddik.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Cross-platform deep-link targets for opening a bottom-nav tab
 * (e.g. from an Android kashrut-timer notification).
 */
object AppNavigation {
    /** Bottom-nav index for the Kashrut Timer screen in all app modes. */
    const val TAB_TIMER = 1

    /** Intent extra key (Android) — value [OPEN_TAB_TIMER]. */
    const val EXTRA_OPEN_TAB = "com.beardytop.beatzaddik.OPEN_TAB"
    const val OPEN_TAB_TIMER = "timer"

    private val _pendingTab = MutableStateFlow<Int?>(null)
    val pendingTab: StateFlow<Int?> = _pendingTab.asStateFlow()

    fun requestTab(tabIndex: Int) {
        _pendingTab.value = tabIndex
    }

    fun requestTimerTab() = requestTab(TAB_TIMER)

    fun consumePendingTab(): Int? {
        val value = _pendingTab.value ?: return null
        _pendingTab.value = null
        return value
    }
}
