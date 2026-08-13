package com.beardytop.beatzaddik.domain

import com.beardytop.beatzaddik.data.ChecklistLoader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LineageChecklistFilterTest {

    @Test
    fun loaderMapsKohenLeviFirstbornFlags() {
        val json = """
            {"version":918273,"items":[
              {"id":"observe_laws_of_kohanim","title":"K","section":"Permanent ongoing mitzvot","gender":"male","kohen":true,"persistChecked":true},
              {"id":"levi_duties","title":"L","section":"Permanent ongoing mitzvot","gender":"male","levi":true,"persistChecked":true},
              {"id":"pidyon_haben","title":"P","section":"Permanent ongoing mitzvot","gender":"male","firstborn":true,"persistChecked":true},
              {"id":"brit_milah","title":"B","section":"Permanent ongoing mitzvot","gender":"male","persistChecked":true}
            ]}
        """.trimIndent()
        val items = ChecklistLoader.load(json, null)
        assertEquals(true, items.single { it.id == LineageChecklistIds.KOHANIM_LAWS }.kohen)
        assertEquals(true, items.single { it.id == LineageChecklistIds.LEVI_DUTIES }.levi)
        assertEquals(true, items.single { it.id == LineageChecklistIds.PIDYON_HABEN }.firstborn)
        assertNotNull(items.single { it.id == LineageChecklistIds.BRIT_MILAH })
        assertTrue(items.all { it.persistChecked })
    }

    @Test
    fun hasFirstbornSonQualifiesForFirstbornChecklistItems() {
        val onlyHasSon = UserProfile(gender = Gender.MALE, hasFirstbornSon = true)
        assertTrue(onlyHasSon.showsFirstbornChecklistItems)
        val onlySelf = UserProfile(gender = Gender.MALE, isFirstbornSon = true)
        assertTrue(onlySelf.showsFirstbornChecklistItems)
        assertFalse(UserProfile(gender = Gender.MALE).showsFirstbornChecklistItems)
    }

    @Test
    fun kohenAndLeviAreExemptFromPidyonHabenChecklistItem() {
        assertFalse(
            UserProfile(gender = Gender.MALE, isKohen = true, isFirstbornSon = true).showsFirstbornChecklistItems,
        )
        assertFalse(
            UserProfile(gender = Gender.MALE, isLevi = true, hasFirstbornSon = true).showsFirstbornChecklistItems,
        )
    }
}
