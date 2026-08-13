package com.beardytop.mitzmode.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import org.junit.Rule
import org.junit.Test

/**
 * Records the classes and methods used during a cold start so they can be AOT-compiled at install
 * time. Without this, ART verifies and JITs them on every cold start — the dominant cost in
 * startup traces.
 *
 * Regenerate with `gradlew :app:generateBaselineProfile`; the result lands in
 * `app/src/release/generated/baselineProfiles/` and should be committed.
 */
class StartupBaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun startup() = rule.collect(
        packageName = "com.beardytop.mitzmode",
        includeInStartupProfile = true,
    ) {
        pressHome()
        startActivityAndWait()

        // The aesthetic splash holds for several seconds and the real UI composes underneath it,
        // so idle alone returns long before the interesting classes have loaded.
        device.waitForIdle()
        Thread.sleep(SPLASH_AND_FIRST_COMPOSITION_MILLIS)
        device.waitForIdle()
    }

    private companion object {
        const val SPLASH_AND_FIRST_COMPOSITION_MILLIS = 12_000L
    }
}
