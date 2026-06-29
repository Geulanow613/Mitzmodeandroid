package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.CustomChecklistItem
import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.UserProfile
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    val profile: Flow<UserProfile>
    /** Persisted flag — false until user accepts the first-launch disclaimer. */
    val disclaimerAcknowledged: Flow<Boolean>
    val checklistChecked: Flow<Map<String, Boolean>>
    val customItems: Flow<List<CustomChecklistItem>>
    val customChecked: Flow<Map<String, Boolean>>
    val kashrutWait: Flow<KashrutWait?>

    suspend fun saveProfile(profile: UserProfile)
    suspend fun acknowledgeDisclaimer()
    suspend fun setChecked(id: String, checked: Boolean, persist: Boolean = false)
    suspend fun addCustomItem(title: String)
    suspend fun removeCustomItem(id: String)
    suspend fun setCustomChecked(id: String, checked: Boolean)
    suspend fun setKashrutWait(wait: KashrutWait?)
    suspend fun clearDayIfNewDate(todayKey: String)

    /**
     * Maps monthly-mitzvah item id → the "hebrewYear-hebrewMonth" key stored when it was last checked.
     * Used by ChecklistEngine to determine if the stored check is current or from a previous month.
     */
    val monthlyCheckedMonths: Flow<Map<String, String>>

    /**
     * Persist a monthly-mitzvah check together with the Hebrew month key so the engine can
     * distinguish a same-month check from a stale previous-month check.
     */
    suspend fun setMonthlyChecked(id: String, checked: Boolean, hebrewMonthKey: String)

    /**
     * Maps weekly-mitzvah item id → the "YYYY-MM-DD" date of the Saturday ending the week
     * when it was last checked. Resets automatically when the Saturday key changes.
     */
    val weeklyCheckedWeeks: Flow<Map<String, String>>

    suspend fun setWeeklyChecked(id: String, checked: Boolean, saturdayKey: String)

    /**
     * Maps tzeit-day mitzvah item id → epoch millis (as string) of the tzeit that started the
     * halachic day when it was last checked. Resets when a new tzeit day begins.
     */
    val tzeitCheckedDays: Flow<Map<String, String>>

    suspend fun setTzeitDayChecked(id: String, checked: Boolean, tzeitDayKey: String)
}
