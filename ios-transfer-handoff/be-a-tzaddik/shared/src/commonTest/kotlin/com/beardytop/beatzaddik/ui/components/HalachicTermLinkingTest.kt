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

    @Test
    fun pageTracker_underlinesGlossaryTermOnlyOnFirstTextBlock() {
        val tracker = HalachicTermUsageTracker()
        val first = buildBodyAnnotatedString(
            text = "Doing a mitzvah connects you to Hashem.",
            enableTerms = true,
            bodyColor = Color.Black,
            usedOnPage = tracker,
            ownerKey = "block-1",
        )
        val second = buildBodyAnnotatedString(
            text = "Another mitzvah later today still counts.",
            enableTerms = true,
            bodyColor = Color.Black,
            usedOnPage = tracker,
            ownerKey = "block-2",
        )
        val tag = HalachicTermsDictionary.annotationTag()
        fun mitzvahCount(annotated: AnnotatedString) =
            annotated.getStringAnnotations(tag, 0, annotated.length)
                .count { annotated.text.substring(it.start, it.end).equals("mitzvah", ignoreCase = true) }
        assertEquals(1, mitzvahCount(first))
        assertEquals(0, mitzvahCount(second))
    }

    @Test
    fun pageTracker_sameOwnerKeepsUnderlineOnRebuild() {
        val tracker = HalachicTermUsageTracker()
        val text = "A mitzvah is a command from Hashem."
        val first = buildBodyAnnotatedString(
            text = text,
            enableTerms = true,
            bodyColor = Color.Black,
            usedOnPage = tracker,
            ownerKey = text,
        )
        val rebuilt = buildBodyAnnotatedString(
            text = text,
            enableTerms = true,
            bodyColor = Color.Red,
            usedOnPage = tracker,
            ownerKey = text,
        )
        val tag = HalachicTermsDictionary.annotationTag()
        fun mitzvahCount(annotated: AnnotatedString) =
            annotated.getStringAnnotations(tag, 0, annotated.length)
                .count { annotated.text.substring(it.start, it.end).equals("mitzvah", ignoreCase = true) }
        assertEquals(1, mitzvahCount(first))
        assertEquals(1, mitzvahCount(rebuilt))
    }

    private fun annotationCovers(
        annotated: AnnotatedString,
        range: AnnotatedString.Range<String>,
        expected: String,
    ): Boolean =
        annotated.text.substring(range.start, range.end).equals(expected, ignoreCase = true)
}
