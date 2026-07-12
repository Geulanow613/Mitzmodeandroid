package com.beardytop.beatzaddik.data

import com.beardytop.beatzaddik.domain.CustomChecklistItem
import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.MealCategory
import com.beardytop.beatzaddik.domain.TimeOfDay
import com.beardytop.beatzaddik.domain.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSURL

@Serializable
private data class StoredState(
    val disclaimerAcknowledged: Boolean = false,
    val profile: UserProfile = UserProfile(),
    val checked: Map<String, Boolean> = emptyMap(),
    val persistChecked: Map<String, Boolean> = emptyMap(),
    val customItems: List<CustomChecklistItem> = emptyList(),
    val customChecked: Map<String, Boolean> = emptyMap(),
    val kashrut: KashrutWait? = null,
    val checklistDate: String = "",
    /** Maps monthly-mitzvah item id → "hebrewYear-hebrewMonth" key when last checked. */
    val monthlyMonths: Map<String, String> = emptyMap(),
    /** Maps weekly-mitzvah item id → "YYYY-MM-DD" Saturday key when last checked. */
    val weeklyWeeks: Map<String, String> = emptyMap(),
    /** Maps tzeit-day mitzvah id → tzeit epoch-millis key when last checked. */
    val tzeitDays: Map<String, String> = emptyMap()
)

class JsonFileAppRepository : AppRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val mutex = Mutex()
    private val filePath = documentsDirectory() + "/beatzaddik_state.json"

    private val _disclaimerAcknowledged = MutableStateFlow(false)
    private val _profile = MutableStateFlow(UserProfile())
    private val _checked = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val _custom = MutableStateFlow<List<CustomChecklistItem>>(emptyList())
    private val _customChecked = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    private val _kashrut = MutableStateFlow<KashrutWait?>(null)
    private val _monthlyMonths = MutableStateFlow<Map<String, String>>(emptyMap())
    private val _weeklyWeeks = MutableStateFlow<Map<String, String>>(emptyMap())
    private val _tzeitDays = MutableStateFlow<Map<String, String>>(emptyMap())

    override val disclaimerAcknowledged: Flow<Boolean> = _disclaimerAcknowledged.asStateFlow()

    override val profile: Flow<UserProfile> = _profile.asStateFlow()
    override val checklistChecked = _checked.asStateFlow()
    override val customItems = _custom.asStateFlow()
    override val customChecked = _customChecked.asStateFlow()
    override val kashrutWait = _kashrut.asStateFlow()
    override val monthlyCheckedMonths: Flow<Map<String, String>> = _monthlyMonths.asStateFlow()
    override val weeklyCheckedWeeks: Flow<Map<String, String>> = _weeklyWeeks.asStateFlow()
    override val tzeitCheckedDays: Flow<Map<String, String>> = _tzeitDays.asStateFlow()

    init {
        loadFromDisk()
    }

    override suspend fun saveProfile(profile: UserProfile) = update { it.copy(profile = profile) }

    override suspend fun acknowledgeDisclaimer() = update { it.copy(disclaimerAcknowledged = true) }

    override suspend fun setChecked(id: String, checked: Boolean, persist: Boolean) = update {
        if (persist) {
            val daily = it.checked.toMutableMap().apply { remove(id) }
            val persistent = it.persistChecked.toMutableMap().apply { put(id, checked) }
            it.copy(checked = daily, persistChecked = persistent)
        } else {
            val persistent = it.persistChecked.toMutableMap().apply { remove(id) }
            val daily = it.checked.toMutableMap().apply { put(id, checked) }
            it.copy(checked = daily, persistChecked = persistent)
        }
    }

    override suspend fun addCustomItem(title: String) = update {
        val id = "custom_${Clock.System.now().toEpochMilliseconds()}"
        it.copy(customItems = it.customItems + CustomChecklistItem(id, title, TimeOfDay.ANY))
    }

    override suspend fun removeCustomItem(id: String) = update {
        it.copy(
            customItems = it.customItems.filter { c -> c.id != id },
            customChecked = it.customChecked.filterKeys { k -> k != id }
        )
    }

    override suspend fun setCustomChecked(id: String, checked: Boolean) = update {
        it.copy(customChecked = it.customChecked.toMutableMap().apply { put(id, checked) })
    }

    override suspend fun setKashrutWait(wait: KashrutWait?) = update { it.copy(kashrut = wait) }

    override suspend fun setMonthlyChecked(id: String, checked: Boolean, hebrewMonthKey: String) = update {
        val persist = it.persistChecked.toMutableMap().apply { put(id, checked) }
        val months = it.monthlyMonths.toMutableMap().apply { put(id, hebrewMonthKey) }
        it.copy(persistChecked = persist, monthlyMonths = months)
    }

    override suspend fun setWeeklyChecked(id: String, checked: Boolean, saturdayKey: String) = update {
        val persist = it.persistChecked.toMutableMap().apply { put(id, checked) }
        val weeks = it.weeklyWeeks.toMutableMap().apply { put(id, saturdayKey) }
        it.copy(persistChecked = persist, weeklyWeeks = weeks)
    }

    override suspend fun setTzeitDayChecked(id: String, checked: Boolean, tzeitDayKey: String) = update {
        val persist = it.persistChecked.toMutableMap().apply { put(id, checked) }
        val days = it.tzeitDays.toMutableMap().apply { put(id, tzeitDayKey) }
        it.copy(persistChecked = persist, tzeitDays = days)
    }

    override suspend fun clearDayIfNewDate(todayKey: String) = update {
        if (it.checklistDate == todayKey) return@update it
        it.copy(
            checklistDate = todayKey,
            checked = emptyMap(),
            customChecked = emptyMap()
        )
    }

    private suspend fun update(block: (StoredState) -> StoredState) {
        mutex.withLock {
            val next = block(readState())
            writeState(next)
            publish(next)
        }
    }

    private fun readState(): StoredState = try {
        val content = NSString.stringWithContentsOfFile(filePath, encoding = NSUTF8StringEncoding, error = null)
        if (content != null) {
            json.decodeFromString<StoredState>(normalizeProfileJson(content.toString()))
        } else StoredState()
    } catch (_: Exception) {
        StoredState()
    }

    private fun writeState(state: StoredState) {
        val text = json.encodeToString(state)
        (text as NSString).writeToFile(filePath, atomically = true, encoding = NSUTF8StringEncoding, error = null)
    }

    private fun loadFromDisk() {
        val state = readState()
        publish(state)
    }

    private fun publish(state: StoredState) {
        _disclaimerAcknowledged.value = state.disclaimerAcknowledged
        _profile.value = state.profile
        _checked.value = state.checked + state.persistChecked
        _custom.value = state.customItems
        _customChecked.value = state.customChecked
        _kashrut.value = state.kashrut
        _monthlyMonths.value = state.monthlyMonths
        _weeklyWeeks.value = state.weeklyWeeks
        _tzeitDays.value = state.tzeitDays
    }

    // Daily rollover is handled by AppViewModel via clearDayIfNewDate() using a tzeit-based key.
}

private fun documentsDirectory(): String {
    val url: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null
    )
    return url?.path ?: "/tmp"
}
