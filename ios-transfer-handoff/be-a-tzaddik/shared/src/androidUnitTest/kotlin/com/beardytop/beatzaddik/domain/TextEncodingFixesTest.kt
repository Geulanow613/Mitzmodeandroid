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
