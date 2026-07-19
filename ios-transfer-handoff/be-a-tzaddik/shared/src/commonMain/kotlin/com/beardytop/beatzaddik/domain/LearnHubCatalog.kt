package com.beardytop.beatzaddik.domain

/**
 * Curated outbound English learning destinations for the Learn tab.
 * Opens publishers' own pages in the system browser — no scraped / hosted content.
 */
object LearnHubCatalog {

    data class LinkItem(
        val title: String,
        val subtitle: String,
        val url: String,
        val sourceLabel: String,
    )

    data class Snapshot(
        val parshaName: String?,
        val thisWeek: List<LinkItem>,
        val watch: List<LinkItem>,
        val browse: List<LinkItem>,
    )

    fun snapshot(
        parshaLabel: String?,
        todayOccasionLabel: String?,
        todayOccasionGuideAnchor: String?,
        omerDay: Int?,
        upcomingHolidays: List<UpcomingHoliday>,
    ): Snapshot {
        val parsha = ParshaData.forDisplayName(parshaLabel)
        return Snapshot(
            parshaName = parsha?.displayName ?: parshaLabel,
            thisWeek = thisWeekLinks(
                parsha = parsha,
                todayOccasionLabel = todayOccasionLabel,
                todayOccasionGuideAnchor = todayOccasionGuideAnchor,
                omerDay = omerDay,
                upcomingHolidays = upcomingHolidays,
            ),
            watch = WATCH_LINKS,
            browse = BROWSE_LINKS,
        )
    }

    private fun thisWeekLinks(
        parsha: ParshaData.ParshaInfo?,
        todayOccasionLabel: String?,
        todayOccasionGuideAnchor: String?,
        omerDay: Int?,
        upcomingHolidays: List<UpcomingHoliday>,
    ): List<LinkItem> {
        val items = mutableListOf<LinkItem>()
        val seenUrls = mutableSetOf<String>()

        fun addUnique(item: LinkItem?) {
            if (item == null || item.url in seenUrls) return
            seenUrls += item.url
            items += item
        }

        val name = parsha?.displayName
        val chabad = parsha?.chabadUrl ?: CHABAD_PARSHA_HUB
        val titleSuffix = name?.let { " · $it" }.orEmpty()
        addUnique(
            LinkItem(
                title = "This week's Parsha$titleSuffix",
                subtitle = "Articles, videos, and Torah text",
                url = chabad,
                sourceLabel = "Chabad.org",
            ),
        )

        addUnique(
            holidayLink(
                label = todayOccasionLabel,
                anchor = todayOccasionGuideAnchor,
                whenLabel = "Today",
            ),
        )

        if (omerDay != null && omerDay in 1..49) {
            addUnique(
                LinkItem(
                    title = "Count the Omer — day $omerDay",
                    subtitle = "Meaning, laws, and inspiration for Sefirat HaOmer",
                    url = OMER_URL,
                    sourceLabel = "Chabad.org",
                ),
            )
        }

        upcomingHolidays
            .filter { it.daysAway in 0..7 }
            .forEach { holiday ->
                val whenLabel = when {
                    holiday.whenLabelOverride != null -> holiday.whenLabelOverride
                    holiday.daysAway == 0 && holiday.beginsTonightWhenImminent -> "Tonight"
                    holiday.daysAway == 0 -> "Today"
                    holiday.daysAway == 1 -> "Tomorrow"
                    else -> "In ${holiday.daysAway} days"
                }
                addUnique(
                    holidayLink(
                        label = holiday.name,
                        anchor = HolidayGuideAnchors.anchorForLabel(holiday.name),
                        whenLabel = whenLabel,
                    ),
                )
            }

        return items
    }

    private fun holidayLink(
        label: String?,
        anchor: String?,
        whenLabel: String,
    ): LinkItem? {
        if (label.isNullOrBlank() && anchor.isNullOrBlank()) return null
        val key = anchor?.takeIf { it.isNotBlank() }
            ?: label?.let { HolidayGuideAnchors.anchorForLabel(it) }
            ?: return null
        val url = HOLIDAY_URLS[key] ?: return null
        val titleName = label?.takeIf { it.isNotBlank() }
            ?: HOLIDAY_TITLES[key]
            ?: return null
        return LinkItem(
            title = "$whenLabel · $titleName",
            subtitle = "Background and how to observe this day",
            url = url,
            sourceLabel = "Chabad.org",
        )
    }

    /** Site-hosted video/audio only — no YouTube. */
    private val WATCH_LINKS = listOf(
        LinkItem(
            title = "TorahAnytime",
            subtitle = "Thousands of English shiurim",
            url = "https://www.torahanytime.com/",
            sourceLabel = "TorahAnytime",
        ),
        LinkItem(
            title = "Daily classes — Rabbi Gordon",
            subtitle = "Chumash, Tanya, and Rambam",
            url = "https://www.chabad.org/multimedia/video_cdo/aid/903523/jewish/Rabbi-Gordon.htm",
            sourceLabel = "Chabad.org",
        ),
        LinkItem(
            title = "Jewish.tv",
            subtitle = "Chabad's English video library",
            url = "https://www.jewish.tv/",
            sourceLabel = "Jewish.tv",
        ),
        LinkItem(
            title = "TheYeshiva.net — Rabbi YY Jacobson",
            subtitle = "In-depth English classes",
            url = "https://www.theyeshiva.net/",
            sourceLabel = "TheYeshiva.net",
        ),
    )

