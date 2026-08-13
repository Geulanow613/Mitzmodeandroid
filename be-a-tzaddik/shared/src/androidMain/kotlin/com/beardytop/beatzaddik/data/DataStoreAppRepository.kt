package com.beardytop.beatzaddik.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.beardytop.beatzaddik.domain.CustomChecklistItem
import com.beardytop.beatzaddik.domain.Gender
import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.LocationSource
import com.beardytop.beatzaddik.domain.MealCategory
import com.beardytop.beatzaddik.domain.NusachSelection
import com.beardytop.beatzaddik.domain.TimeOfDay
import com.beardytop.beatzaddik.domain.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("beatzaddik_prefs")

class DataStoreAppRepository(private val context: Context) : AppRepository {

    private val json = Json { ignoreUnknownKeys = true }

    private object Keys {
        val disclaimerAcknowledged = booleanPreferencesKey("disclaimer_acknowledged")
        val profile = stringPreferencesKey("profile_json")
        val checklistDate = stringPreferencesKey("checklist_date")
        val checklistPrefix = "check_"
        val persistPrefix = "persist_check_"
        val customItems = stringPreferencesKey("custom_items_json")
        val customPrefix = "custom_check_"
        val kashrut = stringPreferencesKey("kashrut_json")
        /** Stores the "hebrewYear-hebrewMonth" key for each monthly mitzvah when last checked. */
        val monthlyMonthPrefix = "monthly_month_"
        /** Stores the "YYYY-MM-DD" Saturday key for each weekly mitzvah when last checked. */
        val weeklyWeekPrefix = "weekly_week_"
        /** Stores the tzeit epoch-millis key for tzeit-to-tzeit daily mitzvot when last checked. */
        val tzeitDayPrefix = "tzeit_day_"
    }

    override val profile: Flow<UserProfile> = context.dataStore.data.map { prefs ->
        prefs[Keys.profile]?.let { json.decodeFromString<UserProfile>(normalizeProfileJson(it)) }
            ?: UserProfile()
    }

