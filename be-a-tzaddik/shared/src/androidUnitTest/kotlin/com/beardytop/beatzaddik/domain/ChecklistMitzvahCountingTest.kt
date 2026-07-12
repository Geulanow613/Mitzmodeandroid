package com.beardytop.beatzaddik.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChecklistMitzvahCountingTest {

    @Test
    fun entryId_isStableForSameItemAndDay() {
        val a = ChecklistMitzvahCounting.entryId("netilat_yadayim", "1720000000000")
        val b = ChecklistMitzvahCounting.entryId("netilat_yadayim", "1720000000000")
        assertEquals(a, b)
        assertEquals("checklist_count_netilat_yadayim_1720000000000", a)
    }

    @Test
    fun alreadyCounted_trueOnlyForSameDayEntry() {
        val day = "1720000000000"
        val ids = listOf(
            ChecklistMitzvahCounting.entryId("tzitzit", day),
            "checklist_tzitzit_${System.currentTimeMillis()}", // legacy spam id — ignored
        )
        assertTrue(ChecklistMitzvahCounting.alreadyCounted(ids, "tzitzit", day))
        assertFalse(ChecklistMitzvahCounting.alreadyCounted(ids, "tzitzit", "999"))
        assertFalse(ChecklistMitzvahCounting.alreadyCounted(ids, "other", day))
    }
}
