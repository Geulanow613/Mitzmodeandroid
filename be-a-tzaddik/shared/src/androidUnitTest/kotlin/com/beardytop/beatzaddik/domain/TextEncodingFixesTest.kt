package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.data.ChecklistLoader
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TextEncodingFixesTest {

    @Test
    fun repairMojibake_replacesBrokenEmDash() {
        val broken = "Talmud Torah \u00E2\u20AC\u201D daytime (Daily Torah Study)"
        val fixed = TextEncodingFixes.sanitizeDisplayTitle(broken)
        assertFalse(fixed.contains("\u00E2\u20AC"))
        assertTrue(fixed == "Talmud Torah - daytime (Daily Torah Study)")
    }

    @Test
    fun repairMojibake_recoversDoubleEncodedHebrew() {
        // "כשר" UTF-8 bytes misread as Latin-1: ×›×©×¨
        val broken = "Kosher (\u00D7\u203A\u00D7\u0161\u00D7\u00A8)"
        // Prefer a realistic sequence from Latin-1 mapping of D7 9B D7 A9 D7 A8:
        val latin1Broken = "Kosher (\u00D7\u009B\u00D7\u00A9\u00D7\u00A8)"
        val fixed = TextEncodingFixes.repairMojibake(latin1Broken)
        assertTrue(fixed.contains("\u05DB") || fixed.contains("\u05E9") || fixed.contains("\u05E8"), fixed)
        assertFalse(fixed.contains("\u00D7\u009B"))
    }

    @Test
    fun checklistLoader_sanitizesTitlesEvenIfJsonHasMojibake() {
        val mojiEm = "\u00E2\u20AC\u201D"
        val json = """
            {"version":991001,"items":[
              {"id":"tt_day","title":"Talmud Torah $mojiEm daytime (Daily Torah Study)","section":"Torah Study","timeOfDay":"day"},
              {"id":"tt_night","title":"Talmud Torah \u2013 nighttime (Daily Torah Study)","section":"Torah Study","timeOfDay":"night"}
            ]}
        """.trimIndent()
        val items = ChecklistLoader.load(json)
        items.forEach { item ->
            assertFalse(item.title.contains("\u00E2\u20AC"), "title still mojibake: ${item.title}")
            assertFalse(item.title.contains('\u2014') || item.title.contains('\u2013'), "title still fancy dash: ${item.title}")
            assertTrue(item.title.contains(" - "))
        }
    }
}