    override val disclaimerAcknowledged: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.disclaimerAcknowledged] == true
    }

    override val checklistChecked: Flow<Map<String, Boolean>> = context.dataStore.data.map { prefs ->
        mergeCheckedMaps(prefs.asMap())
    }

    override val customItems: Flow<List<CustomChecklistItem>> = context.dataStore.data.map { prefs ->
        prefs[Keys.customItems]?.let {
            json.decodeFromString<List<CustomChecklistItem>>(it)
        } ?: emptyList()
    }

    override val customChecked: Flow<Map<String, Boolean>> = context.dataStore.data.map { prefs ->
        prefs.asMap()
            .filter { (key, _) -> key.name.startsWith(Keys.customPrefix) }
            .mapKeys { (key, _) -> key.name.removePrefix(Keys.customPrefix) }
            .mapValues { (_, value) -> preferenceBooleanTrue(value) }
    }

    override val kashrutWait: Flow<KashrutWait?> = context.dataStore.data.map { prefs ->
        prefs[Keys.kashrut]?.let { raw ->
            val parts = raw.split("|")
            if (parts.size == 3) {
                KashrutWait(
                    MealCategory.valueOf(parts[0]),
                    parts[1].toLong(),
                    parts[2].toLong()
                )
            } else null
        }
    }

    override suspend fun saveProfile(profile: UserProfile) {
        context.dataStore.edit { it[Keys.profile] = json.encodeToString(profile) }
    }

    override suspend fun acknowledgeDisclaimer() {
        context.dataStore.edit { it[Keys.disclaimerAcknowledged] = true }
    }

    override suspend fun setChecked(id: String, checked: Boolean, persist: Boolean) {
        if (id.isBlank()) return
        context.dataStore.edit { prefs ->
            val dailyKey = booleanPreferencesKey(Keys.checklistPrefix + id)
            val persistKey = booleanPreferencesKey(Keys.persistPrefix + id)
            if (persist) {
                prefs[persistKey] = checked
                prefs[dailyKey] = false
            } else {
                prefs[dailyKey] = checked
            }
        }
    }

    override suspend fun addCustomItem(title: String) {
        val id = "custom_${Clock.System.now().toEpochMilliseconds()}"
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.customItems]?.let {
                json.decodeFromString<List<CustomChecklistItem>>(it)
            } ?: emptyList()
            prefs[Keys.customItems] = json.encodeToString(
                current + CustomChecklistItem(id, title, TimeOfDay.ANY)
            )
        }
    }

    override suspend fun removeCustomItem(id: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.customItems]?.let {
                json.decodeFromString<List<CustomChecklistItem>>(it)
            } ?: emptyList()
            prefs[Keys.customItems] = json.encodeToString(current.filter { it.id != id })
            prefs[booleanPreferencesKey(Keys.customPrefix + id)] = false
        }
    }

    override suspend fun setCustomChecked(id: String, checked: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey(Keys.customPrefix + id)] = checked
        }
    }

    override suspend fun setKashrutWait(wait: KashrutWait?) {
        context.dataStore.edit { prefs ->
            if (wait == null) prefs.remove(Keys.kashrut)
            else prefs[Keys.kashrut] = "${wait.category}|${wait.startedAtEpochMillis}|${wait.endsAtEpochMillis}"
        }
    }

    override suspend fun clearDayIfNewDate(todayKey: String) {
        context.dataStore.edit { prefs ->
            val stored = prefs[Keys.checklistDate]
            if (stored != todayKey) {
                prefs.asMap().keys
                    .map { it.name }
                    .filter { name ->
                        name.startsWith(Keys.checklistPrefix) || name.startsWith(Keys.customPrefix)
                    }
                    .forEach { name -> prefs[booleanPreferencesKey(name)] = false }
                prefs[Keys.checklistDate] = todayKey
            }
        }
    }

    override val monthlyCheckedMonths: Flow<Map<String, String>> = context.dataStore.data.map { prefs ->
        prefs.asMap()
            .filter { (key, _) -> key.name.startsWith(Keys.monthlyMonthPrefix) }
            .mapKeys { (key, _) -> key.name.removePrefix(Keys.monthlyMonthPrefix) }
            .mapValues { (_, value) -> value as? String ?: "" }
    }

    override suspend fun setMonthlyChecked(id: String, checked: Boolean, hebrewMonthKey: String) {
        if (id.isBlank()) return
        context.dataStore.edit { prefs ->
            val persistKey = booleanPreferencesKey(Keys.persistPrefix + id)
            val monthKey = stringPreferencesKey(Keys.monthlyMonthPrefix + id)
            prefs[persistKey] = checked
            prefs[monthKey] = hebrewMonthKey
        }
    }

    override val weeklyCheckedWeeks: Flow<Map<String, String>> = context.dataStore.data.map { prefs ->
        prefs.asMap()
            .filter { (key, _) -> key.name.startsWith(Keys.weeklyWeekPrefix) }
            .mapKeys { (key, _) -> key.name.removePrefix(Keys.weeklyWeekPrefix) }
            .mapValues { (_, value) -> value as? String ?: "" }
    }

    override suspend fun setWeeklyChecked(id: String, checked: Boolean, saturdayKey: String) {
        if (id.isBlank()) return
        context.dataStore.edit { prefs ->
            val persistKey = booleanPreferencesKey(Keys.persistPrefix + id)
            val weekKey = stringPreferencesKey(Keys.weeklyWeekPrefix + id)
            prefs[persistKey] = checked
            prefs[weekKey] = saturdayKey
        }
    }

    override val tzeitCheckedDays: Flow<Map<String, String>> = context.dataStore.data.map { prefs ->
        prefs.asMap()
            .filter { (key, _) -> key.name.startsWith(Keys.tzeitDayPrefix) }
            .mapKeys { (key, _) -> key.name.removePrefix(Keys.tzeitDayPrefix) }
            .mapValues { (_, value) -> value as? String ?: "" }
    }

    override suspend fun setTzeitDayChecked(id: String, checked: Boolean, tzeitDayKey: String) {
        if (id.isBlank()) return
        context.dataStore.edit { prefs ->
            val persistKey = booleanPreferencesKey(Keys.persistPrefix + id)
            val dayKey = stringPreferencesKey(Keys.tzeitDayPrefix + id)
            prefs[persistKey] = checked
            prefs[dayKey] = tzeitDayKey
        }
    }

    /** Persist map wins when present; avoids NPE on corrupt null boolean values. */
    private fun mergeCheckedMaps(raw: Map<Preferences.Key<*>, Any?>): Map<String, Boolean> {
        val ids = mutableSetOf<String>()
        raw.keys.forEach { key ->
            when {
                key.name.startsWith(Keys.checklistPrefix) ->
                    ids.add(key.name.removePrefix(Keys.checklistPrefix))
                key.name.startsWith(Keys.persistPrefix) ->
                    ids.add(key.name.removePrefix(Keys.persistPrefix))
            }
        }
        return ids.associateWith { id ->
            val persistKey = booleanPreferencesKey(Keys.persistPrefix + id)
            val dailyKey = booleanPreferencesKey(Keys.checklistPrefix + id)
            when {
                raw.containsKey(persistKey) -> preferenceBooleanTrue(raw[persistKey])
                else -> preferenceBooleanTrue(raw[dailyKey])
            }
        }
    }

    private fun preferenceBooleanTrue(value: Any?): Boolean = value === true

    override suspend fun warmStartupReads() {
        context.dataStore.data.first()
    }
}

actual fun createAppRepository(platformContext: Any?): AppRepository {
    val ctx = platformContext as Context
    return DataStoreAppRepository(ctx)
}
