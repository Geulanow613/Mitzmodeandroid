package com.beardytop.beatzaddik.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ZmanCountdownFormatterTest {

    @Test
    fun underOneMinuteUsesOneMinNotSoon() {
        val now = 1_000_000L
        assertEquals("1 min", ZmanCountdownFormatter.formatDuration(now + 30_000, now))
        assertEquals("1 min", ZmanCountdownFormatter.formatDuration(now + 59_999, now))
    }

    @Test
    fun upcomingUsesCountdownForHebrewNotAbsoluteClock() {
        val now = 1_000_000L
        val dawn = now + 5 * 60_000
        val (key, args) = ZmanCountdownFormatter.upcomingSummaryTemplate(
            windowStartMillis = dawn,
            nowMillis = now,
            atLabel = "dawn",
            timezoneId = "Asia/Jerusalem",
            languageCode = "he",
        )!!
        assertEquals("Available in {countdown}{at}", key)
        assertEquals("5m", args["countdown"])
        assertFalse(args.containsKey("time"))
    }

    @Test
    fun imminentWindowShowsAvailableNow() {
        val now = 1_000_000L
        val (key, args) = ZmanCountdownFormatter.upcomingSummaryTemplate(
            windowStartMillis = now + 10_000,
            nowMillis = now,
            atLabel = "dawn",
            timezoneId = "America/New_York",
            languageCode = "en",
        )!!
        assertEquals("Available now{at}", key)
        assertEquals(" · at dawn", args["at"])
    }

    @Test
    fun pastWindowStart_doesNotClaimAvailableNow() {
        val now = 5_000_000L
        val result = ZmanCountdownFormatter.upcomingSummaryTemplate(
            windowStartMillis = now - 60_000L,
            nowMillis = now,
            atLabel = "sunset",
            timezoneId = "Asia/Jerusalem",
            languageCode = "en",
        )
        assertEquals(null, result)
    }

    @Test
    fun filledSummaryNeverContainsInSoon() {
        val now = 1_000_000L
        val summary = ZmanCountdownFormatter.upcomingSummary(
            windowStartMillis = now + 45_000,
            nowMillis = now,
            atLabel = "dawn",
            timezoneId = "America/New_York",
            languageCode = "en",
        )!!
        assertEquals("Available in 1 min · at dawn", summary)
    }
}
