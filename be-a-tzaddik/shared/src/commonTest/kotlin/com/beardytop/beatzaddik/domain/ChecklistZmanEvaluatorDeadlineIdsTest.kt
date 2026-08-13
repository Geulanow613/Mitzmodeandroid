package com.beardytop.beatzaddik.domain

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Deadline rows must stay in [ChecklistZmanEvaluator.appliesTo] so festival-prep /
 * Chol HaMoed bypasses cannot leave them ACTIVE forever.
 */
class ChecklistZmanEvaluatorDeadlineIdsTest {

    @Test
    fun timedSeasonalAndErevRows_areCoveredByEvaluator() {
        val ids = listOf(
            "sukkot_arba_minim",
            "hoshana_rabbah_aravot",
            "erev_yom_kippur_eat",
            "erev_tisha_beav_prep",
            "erev_pesach_prepare_seder",
            "erev_chag_prep",
            "erev_pesach_mechirat_chametz",
            "zecher_machatzit_hashekel_5786",
            "yom_tov_shabbat_advance_prep",
            "erev_chanukah_prep",
            "erev_pesach_taanit_bechorot",
            "purim_meshulash_friday_matanot",
        )
        ids.forEach { id ->
            assertTrue(ChecklistZmanEvaluator.appliesTo(id), "expected appliesTo($id)")
        }
    }

    @Test
    fun softPrepRows_withoutHardDeadline_needNotBeInEvaluator() {
        // Still festival-prep bypassed intentionally (all-day soft reminders).
        assertFalse(ChecklistZmanEvaluator.appliesTo("erev_purim_prep"))
        assertFalse(ChecklistZmanEvaluator.appliesTo("erev_public_fast_prep"))
    }
}
