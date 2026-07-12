package com.beardytop.beatzaddik

import com.beardytop.beatzaddik.data.AppRepository
import com.beardytop.beatzaddik.data.BundledTranslationsCatalog
import com.beardytop.beatzaddik.data.ChecklistCatalog
import com.beardytop.beatzaddik.data.CityGeographyCatalog
import com.beardytop.beatzaddik.data.ManualCitiesCatalog
import com.beardytop.beatzaddik.data.HolidayOverlayEntry
import com.beardytop.beatzaddik.data.HolidaysLoader
import com.beardytop.beatzaddik.data.createAppRepository
import com.beardytop.beatzaddik.domain.ChecklistEngine
import com.beardytop.beatzaddik.domain.JewishCalendarService
import com.beardytop.beatzaddik.domain.KashrutTimerService
import com.beardytop.beatzaddik.domain.ManualCities
import com.beardytop.beatzaddik.domain.createJewishCalendarBackend
import com.beardytop.beatzaddik.platform.PlatformLocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppDependencies(
    val platformContext: Any?,
    locationService: PlatformLocationService,
    holidayOverlay: List<HolidayOverlayEntry> = emptyList()
) {
    val repository: AppRepository = createAppRepository(platformContext)
    val calendar = JewishCalendarService(createJewishCalendarBackend(), holidayOverlay)
    val checklistEngine = ChecklistEngine(calendar, ChecklistCatalog.all)
    val kashrut = KashrutTimerService()
    val location = locationService

    companion object {
        suspend fun create(
            platformContext: Any?,
            locationService: PlatformLocationService,
            embeddedMode: Boolean = false,
        ): AppDependencies = withContext(Dispatchers.Default) {
            ChecklistCatalog.loadFromAssets()
            ManualCitiesCatalog.loadFromAssets()
            ManualCities.onCatalogLoaded(ManualCitiesCatalog.all)
            if (!embeddedMode) {
                CityGeographyCatalog.loadFromAssets()
                BundledTranslationsCatalog.loadFromAssets()
            }
            val holidays = HolidaysLoader.load()
            val deps = AppDependencies(platformContext, locationService, holidays)
            if (embeddedMode) {
                deps.repository.warmStartupReads()
            }
            deps
        }
    }
}
