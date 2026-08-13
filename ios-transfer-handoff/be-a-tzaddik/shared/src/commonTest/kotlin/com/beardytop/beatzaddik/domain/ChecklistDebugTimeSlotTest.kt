package com.beardytop.beatzaddik.domain

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChecklistDebugTimeSlotTest {

    @Test
    fun timeSlotsMapToDistinctChecklistPeriods() {
        assertEquals(TimeOfDay.NIGHT, ChecklistDebugTimeSlot.PRE_DAWN.toTimeOfDay())
        assertEquals(TimeOfDay.DAY, ChecklistDebugTimeSlot.MORNING.toTimeOfDay())
        assertEquals(TimeOfDay.AFTERNOON, ChecklistDebugTimeSlot.AFTERNOON.toTimeOfDay())
        assertEquals(TimeOfDay.NIGHT, ChecklistDebugTimeSlot.EVENING.toTimeOfDay())
        assertEquals(TimeOfDay.NIGHT, ChecklistDebugTimeSlot.NIGHT.toTimeOfDay())
    }

    @Test
    fun epochMillisAtChangesWithTimeSlot() {
        val date = LocalDate(2026, 7, 2)
        val preDawn = ChecklistDebugDateFinder.epochMillisAt(date, ChecklistDebugTimeSlot.PRE_DAWN, "America/New_York")
        val morning = ChecklistDebugDateFinder.epochMillisAt(date, ChecklistDebugTimeSlot.MORNING, "America/New_York")
        val afternoon = ChecklistDebugDateFinder.epochMillisAt(date, ChecklistDebugTimeSlot.AFTERNOON, "America/New_York")
        val night = ChecklistDebugDateFinder.epochMillisAt(date, ChecklistDebugTimeSlot.NIGHT, "America/New_York")
        assertTrue(morning > preDawn)
        assertTrue(afternoon > morning)
        assertTrue(night > afternoon)
    }

    @Test
    fun motzeiPhaseDisplayLabel() {
        val scenario = ChecklistDebugScenarios.byId("yom_kippur_motzei")
        requireNotNull(scenario)
        assertEquals(ChecklistDebugPhase.MOTZEI, scenario.phase)
        assertEquals("Motzei — Yom Kippur", ChecklistDebugScenarios.displayLabel(scenario))
    }

    @Test
    fun searchMatchesHolidayLabelPhaseAndTwoDaysBefore() {
        val roshHashanaDay = requireNotNull(ChecklistDebugScenarios.byId("rosh_hashana_day"))
        val roshHashanaMotzei = requireNotNull(ChecklistDebugScenarios.byId("rosh_hashana_motzei"))
        val roshHashanaTwoDays = requireNotNull(ChecklistDebugScenarios.byId("rosh_hashana_day_2d"))

        assertTrue(ChecklistDebugScenarios.matchesSearch(roshHashanaDay, "hash"))
        assertTrue(ChecklistDebugScenarios.matchesSearch(roshHashanaMotzei, "motzei"))
        assertTrue(ChecklistDebugScenarios.matchesSearch(roshHashanaTwoDays, "before"))
        assertTrue(!ChecklistDebugScenarios.matchesSearch(roshHashanaDay, "motzei"))
    }
}
