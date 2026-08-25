package edu.kit.ifv.mobitopp.actitoppNG.timebudgets

import edu.kit.ifv.mobitopp.actitoppNG.enums.Category
import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random
import kotlin.test.Test

class ModifiableArrayHistogramTest {

    @Test
    fun simpleModification() {
        val histogram = ModifiableArrayHistogram(probabilities = DoubleArray(10) {0.1}, categoryIndex = Category(0))

        histogram.modify(0)

        assertEquals(histogram.probabilities[0], 0.4)
        assertEquals(histogram.probabilities[1], 0.1 * 2.0 / 3.0)

        histogram.modify(1)
    }
    @Test
    fun comparisionOfHistograms() {
        val originalHistogram = ModifiableArrayHistogram(probabilities = DoubleArray(10) {0.1}, categoryIndex = Category
            (0))

        val original = ArrayHistogram(probabilities = DoubleArray(10) {0.1}, categoryIndex = Category(0))
        val newHistogram = ChangeTrackingArrayHistogram(original)


        originalHistogram.modify(0)
        newHistogram.modify(0)

        val drawAmount = 1000000


        val originalDraws = context(Random(42)) {
            (0 until drawAmount).map {
                originalHistogram.select()
            }
        }

        val newDraws = context(Random(42)) {
            (0 until drawAmount).map {
                newHistogram.select()
            }
        }

        val originalCounts = originalDraws.groupingBy { it }.eachCount().toSortedMap()
        val newCounts = newDraws.groupingBy { it }.eachCount().toSortedMap()

        println("Original $originalCounts, New $newCounts")

    }
}