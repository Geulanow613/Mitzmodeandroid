package com.beardytop.beatzaddik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beardytop.beatzaddik.AppDependencies
import com.beardytop.beatzaddik.domain.AppRatingPromptPolicy
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
import com.beardytop.beatzaddik.domain.LocationElevation
import com.beardytop.beatzaddik.domain.LocationTimezone
import com.beardytop.beatzaddik.platform.LocationResult
import com.beardytop.beatzaddik.platform.KashrutNotifications
import com.beardytop.beatzaddik.platform.applyLauncherIcon
import com.beardytop.beatzaddik.domain.ChecklistDebugDateFinder
import com.beardytop.beatzaddik.domain.ChecklistDebugScenarios
import com.beardytop.beatzaddik.domain.ChecklistDebugOverride
import com.beardytop.beatzaddik.domain.ChecklistDebugPhase
import com.beardytop.beatzaddik.domain.ChecklistDebugScenario
import com.beardytop.beatzaddik.domain.ChecklistDebugTimeSlot
import com.beardytop.beatzaddik.domain.forDebugCalendar
import com.beardytop.beatzaddik.domain.ElectronicsRestPeriod
import com.beardytop.beatzaddik.domain.HolyDayPhoneRules
import com.beardytop.beatzaddik.domain.RestKind
import com.beardytop.beatzaddik.domain.SHABBAT_REST_TITLE
import com.beardytop.beatzaddik.domain.TzeitDay
import com.beardytop.beatzaddik.domain.shabbatMessage
import com.beardytop.beatzaddik.ui.theme.TextScaleDefaults
import kotlinx.coroutines.Job
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class AppViewModel(private val deps: AppDependencies) : ViewModel() {

    val profile: StateFlow<UserProfile> = deps.repository.profile.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile()
    )

    private val _prefsLoaded = MutableStateFlow(false)
    val prefsLoaded: StateFlow<Boolean> = _prefsLoaded

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

    private val _showNotificationPermissionDialog = MutableStateFlow(false)
    val showNotificationPermissionDialog: StateFlow<Boolean> = _showNotificationPermissionDialog

    fun dismissNotificationPermissionDialog() { _showNotificationPermissionDialog.value = false }
    fun openNotificationSettings() {
        KashrutNotifications.openNotificationSettings()
    }

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
            persistGpsModeEnabled()
            refreshGps()
            return
        }
        deps.location.onPermissionResult = { granted ->
            if (granted) {
                onUiState(true)
                persistGpsModeEnabled()
                refreshGps()
            } else {
                onUiState(false)
                _showLocationPermissionDialog.value = true
            }
        }
        deps.location.requestLocationPermission()
    }

    /** Keep GPS mode on even when a fix is still pending (emulator / weak signal). */
    private fun persistGpsModeEnabled() {
        viewModelScope.launch {
            val reconciled = LocationTimezone.resolveForProfile(
                profile.value.copy(
                    useGps = true,
                    manualCityId = null,
                    locationSource = LocationSource.GPS,
                )
            )
            deps.repository.saveProfile(reconciled)
        }
    }

    /** Fired once per day when all required mitzvot are complete. */
    private val _candlelightReward = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val candlelightReward = _candlelightReward.asSharedFlow()

    private val _showRatingPrompt = MutableStateFlow(false)
    val showRatingPrompt: StateFlow<Boolean> = _showRatingPrompt

    /** Force the soft rating dialog (debug menu). Does not skip "completed" permanently until they finish. */
    fun debugShowRatingPrompt() {
        _showRatingPrompt.value = true
    }

    fun dismissRatingPromptForLater() {
        _showRatingPrompt.value = false
        viewModelScope.launch {
            val now = Clock.System.now().toEpochMilliseconds()
            val current = deps.repository.profile.first()
            deps.repository.saveProfile(
                current.copy(ratingPromptSnoozeUntilEpochMillis = AppRatingPromptPolicy.snoozeUntil(now))
            )
        }
    }

    fun markRatingPromptCompleted() {
        _showRatingPrompt.value = false
        viewModelScope.launch {
            val current = deps.repository.profile.first()
            deps.repository.saveProfile(
                current.copy(
                    ratingPromptCompleted = true,
                    ratingPromptSnoozeUntilEpochMillis = null,
                )
            )
        }
    }

    private fun maybeShowRatingPrompt(profile: UserProfile, nowEpochMillis: Long) {
        if (_showRatingPrompt.value) return
        if (!AppRatingPromptPolicy.isEligible(profile, nowEpochMillis)) return
        _showRatingPrompt.value = true
    }

    // Nested combine: first pack the 5 checklist data sources together, then tick with the clock.
    private data class ChecklistInput(
        val profile: UserProfile,
        val checked: Map<String, Boolean>,
        val custom: List<CustomChecklistItem>,
        val customChecked: Map<String, Boolean>,
        val monthlyMonths: Map<String, String>,
        val weeklyWeeks: Map<String, String>,
        val tzeitDays: Map<String, String>,
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
            deps.repository.weeklyCheckedWeeks,
            deps.repository.tzeitCheckedDays,
        ) { customChecked, monthly, weekly, tzeit -> ChecklistInputPartial(customChecked, monthly, weekly, tzeit) }
    ) { (prof, checked, custom), partial ->
        ChecklistInput(prof, checked, custom, partial.customChecked, partial.monthly, partial.weekly, partial.tzeit)
    }

    private data class ChecklistInputPartial(
        val customChecked: Map<String, Boolean>,
        val monthly: Map<String, String>,
        val weekly: Map<String, String>,
        val tzeit: Map<String, String>,
    )

    private val _checklistDebugOverride = MutableStateFlow<ChecklistDebugOverride?>(null)
    val checklistDebugOverride: StateFlow<ChecklistDebugOverride?> = _checklistDebugOverride

    private val _pendingDebugTimeSlot = MutableStateFlow(ChecklistDebugTimeSlot.MORNING)
    val pendingDebugTimeSlot: StateFlow<ChecklistDebugTimeSlot> = _pendingDebugTimeSlot

    private val _checklistDebugResolving = MutableStateFlow(false)
    val checklistDebugResolving: StateFlow<Boolean> = _checklistDebugResolving

    private val _checklistDebugError = MutableStateFlow<String?>(null)
    val checklistDebugError: StateFlow<String?> = _checklistDebugError

    private var checklistDebugResolveJob: Job? = null

    /** Wall-clock anchor so sunset-offset warn sims advance and the countdown ticks. */
    private val _debugSimWallAnchor = MutableStateFlow<Long?>(null)

    /** 60s normally; 1s while a phone-warn sunset-offset sim is running. */
    private val clockTick = _checklistDebugOverride.flatMapLatest { debug ->
        val scenario = debug?.scenarioId?.let { ChecklistDebugScenarios.byId(it) }
        val intervalMs = if (scenario?.sunsetOffsetMinutes != null) 1_000L else 60_000L
        flow {
            emit(Clock.System.now().toEpochMilliseconds())
            while (true) {
                delay(intervalMs)
                emit(Clock.System.now().toEpochMilliseconds())
            }
        }
    }

    private val effectiveNowMillis = combine(
        _checklistDebugOverride,
        _debugSimWallAnchor,
        clockTick,
    ) { debug, anchor, tick ->
        if (debug == null) {
            tick
        } else {
            val scenario = ChecklistDebugScenarios.byId(debug.scenarioId)
            if (scenario?.sunsetOffsetMinutes != null && anchor != null) {
                val wallElapsed = (tick - anchor).coerceAtLeast(0L)
                val simElapsed = wallElapsed * HolyDayPhoneRules.DEBUG_WARN_SIM_SPEED
                debug.epochMillis + simElapsed
            } else {
                debug.epochMillis
            }
        }
    }

    /** Wall / simulated "now" for clocks and debug labels (advances during phone-warn sims). */
    val displayNowMillis: StateFlow<Long> = effectiveNowMillis.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Clock.System.now().toEpochMilliseconds(),
    )

    fun applyChecklistDebugScenario(scenario: ChecklistDebugScenario, timeSlot: ChecklistDebugTimeSlot) {
        _pendingDebugTimeSlot.value = timeSlot
        checklistDebugResolveJob?.cancel()
        checklistDebugResolveJob = viewModelScope.launch {
            _checklistDebugResolving.value = true
            _checklistDebugError.value = null
            val prof = profile.value.forDebugCalendar(scenario)
            ChecklistDebugDateFinder.clearCache()
            val override = withContext(Dispatchers.Default) {
                ChecklistDebugDateFinder.resolve(
                    calendar = deps.calendar,
                    profile = prof,
                    scenario = scenario,
                    timeSlot = timeSlot,
                )
            }
            _checklistDebugResolving.value = false
            if (override == null) {
                _checklistDebugError.value =
                    "No calendar date found for ${ChecklistDebugScenarios.displayLabel(scenario)}"
                return@launch
            }
            _debugSimWallAnchor.value = Clock.System.now().toEpochMilliseconds()
            _checklistDebugOverride.value = override
        }
    }

    fun setChecklistDebugTimeSlot(timeSlot: ChecklistDebugTimeSlot) {
        _pendingDebugTimeSlot.value = timeSlot
        val current = _checklistDebugOverride.value ?: return
        val scenario = ChecklistDebugScenarios.byId(current.scenarioId) ?: return
        val prof = profile.value.forDebugCalendar(scenario)
        val millis = when {
            scenario.sunsetOffsetMinutes != null ||
                scenario.phase == ChecklistDebugPhase.MOTZEI ->
                ChecklistDebugDateFinder.overrideEpochMillis(
                    calendar = deps.calendar,
                    profile = prof,
                    scenario = scenario,
                    simulatedDate = current.simulatedDate,
                    timeSlot = timeSlot,
                )
            else -> ChecklistDebugDateFinder.epochMillisAt(
                current.simulatedDate,
                timeSlot,
                prof.timezoneId,
            )
        }
        _debugSimWallAnchor.value = Clock.System.now().toEpochMilliseconds()
        _checklistDebugOverride.value = current.copy(
            timeSlot = timeSlot,
            epochMillis = millis,
        )
    }

    fun clearChecklistDebug() {
        _checklistDebugOverride.value = null
        _checklistDebugError.value = null
        _debugSimWallAnchor.value = null
    }

    val checklistDebugMenuVisible: StateFlow<Boolean> = profile
        .map { it.checklistDebugMenuVisible }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleChecklistDebugMenu() {
        viewModelScope.launch {
            val current = profile.value
            deps.repository.saveProfile(
                current.copy(checklistDebugMenuVisible = !current.checklistDebugMenuVisible)
            )
        }
    }

    val dayChecklists: StateFlow<DayChecklists?> = combine(
        checklistInput,
        effectiveNowMillis,
        checklistDebugOverride,
    ) { input, nowMillis, debug ->
        val scenario = debug?.scenarioId?.let { ChecklistDebugScenarios.byId(it) }
        val calendarProfile = input.profile.forDebugCalendar(scenario)
        deps.checklistEngine.resolve(
            calendarProfile, input.checked, input.custom, input.customChecked,
            input.monthlyMonths, input.weeklyWeeks, input.tzeitDays,
            nowMillis = nowMillis,
        )
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Halachic day key (tzeit→tzeit) used so checklist mitzvah counts increment once per day. */
    val halachicDayKey: StateFlow<String> = combine(profile, effectiveNowMillis) { prof, nowMillis ->
        val today = deps.calendar.dayInfoAt(nowMillis, prof)
        val yesterday = deps.calendar.dayInfoAt(nowMillis - 86_400_000L, prof)
        TzeitDay.currentKeyOrCivilDate(nowMillis, today, yesterday, prof.timezoneId)
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val kashrutWait: StateFlow<KashrutWait?> = deps.repository.kashrutWait.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val requiredProgress: StateFlow<Pair<Int, Int>> = combine(dayChecklists, profile) { d, prof ->
        if (d == null) 0 to 0
        else deps.checklistEngine.requiredProgress(d, prof)
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0 to 0)

    val upcomingHolidays: StateFlow<List<UpcomingHoliday>> = combine(
        profile,
        effectiveNowMillis,
        checklistDebugOverride,
    ) { prof, now, debug ->
        val scenario = debug?.scenarioId?.let { ChecklistDebugScenarios.byId(it) }
        deps.calendar.upcomingHolidays(
            nowEpochMillis = now,
            profile = prof.forDebugCalendar(scenario),
        )
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val electronicsRest: StateFlow<ElectronicsRestPeriod?> = combine(
        profile,
        effectiveNowMillis,
        checklistDebugOverride,
    ) { prof, now, debug ->
        if (debug != null) return@combine null
        deps.calendar.electronicsRestPeriod(nowEpochMillis = now, profile = prof)
            ?: shabbatRestFallback(now, prof)
    }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            deps.repository.profile.first()
            deps.repository.disclaimerAcknowledged.first()
            _prefsLoaded.value = true
            // Count this cold start toward soft-rating eligibility (once prefs are readable).
            val now = Clock.System.now().toEpochMilliseconds()
            val current = deps.repository.profile.first()
            deps.repository.saveProfile(
                current.copy(
                    ratingPromptFirstLaunchEpochMillis =
                        current.ratingPromptFirstLaunchEpochMillis ?: now,
                    ratingPromptLaunchCount = current.ratingPromptLaunchCount + 1,
                )
            )
            delay(2_500)
            val prof = deps.repository.profile.first()
            if (prof.onboardingComplete) {
                maybeShowRatingPrompt(prof, Clock.System.now().toEpochMilliseconds())
            }
        }
        // Ensure daily checklist items reset at tzeit (halachic day boundary), not midnight.
        viewModelScope.launch(Dispatchers.Default) {
            combine(profile, effectiveNowMillis) { prof, now -> prof to now }
                .collect { (prof, nowMillis) ->
                    val today = deps.calendar.dayInfoAt(nowMillis, prof)
                    val yesterday = deps.calendar.dayInfoAt(nowMillis - 86_400_000L, prof)
                    val key = TzeitDay.currentKeyOrCivilDate(nowMillis, today, yesterday, prof.timezoneId)
                    deps.repository.clearDayIfNewDate(key)

                    // Daily ongoing section: if user collapsed it on a previous halachic day, re-expand now.
                    if (prof.dailyOngoingCollapsed && prof.dailyOngoingCollapsedDate != key) {
                        deps.repository.saveProfile(
                            prof.copy(dailyOngoingCollapsed = false, dailyOngoingCollapsedDate = null)
                        )
                    }
                }
        }
        viewModelScope.launch { runStartupMaintenance() }
        viewModelScope.launch {
            combine(dayChecklists, profile, checklistDebugOverride) { day, prof, debug -> Triple(day, prof, debug) }
                .collect { (day, prof, debug) ->
                    if (day == null || debug != null) return@collect
                    if (!deps.checklistEngine.allRequiredComplete(day, prof)) return@collect
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
        // Note: daily rollover is handled by the collector in init() using a tzeit-based key.
        val now = Clock.System.now().toEpochMilliseconds()
        val wait = deps.repository.kashrutWait.first()
        var prof = deps.repository.profile.first()
        when {
            wait != null && wait.endsAtEpochMillis <= now -> {
                // Keep finished wait until the user clears it; refresh the “you can eat” notification.
                deps.kashrut.showFinishedNotification(wait, prof)
            }
            wait != null -> deps.kashrut.scheduleEndNotification(wait, prof)
            else -> deps.kashrut.cancelNotification()
        }
        if (prof.useGps && prof.elevationMeters == null) {
            LocationElevation.backfillForProfile(prof)?.let { elevation ->
                prof = prof.copy(elevationMeters = elevation)
                deps.repository.saveProfile(prof)
            }
        }
        prof = deps.repository.profile.first()
        val reconciled = withContext(Dispatchers.Default) {
            LocationTimezone.resolveForProfile(prof)
        }
        if (reconciled != prof) {
            deps.repository.saveProfile(reconciled)
            prof = reconciled
        }
        if (prof.useGps) {
            refreshGps()
        }
    }

    fun setOngoingSectionCollapsed(section: String, collapsed: Boolean) {
        viewModelScope.launch {
            val current = deps.repository.profile.first()
            val nowMillis = effectiveNowMillis.first()
            val todayInfo = deps.calendar.dayInfoAt(nowMillis, current)
            val yesterdayInfo = deps.calendar.dayInfoAt(nowMillis - 86_400_000L, current)
            val halachicKey = TzeitDay.currentKeyOrCivilDate(
                nowMillis,
                todayInfo,
                yesterdayInfo,
                current.timezoneId,
            )
            when (section) {
                "Permanent ongoing mitzvot" -> {
                    deps.repository.saveProfile(current.copy(permanentOngoingCollapsed = collapsed))
                }
                "Daily ongoing mitzvot" -> {
                    deps.repository.saveProfile(
                        current.copy(
                            dailyOngoingCollapsed = collapsed,
                            dailyOngoingCollapsedDate = if (collapsed) halachicKey else null,
                        )
                    )
                }
            }
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

    fun skipOnboardingWithDefaults() {
        completeOnboarding(
            gender = Gender.MALE,
            married = false,
            hasChildren = false,
            nusach = NusachSelection.ASHKENAZ,
            useGps = hasLocationPermission(),
        )
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
                appTourCompleted = false,
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

    fun completeAppTour() {
        viewModelScope.launch {
            val current = deps.repository.profile.first()
            if (current.appTourCompleted) return@launch
            deps.repository.saveProfile(current.copy(appTourCompleted = true))
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

    /**
     * Unified Settings toggle for Israel vs chutz la'aretz customs.
     * [locationInIsrael] chooses which stored flag to update and clears the other override
     * so switching cities (Eilat ↔ NYC) doesn't leave a stale force flag stuck on.
     */
    fun setUseIsraelCustoms(enabled: Boolean, locationInIsrael: Boolean) {
        viewModelScope.launch {
            val p = profile.value
            deps.repository.saveProfile(
                if (locationInIsrael) {
                    p.copy(
                        forceChutzLaAretzCustoms = !enabled,
                    )
                } else {
                    p.copy(
                        liveInIsrael = enabled,
                        forceChutzLaAretzCustoms = false,
                    )
                }
            )
        }
    }

    /** When [force] is true, use chutz la'aretz customs even if location is in Israel. */
    fun setForceChutzLaAretzCustoms(force: Boolean) {
        viewModelScope.launch {
            deps.repository.saveProfile(profile.value.copy(forceChutzLaAretzCustoms = force))
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
            val daysUntilSaturday = (DayOfWeek.SATURDAY.ordinal - cal.date.dayOfWeek.ordinal + 7) % 7
            val saturdayKey = cal.date.plus(daysUntilSaturday, DateTimeUnit.DAY).toString()
            deps.repository.setWeeklyChecked(id, checked, saturdayKey)
        }
    }

    /** Women's daily prayer — checked state resets each halachic day at tzeit. */
    fun setTzeitDayChecked(id: String, checked: Boolean) {
        viewModelScope.launch {
            val nowMillis = Clock.System.now().toEpochMilliseconds()
            val prof = deps.repository.profile.first()
            val cal = deps.calendar.dayInfoAt(nowMillis, prof)
            val yesterday = deps.calendar.dayInfoForDate(
                cal.date.plus(-1, DateTimeUnit.DAY),
                prof,
            )
            val key = TzeitDay.currentKey(nowMillis, cal, yesterday) ?: return@launch
            deps.repository.setTzeitDayChecked(id, checked, key)
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

    /**
     * Clears a finished wait (banner / notification). Ignored while the countdown is still running —
     * use [cancelKashrut] for that.
     */
    fun clearKashrut() {
        viewModelScope.launch {
            val wait = deps.repository.kashrutWait.first() ?: return@launch
            val now = Clock.System.now().toEpochMilliseconds()
            if (wait.endsAtEpochMillis > now) return@launch
            deps.repository.setKashrutWait(null)
            deps.kashrut.cancelNotification()
        }
    }

    /** Cancels a running (or finished) wait and dismisses notifications. */
    fun cancelKashrut() {
        viewModelScope.launch {
            deps.repository.setKashrutWait(null)
            deps.kashrut.cancelNotification()
        }
    }

    /** Cancels the current wait and starts a fresh countdown for the same meal category. */
    fun restartKashrut() {
        viewModelScope.launch {
            val current = deps.repository.kashrutWait.first() ?: return@launch
            val prof = profile.value
            val wait = deps.kashrut.startMeal(
                prof,
                current.category,
                Clock.System.now().toEpochMilliseconds(),
            )
            deps.repository.setKashrutWait(wait)
            deps.kashrut.scheduleEndNotification(wait, prof)
        }
    }

    fun setShowKashrutTimerNotification(enabled: Boolean) {
        viewModelScope.launch {
            if (!enabled) {
                val p = profile.value.copy(showKashrutTimerNotification = false)
                deps.repository.saveProfile(p)
                refreshKashrutNotifications(p)
                return@launch
            }
            if (KashrutNotifications.areNotificationsAllowed()) {
                val p = profile.value.copy(showKashrutTimerNotification = true)
                deps.repository.saveProfile(p)
                refreshKashrutNotifications(p)
                return@launch
            }
            KashrutNotifications.requestPermission { granted ->
                viewModelScope.launch {
                    if (granted) {
                        val p = profile.value.copy(showKashrutTimerNotification = true)
                        deps.repository.saveProfile(p)
                        refreshKashrutNotifications(p)
                    } else {
                        val p = profile.value.copy(showKashrutTimerNotification = false)
                        deps.repository.saveProfile(p)
                        refreshKashrutNotifications(p)
                        _showNotificationPermissionDialog.value = true
                    }
                }
            }
        }
    }

    fun setKashrutTimerAlertPrefs(sound: Boolean? = null, vibrate: Boolean? = null) {
        viewModelScope.launch {
            val cur = profile.value
            val p = cur.copy(
                kashrutTimerSound = sound ?: cur.kashrutTimerSound,
                kashrutTimerVibrate = vibrate ?: cur.kashrutTimerVibrate,
            )
            deps.repository.saveProfile(p)
            refreshKashrutNotifications(p)
        }
    }

    private suspend fun refreshKashrutNotifications(p: com.beardytop.beatzaddik.domain.UserProfile) {
        val wait = deps.repository.kashrutWait.first()
        val now = Clock.System.now().toEpochMilliseconds()
        when {
            wait == null -> deps.kashrut.cancelNotification()
            wait.endsAtEpochMillis <= now -> deps.kashrut.showFinishedNotification(wait, p)
            p.showKashrutTimerNotification -> deps.kashrut.scheduleEndNotification(wait, p)
            else -> {
                deps.kashrut.dismissStatusNotification()
                deps.kashrut.scheduleEndNotification(wait, p)
            }
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
                    val elevation = withContext(Dispatchers.Default) {
                        LocationElevation.resolveForGps(
                            latitude = d.latitude,
                            longitude = d.longitude,
                            gpsAltitudeMeters = d.elevationMeters,
                            hasAltitudeReading = d.hasAltitudeReading,
                        )
                    }
                    val resolvedTimezoneId = withContext(Dispatchers.Default) {
                        LocationTimezone.resolve(
                            latitude = d.latitude,
                            longitude = d.longitude,
                            deviceFallback = d.timezoneId,
                        )
                    }
                    deps.repository.saveProfile(
                        current.copy(
                            latitude = d.latitude,
                            longitude = d.longitude,
                            timezoneId = resolvedTimezoneId,
                            locationLabel = d.label,
                            elevationMeters = elevation,
                            locationSource = LocationSource.GPS,
                            useGps = true,
                            manualCityId = null
                        )
                    )
                    _locationMessage.value = buildString {
                        d.label?.let { append("Location: $it. ") }
                        if (resolvedTimezoneId == d.timezoneId) {
                            append("Using timezone $resolvedTimezoneId.")
                        } else {
                            append("Using timezone $resolvedTimezoneId (matched from nearby city).")
                        }
                    }
                }
                LocationResult.PermissionDenied -> {
                    _locationMessage.value = null
                    _showLocationPermissionDialog.value = true
                }
                LocationResult.Unavailable -> {
                    val current = profile.value
                    val reconciled = withContext(Dispatchers.Default) {
                        LocationTimezone.resolveForProfile(current)
                    }
                    if (reconciled != current) {
                        deps.repository.saveProfile(reconciled)
                    }
                    _locationMessage.value =
                        "Could not get GPS fix yet — still using GPS mode; set emulator location or pick a city below"
                }
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
                    elevationMeters = city.elevationMeters,
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
