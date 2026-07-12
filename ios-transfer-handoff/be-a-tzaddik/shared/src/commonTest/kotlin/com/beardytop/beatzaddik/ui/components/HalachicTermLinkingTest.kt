package com.beardytop.beatzaddik.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import com.beardytop.beatzaddik.domain.ChecklistLink
import com.beardytop.beatzaddik.domain.HalachicTermsDictionary
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HalachicTermLinkingTest {

    @Test
    fun findMatches_linksEachGlossaryTermOnlyOnce() {
        val text =
            "Learn about Oneg Shabbat! Taking pleasure in Shabbat (Oneg Shabbat) is a mitzvah. " +
                "The Talmud (Shabbat 118a) teaches that one who delights in Shabbat is rewarded. " +
                "Take that Shabbat nap and level up your next Shabbat table!"
        val matches = HalachicTermsDictionary.findMatches(text)
        val shabbatHits = matches.filter { it.matchedText.equals("Shabbat", ignoreCase = true) }
        assertEquals(1, shabbatHits.size, "expected a single Shabbat glossary underline, got $matches")
        val onegHits = matches.filter { it.matchedText.equals("Oneg Shabbat", ignoreCase = true) }
        assertEquals(1, onegHits.size, "expected a single Oneg Shabbat underline, got $matches")
        // Standalone Shabbat must be after the first Oneg Shabbat phrase, not buried inside it.
        assertTrue(shabbatHits.first().start >= onegHits.first().end)
    }

    @Test
    fun knownLinkThatIsGlossaryLabel_doesNotStealBodyUnderlines() {
        val text =
            "Learn about Oneg Shabbat! Taking pleasure in Shabbat is a mitzvah. " +
                "Delights in Shabbat and another Shabbat meal."
        val annotated = buildBodyAnnotatedString(
            text = text,
            knownLinks = listOf(
                ChecklistLink(
                    displayText = "Shabbat",
                    url = "https://www.chabad.org/library/article_cdo/aid/82898/jewish/Shabbat-Info.htm",
                ),
            ),
            enableTerms = true,
            bodyColor = Color.Black,
        )
        val urls = annotated.getStringAnnotations("URL", 0, annotated.length)
        val terms = annotated.getStringAnnotations(HalachicTermsDictionary.annotationTag(), 0, annotated.length)
        assertTrue(urls.isEmpty(), "glossary labels must not be inlined as blue URLs in the body")
        assertEquals(1, terms.count { annotationCovers(annotated, it, "Shabbat") })
    }

    @Test
    fun knownLink_nonGlossaryLabel_isLinkedOnlyOnce() {
        val text = "See Psalm 1 today, then read Psalm 1 again tonight."
        val annotated = buildBodyAnnotatedString(
            text = text,
            knownLinks = listOf(
                ChecklistLink(displayText = "Psalm 1", url = "https://example.com/psalm1"),
            ),
            enableTerms = true,
            bodyColor = Color.Black,
        )
        val urls = annotated.getStringAnnotations("URL", 0, annotated.length)
        assertEquals(1, urls.size)
        assertEquals("Psalm 1", annotated.text.substring(urls.first().start, urls.first().end))
    }

    private fun annotationCovers(
        annotated: AnnotatedString,
        range: AnnotatedString.Range<String>,
        expected: String,
    ): Boolean =
        annotated.text.substring(range.start, range.end).equals(expected, ignoreCase = true)
}
