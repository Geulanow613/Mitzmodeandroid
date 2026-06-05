package com.beardytop.beatzaddik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beardytop.beatzaddik.AppDependencies
import com.beardytop.beatzaddik.domain.CustomChecklistItem
import com.beardytop.beatzaddik.domain.DayChecklists
import com.beardytop.beatzaddik.domain.Gender
import com.beardytop.beatzaddik.domain.KashrutWait
import com.beardytop.beatzaddik.domain.KashrutWaitTimes
import com.beardytop.beatzaddik.domain.LocationSource
import com.beardytop.beatzaddik.domain.ManualCities
import com.beardytop.beatzaddik.domain.MealCategory
import com.beardytop.beatzaddik.domain.NusachSelection
import com.beardytop.beatzaddik.domain.UpcomingHoliday
import com.beardytop.beatzaddik.domain.UserProfile
import com.beardytop.beatzaddik.platform.LocationResult
import com.beardytop.beatzaddik.platform.applyLauncherIcon
import com.beardytop.beatzaddik.domain.ElectronicsRestPeriod
import com.beardytop.beatzaddik.domain.HolyDayPhoneRules
import com.beardytop.beatzaddik.domain.RestKind
import com.beardytop.beatzaddik.domain.SHABBAT_REST_TITLE
import com.beardytop.beatzaddik.domain.shabbatMessage
import com.beardytop.beatzaddik.ui.theme.TextScaleDefaults
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class AppViewModel(private val deps: AppDependencies) : ViewModel() {

    val profile: StateFlow<UserProfile> = deps.repository.profile.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile()
    )

    private val _prefsLoaded = MutableStateFlow(false)

    val showDisclaimerDialog: StateFlow<Boolean> = combine(
        _prefsLoaded,
        deps.repository.disclaimerAcknowledged
    ) { loaded, acknowledged -> loaded && !acknowledged }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _locationMessage = MutableStateFlow<String?>(null)
    val locationMessage: StateFlow<String?> = _locationMessage

    /** True while the "location permission denied" dialog should be shown in Settings. */
    private val _showLocationPermissionDialog = MutableStateFlow(false)
    val showLocationPermissionDialog: StateFlow<Boolean> = _showLocationPermissionDialog

    fun dismissLocationPermissionDialog() { _showLocationPermissionDialog.value = false }
    fun openAppSettings() { deps.location.openAppSettings() }

    fun hasLocationPermission(): Boolean = deps.location.hasLocationPermission()

    /**
     * Onboarding / settings GPS toggle. If enabling without permission, shows the system dialog first.
     * [onUiState] receives the toggle state to show (false if permission denied).
     */
    fun setGpsForZmanim(enabled: Boolean, onUiState: (Boolean) -> Unit) {
        if (!enabled) {
            onUiState(false)
            viewModelScope.launch {
                deps.repository.saveProfile(profile.value.copy(useGps = false))
            }
            return
        }
        if (deps.location.hasLocationPermission()) {
            onUiState(true)
            refreshGps()
            return
        }
        deps.location.onPermissionResult = { granted ->
            if (granted) {
                onUiState(true)
                refreshGps()
            } else {
                onUiState(false)
                _showLocationPermissionDialog.value = true
            }
        }
        deps.location.requestLocationPermission()
    }

    /** Fired once per day when all required mitzvot are complete. */
    private val _candlelightReward = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val candlelightReward = _candlelightReward.asSharedFlow()

    private val clockTick = flow {
        emit(Clock.System.now().toEpochMilliseconds())
        while (true) {
            delay(60_000)
            emit(Clock.System.now().toEpochMilliseconds())
        }
    }

    // Nested combine: first pack the 5 checklist data sources together, then tick with the clock.
    private data class ChecklistInput(
        val profile: UserProfile,
        val checked: Map<String, Boolean>,
        val custom: List<CustomChecklistItem>,
        val customChecked: Map<String, Boolean>,
        val monthlyMonths: Map<String, String>,
        val weeklyWeeks: Map<String, String>
    )

    private val checklistInput = combine(
        combine(
            deps.repository.profile,
            deps.repository.checklistChecked,
            deps.repository.customItems
        ) { prof, checked, custom -> Triple(prof, checked, custom) },
        combine(
            deps.repository.customChecked,
            deps.repository.monthlyCheckedMonths,
            deps.repository.weeklyCheckedWeeks
        ) { customChecked, monthly, weekly -> Triple(customChecked, monthly, weekly) }
    ) { (prof, checked, custom), (customChecked, monthly, weekly) ->
        ChecklistInput(prof, checked, custom, customChecked, monthly, weekly)
    }

    val dayChecklists: StateFlow<DayChecklists?> = combine(
        checklistInput,
        clockTick
    ) { input, _ ->
        deps.checklistEngine.resolve(
            input.profile, input.checked, input.custom, input.customChecked,
            input.monthlyMonths, input.weeklyWeeks
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val kashrutWait: StateFlow<KashrutWait?> = deps.repository.kashrutWait.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val requiredProgress: StateFlow<Pair<Int, Int>> = dayChecklists.map { d ->
        if (d == null) 0 to 0
        else deps.checklistEngine.requiredProgress(d)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0 to 0)

    val upcomingHolidays: StateFlow<List<UpcomingHoliday>> = combine(profile, clockTick) { prof, now ->
        deps.calendar.upcomingHolidays(nowEpochMillis = now, profile = prof)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val electronicsRest: StateFlow<ElectronicsRestPeriod?> = combine(profile, clockTick) { prof, now ->
        deps.calendar.electronicsRestPeriod(nowEpochMillis = now, profile = prof)
            ?: shabbatRestFallback(now, prof)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            deps.repository.profile.first()
            deps.repository.disclaimerAcknowledged.first()
            _prefsLoaded.value = true
        }
        viewModelScope.launch { runStartupMaintenance() }
        viewModelScope.launch {
            combine(dayChecklists, profile) { day, prof -> day to prof }
                .collect { (day, prof) ->
                    if (day == null) return@collect
                    if (!deps.checklistEngine.allRequiredComplete(day)) return@collect
                    val today = day.date.toString()
                    if (prof.tzaddikShownDate == today) return@collect
                    viewModelScope.launch {
                        deps.repository.saveProfile(prof.copy(tzaddikShownDate = today))
                        _candlelightReward.emit(Unit)
                    }
                }
        }
    }

    private suspend fun runStartupMaintenance() {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        deps.repository.clearDayIfNewDate(today)
        val now = Clock.System.now().toEpochMilliseconds()
        val wait = deps.repository.kashrutWait.first()
        val prof = deps.repository.profile.first()
        when {
            wait != null && wait.endsAtEpochMillis <= now -> {
                deps.repository.setKashrutWait(null)
                deps.kashrut.cancelNotification()
            }
            wait != null -> deps.kashrut.scheduleEndNotification(wait, prof)
            else -> deps.kashrut.cancelNotification()
        }
    }

    fun acknowledgeDisclaimer() {
        viewModelScope.launch {
            if (deps.repository.disclaimerAcknowledged.first()) return@launch
            deps.repository.acknowledgeDisclaimer()
        }
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            deps.repository.saveProfile(profile)
            applyLauncherIcon(profile.gender)
        }
    }

    fun completeOnboarding(
        gender: Gender,
        married: Boolean,
        hasChildren: Boolean,
        nusach: NusachSelection,
        useGps: Boolean
    ) {
        viewModelScope.launch {
            val current = deps.repository.profile.first()
            val updated = current.copy(
                onboardingComplete = true,
                gender = gender,
                married = married,
                hasChildren = hasChildren,
                nusachSelection = nusach,
                useGps = useGps
            )
            deps.repository.saveProfile(updated)
            applyLauncherIcon(updated.gender)
            if (useGps) refreshGps()
        }
    }

    /** Saves profile fields from onboarding step 1 so married/gender are not lost before finish. */
    fun saveOnboardingAboutYou(gender: Gender, married: Boolean, hasChildren: Boolean) {
        viewModelScope.launch {
            val current = deps.repository.profile.first()
            val updated = current.copy(
                gender = gender,
                married = married,
                hasChildren = hasChildren
            )
            deps.repository.saveProfile(updated)
            applyLauncherIcon(updated.gender)
        }
    }

    fun updateProfile(
        gender: Gender? = null,
        married: Boolean? = null,
        hasChildren: Boolean? = null
    ) {
        viewModelScope.launch {
            val p = deps.repository.profile.first()
            val updated = p.copy(
                gender = gender ?: p.gender,
                married = married ?: p.married,
                hasChildren = hasChildren ?: p.hasChildren
            )
            deps.repository.saveProfile(updated)
            applyLauncherIcon(updated.gender)
        }
    }

    fun setLiveInIsrael(value: Boolean) {
        viewModelScope.launch {
            deps.repository.saveProfile(profile.value.copy(liveInIsrael = value))
        }
    }

    fun setKashrutHours(meatToDairyHours: Int?, dairyToMeatMinutes: Int?) {
        viewModelScope.launch {
            val p = profile.value
            deps.repository.saveProfile(
                p.copy(
                    dairyAfterMeatHours = meatToDairyHours?.coerceIn(1, 12) ?: p.dairyAfterMeatHours,
                    meatAfterDairyHours = dairyToMeatMinutes?.let { KashrutWaitTimes.coerceDairyToMeatMinutes(it) }
                        ?: p.meatAfterDairyHours
                )
            )
        }
    }

    fun removeCustomItem(id: String) {
        viewModelScope.launch { deps.repository.removeCustomItem(id) }
    }

    fun setChecked(id: String, checked: Boolean, persist: Boolean = false) {
        viewModelScope.launch {
            if (id.startsWith("custom_")) {
                deps.repository.setCustomChecked(id, checked)
            } else {
                deps.repository.setChecked(id, checked, persist)
            }
        }
    }

    /**
     * Called for monthly mitzvot (e.g. Kiddush Levana). Stores the check alongside the current
     * Hebrew month key so the engine can determine if the item is current or from a previous month.
     */
    fun setWeeklyChecked(id: String, checked: Boolean) {
        viewModelScope.launch {
            val nowMillis = Clock.System.now().toEpochMilliseconds()
            val prof = deps.repository.profile.first()
            val cal = deps.calendar.dayInfoAt(nowMillis, prof)
            val daysUntilSaturday = (6 - cal.date.dayOfWeek.ordinal + 7) % 7
            val saturdayKey = cal.date.plus(daysUntilSaturday, DateTimeUnit.DAY).toString()
            deps.repository.setWeeklyChecked(id, checked, saturdayKey)
        }
    }

    fun setMonthlyChecked(id: String, checked: Boolean) {
        viewModelScope.launch {
            val nowMillis = Clock.System.now().toEpochMilliseconds()
            val prof = deps.repository.profile.first()
            val cal = deps.calendar.dayInfoAt(nowMillis, prof)
            val monthKey = "${cal.hebrewYear}-${cal.hebrewMonth}"
            deps.repository.setMonthlyChecked(id, checked, monthKey)
        }
    }

    fun addCustomItem(title: String) {
        viewModelScope.launch { deps.repository.addCustomItem(title.trim()) }
    }

    fun logMeal(category: MealCategory, scheduleNotification: Boolean = true) {
        viewModelScope.launch {
            val prof = profile.value
            val wait = deps.kashrut.startMeal(prof, category, Clock.System.now().toEpochMilliseconds())
            deps.repository.setKashrutWait(wait)
            if (scheduleNotification) {
                deps.kashrut.scheduleEndNotification(wait, prof)
            }
        }
    }

    fun clearKashrut() {
        viewModelScope.launch {
            deps.repository.setKashrutWait(null)
            deps.kashrut.cancelNotification()
        }
    }

    fun refreshGps() {
        if (!deps.location.hasLocationPermission()) {
            deps.location.onPermissionResult = { granted ->
                if (granted) refreshGps()
                else _showLocationPermissionDialog.value = true
            }
            deps.location.requestLocationPermission()
            return
        }
        viewModelScope.launch {
            when (val result = deps.location.getCurrentLocation()) {
                is LocationResult.Success -> {
                    val d = result.data
                    val current = profile.value
                    deps.repository.saveProfile(
                        current.copy(
                            latitude = d.latitude,
                            longitude = d.longitude,
                            timezoneId = d.timezoneId,
                            locationLabel = d.label,
                            locationSource = LocationSource.GPS,
                            useGps = true,
                            manualCityId = null
                        )
                    )
                    _locationMessage.value = buildString {
                        d.label?.let { append("Location: $it. ") }
                        append("Zmanim use device timezone (${d.timezoneId}) — pick a manual city when traveling.")
                    }
                }
                LocationResult.PermissionDenied -> {
                    _locationMessage.value = null
                    _showLocationPermissionDialog.value = true
                }
                LocationResult.Unavailable ->
                    _locationMessage.value = "Could not get GPS fix — pick a manual city"
            }
        }
    }

    fun setManualCity(cityId: String) {
        viewModelScope.launch {
            val city = ManualCities.byId(cityId) ?: return@launch
            val current = profile.value
            deps.repository.saveProfile(
                current.copy(
                    manualCityId = city.id,
                    latitude = city.latitude,
                    longitude = city.longitude,
                    timezoneId = city.timezoneId,
                    locationLabel = city.label,
                    locationSource = LocationSource.MANUAL,
                    useGps = false
                )
            )
            _locationMessage.value = "Using ${city.label} for zmanim"
        }
    }

    fun setTextScale(scale: Float) {
        viewModelScope.launch {
            deps.repository.saveProfile(
                profile.value.copy(textScale = TextScaleDefaults.coerce(scale))
            )
        }
    }

    fun adjustTextScale(delta: Float) {
        setTextScale(profile.value.textScale + delta)
    }

    fun updateOnboardingDraft(
        gender: Gender,
        married: Boolean,
        hasChildren: Boolean,
        nusach: NusachSelection,
        useGps: Boolean
    ) {
        viewModelScope.launch {
            val current = profile.value
            deps.repository.saveProfile(
                current.copy(
                    gender = gender,
                    married = married,
                    hasChildren = hasChildren,
                    nusachSelection = nusach,
                    useGps = useGps
                )
            )
            if (useGps) refreshGps()
        }
    }

    /** Fallback pause when full zmanim calendar fails — still ends at tzeit when zmanim are available. */
    private fun shabbatRestFallback(nowEpochMillis: Long, profile: UserProfile): ElectronicsRestPeriod? {
        val cal = deps.calendar.dayInfoAt(nowEpochMillis, profile)
        if (!HolyDayPhoneRules.isShabbatMelachaWindow(cal, nowEpochMillis)) return null
        return ElectronicsRestPeriod(
            kind = RestKind.SHABBAT,
            title = SHABBAT_REST_TITLE,
            message = shabbatMessage(),
            hebrewDateLabel = cal.hebrewLabel,
            locationLabel = profile.locationLabel,
            endsAtEpochMillis = cal.zmanim?.tzeitMillis,
        )
    }
}
