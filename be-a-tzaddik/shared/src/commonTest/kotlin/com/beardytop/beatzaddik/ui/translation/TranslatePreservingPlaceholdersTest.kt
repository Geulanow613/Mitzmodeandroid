package com.beardytop.beatzaddik.ui.translation

import kotlin.test.Test
import kotlin.test.assertEquals

class TranslatePreservingPlaceholdersTest {
    @Test
    fun preservesBracePlaceholders() {
        val result = translatePreservingPlaceholdersSync("{weekday}, {month} {day}, {year}") { segment ->
            when (segment) {
                ", " -> " · "
                " " -> " "
                else -> "[$segment]"
            }
        }
        assertEquals("{weekday} · {month} {day} · {year}", result)
    }

    @Test
    fun preservesDollarPlaceholders() {
        val result = translatePreservingPlaceholdersSync("Hello \$name") { segment ->
            if (segment == "Hello ") "שלום " else segment
        }
        assertEquals("שלום \$name", result)
    }

    @Test
    fun translatesPlainStringWithoutPlaceholders() {
        val result = translatePreservingPlaceholdersSync("Ashkenaz") { "אשכנז" }
        assertEquals("אשכנז", result)
    }

    @Test
    fun preservesMinutesBadgeTemplate() {
        val result = translatePreservingPlaceholdersSync("{minutes} min") { segment ->
            if (segment == " min") " מינוט" else segment
        }
        assertEquals("{minutes} מינוט", result)
    }
}