    private val BROWSE_LINKS = listOf(
        LinkItem(
            title = "OU Torah",
            subtitle = "Daily English learning",
            url = "https://outorah.org/",
            sourceLabel = "OU Torah",
        ),
        LinkItem(
            title = "YUTorah",
            subtitle = "Searchable English audio shiur archive",
            url = "https://www.yutorah.org/",
            sourceLabel = "YUTorah",
        ),
    )

    private const val CHABAD_PARSHA_HUB =
        "https://www.chabad.org/parshah/default_cdo/jewish/Torah-Portion.htm"
    private const val OMER_URL =
        "https://www.chabad.org/library/article_cdo/aid/4886/jewish/Counting-the-Omer.htm"

    /** Stable public holiday hubs (same destinations already referenced across the web). */
    private val HOLIDAY_URLS = mapOf(
        "rosh_hashana" to "https://www.chabad.org/library/article_cdo/aid/4762/jewish/What-Is-Rosh-Hashanah.htm",
        "yom_kippur" to "https://www.chabad.org/library/article_cdo/aid/4688/jewish/What-Is-Yom-Kippur.htm",
        "sukkot" to "https://www.chabad.org/library/article_cdo/aid/4784/jewish/What-Is-Sukkot.htm",
        "chol_hamoed_sukkot" to "https://www.chabad.org/library/article_cdo/aid/4784/jewish/What-Is-Sukkot.htm",
        "hoshana_raba" to "https://www.chabad.org/library/article_cdo/aid/757453/jewish/Hoshana-Rabbah.htm",
        "shemini_atzeret" to "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm",
        "simchat_torah" to "https://www.chabad.org/library/article_cdo/aid/4464/jewish/What-Is-Shemini-Atzeret-Simchat-Torah.htm",
        "chanukah" to "https://www.chabad.org/holidays/chanukah/default_cdo/jewish/Chanukah.htm",
        "purim" to "https://www.chabad.org/holidays/purim/default_cdo/jewish/Purim.htm",
        "purim_katan" to "https://www.chabad.org/holidays/purim/default_cdo/jewish/Purim.htm",
        "shushan_purim_katan" to "https://www.chabad.org/holidays/purim/default_cdo/jewish/Purim.htm",
        "pesach" to "https://www.chabad.org/holidays/passover/default_cdo/jewish/Passover.htm",
        "erev_pesach" to "https://www.chabad.org/holidays/passover/default_cdo/jewish/Passover.htm",
        "chol_hamoed_pesach" to "https://www.chabad.org/holidays/passover/default_cdo/jewish/Passover.htm",
        "pesach_sheni" to "https://www.chabad.org/library/article_cdo/aid/4377624/jewish/What-Is-Pesach-Sheni-the-Second-Passover.htm",
        "shavuot" to "https://www.chabad.org/holidays/shavuot/default_cdo/jewish/Shavuot.htm",
        "lag_baomer" to "https://www.chabad.org/library/article_cdo/aid/679300/jewish/What-Is-Lag-BaOmer.htm",
        "tu_bshvat" to "https://www.chabad.org/library/article_cdo/aid/468738/jewish/Tu-BiShvat-What-and-How.htm",
        "tu_beav" to "https://www.chabad.org/library/article_cdo/aid/717167/jewish/7-Joyous-Events-That-Happened-on-the-15th-of-Av.htm",
        "tisha_beav" to "https://www.chabad.org/library/article_cdo/aid/144575/jewish/What-Is-Tisha-BAv.htm",
        "fast_gedaliah" to "https://www.chabad.org/library/article_cdo/aid/2316462/jewish/Tzom-Gedaliah-Fast-Day.htm",
        "fast_10_tevet" to "https://www.chabad.org/library/article_cdo/aid/3170662/jewish/What-Is-Asarah-BTevet-Tevet-10.htm",
        "fast_17_tammuz" to "https://www.chabad.org/library/article_cdo/aid/479885/jewish/The-17th-of-Tammuz.htm",
        "fast_esther" to "https://www.chabad.org/holidays/purim/article_cdo/aid/1473/jewish/Fast-of-Esther.htm",
        "sefirat_haomer" to OMER_URL,
        "yom_tov" to "https://www.chabad.org/library/article_cdo/aid/4762/jewish/What-Is-Rosh-Hashanah.htm",
    )

    private val HOLIDAY_TITLES = mapOf(
        "rosh_hashana" to "Rosh Hashana",
        "yom_kippur" to "Yom Kippur",
        "sukkot" to "Sukkot",
        "chol_hamoed_sukkot" to "Chol HaMoed Sukkot",
        "hoshana_raba" to "Hoshana Raba",
        "shemini_atzeret" to "Shemini Atzeret",
        "simchat_torah" to "Simchat Torah",
        "chanukah" to "Chanukah",
        "purim" to "Purim",
        "pesach" to "Pesach",
        "erev_pesach" to "Erev Pesach",
        "shavuot" to "Shavuot",
        "lag_baomer" to "Lag BaOmer",
        "tisha_beav" to "Tisha B'Av",
        "fast_17_tammuz" to "Fast of 17 Tammuz",
        "fast_esther" to "Fast of Esther",
        "sefirat_haomer" to "Sefirat HaOmer",
    )
}
