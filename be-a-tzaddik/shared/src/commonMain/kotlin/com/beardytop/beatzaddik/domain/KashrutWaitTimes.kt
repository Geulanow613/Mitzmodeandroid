package com.beardytop.beatzaddik.domain

/** Meat / dairy separation wait helpers. */
object KashrutWaitTimes {
    /** After dairy → before meat: 30 minutes, then 1–12 hours (hourly only). */
    val dairyToMeatMinuteOptions: List<Int> =
        listOf(30) + (1..12).map { it * 60 }

    fun coerceDairyToMeatMinutes(minutes: Int): Int =
        dairyToMeatMinuteOptions.minBy { kotlin.math.abs(it - minutes) }

    /** Legacy profiles stored 1–12 as whole hours; newer values are minutes (30, 60, …). */
    fun resolveDairyToMeatMinutes(stored: Int?): Int? = stored?.let { value ->
        if (value in 1..12) value * 60 else value
    }

    fun formatDairyToMeatWait(minutes: Int): String = when {
        minutes < 60 -> "$minutes min"
        minutes % 60 == 0 -> {
            val h = minutes / 60
            if (h == 1) "1h" else "${h}h"
        }
        else -> "$minutes min"
    }

    fun dairyToMeatOptionIndex(minutes: Int): Int {
        val resolved = coerceDairyToMeatMinutes(minutes)
        return dairyToMeatMinuteOptions.indexOf(resolved).coerceAtLeast(0)
    }
}
