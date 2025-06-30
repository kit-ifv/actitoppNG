package edu.kit.ifv.mobitopp.actitoppNG.enums

import kotlinx.serialization.Serializable


/**
 * For Some reason there are a lot of casts for category from Int to String and back. Until I figure
 * out the very exact reason what category should be I will wrap it around integer, which appears
 * to be the nubmer in the _KAT_{X}.csv files.
 *
 * Note that category only exists because the utility functions for the duration of lead and major activities
 * requires the category by activity type, which differs. (Take a look at 7B and 7C for example, the same categories
 * map over a different time span)
 */
@JvmInline
@Serializable
value class Category(private val category: Int) {
    // To collect all comparisons against the category to simplify changing to a 0 based index sometime
    fun matches(index: Int): Boolean {
        return category == index
    }

    companion object {
        // Sometimes no category is picked, this is the representative of that case
        val NONE_CHOSEN = Category(0)
    }

    fun toInt() = category
}