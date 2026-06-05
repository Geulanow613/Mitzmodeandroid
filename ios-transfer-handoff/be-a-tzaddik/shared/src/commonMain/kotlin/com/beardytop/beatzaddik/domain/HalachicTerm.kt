package com.beardytop.beatzaddik.domain

/**
 * A transliterated halachic term with one or more spellings to match in user-facing text.
 */
data class HalachicTerm(
    val id: String,
    val title: String,
    val definition: String,
    val literal: String? = null,
    val matchLabels: List<String> = listOf(title),
)

data class HalachicTermMatch(
    val start: Int,
    val end: Int,
    val term: HalachicTerm,
    val matchedText: String,
)
