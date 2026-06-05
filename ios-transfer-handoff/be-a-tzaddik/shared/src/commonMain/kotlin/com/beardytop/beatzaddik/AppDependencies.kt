package com.beardytop.beatzaddik

import com.beardytop.beatzaddik.data.AppRepository
import com.beardytop.beatzaddik.data.ChecklistCatalog
import com.beardytop.beatzaddik.data.ManualCitiesCatalog
import com.beardytop.beatzaddik.data.HolidayOverlayEntry
import com.beardytop.beatzaddik.data.HolidaysLoader
import com.beardytop.beatzaddik.data.createAppRepository
import com.beardytop.beatzaddik.domain.ChecklistEngine
import com.beardytop.beatzaddik.domain.JewishCalendarService
import com.beardytop.beatzaddik.domain.KashrutTimerService
import com.beardytop.beatzaddik.domain.createJewishCalendarBackend
import com.beardytop.beatzaddik.platform.PlatformLocationService

class AppDependencies(
    platformContext: Any?,
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
            locationService: PlatformLocationService
        ): AppDependencies {
            ChecklistCatalog.loadFromAssets()
            ManualCitiesCatalog.loadFromAssets()
            val holidays = HolidaysLoader.load()
            return AppDependencies(platformContext, locationService, holidays)
        }
    }
}
