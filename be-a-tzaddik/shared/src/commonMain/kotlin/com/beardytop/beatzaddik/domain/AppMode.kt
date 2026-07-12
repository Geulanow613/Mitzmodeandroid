package com.beardytop.beatzaddik.domain

/**
 * How the checklist shell is hosted.
 * - [Standalone]: Be a Tzaddik store app — Today | Timer | Settings | About
 * - [Unified]: Shipping Mitz Mode — full 7-tab shell with generator / blessings / status
 * - [Embedded]: Legacy overlay inside old Mitz Mode home (center tab closes overlay)
 */
enum class AppMode {
    Standalone,
    Unified,
    Embedded,
}

object MitzModeFeatures {
    /** Milestone reward videos (counts 1–1000). Keep false; flip to re-enable. */
    const val milestoneVideosEnabled: Boolean = false

    /** Add-a-Mitzvah submission UI. Keep false; flip to re-enable. */
    const val addMitzvahEnabled: Boolean = false

    /** Final reward at 1800 + certificate replay. */
    const val finalRewardEnabled: Boolean = true

    /** Old Mitz Mode Compose home (floating Mitzvah Me). Keep false. */
    const val legacyHomeEnabled: Boolean = false

    /**
     * Ship full offline he/es/fr/ru translation JSON (~9MB). Keep false for store builds;
     * full catalogs remain under `data/bundled-translations/` to re-enable later.
     * Online Google Translate in Settings still works when this is false.
     */
    const val bundledOfflineTranslationsEnabled: Boolean = false
}
