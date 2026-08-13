package com.beardytop.beatzaddik.ui.tour

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned

/** Named spots the first-run tour can spotlight. */
enum class TourTarget {
    HeaderUpcoming,
    Checklist,
    NavTimer,
    NavBless,
    NavMitzvah,
}

fun interface TourTargetReporter {
    fun report(target: TourTarget, boundsInRoot: Rect)
}

val LocalTourTargetReporter = staticCompositionLocalOf<TourTargetReporter?> { null }

/** Reports this node's root bounds to the active tour, if any. */
fun Modifier.reportTourTarget(
    target: TourTarget,
    reporter: TourTargetReporter?,
): Modifier {
    if (reporter == null) return this
    return onGloballyPositioned { coords ->
        reporter.report(target, coords.boundsInRoot())
    }
}
