package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.GeneratorMitzvah

/**
 * Persists accepted mitzvot in the same store Android Mitz Mode already uses
 * (`mitzvot_prefs` / `completed_mitzvot`) so upgrades keep user progress.
 */
expect class MitzvotCountStore(platformContext: Any?) {
    suspend fun loadCompleted(): List<GeneratorMitzvah>
    suspend fun saveCompleted(mitzvot: List<GeneratorMitzvah>)
}
