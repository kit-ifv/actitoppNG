package edu.kit.ifv.mobitopp.actitopp.steps.step7

import edu.kit.ifv.mobitopp.actitopp.changes.Category
import org.junit.jupiter.api.Assertions.*
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.time.Duration.Companion.minutes

class ArrayHistogramTest {
    @Test
    fun boundedSelectionTakesProperElements() {
        val histogram = ArrayHistogram(offset = 0, listOf(1, 1, 1, 1, 1), Category(1))
        assertEquals(histogram[0], 0.2)
        assertEquals(histogram[2], 0.2)

        assertEquals(histogram.select(0.5), 2.minutes)

        assertEquals(histogram.selectInt(0.45, lowerBoundInclusive = 0, upperBoundInclusive = 1), 0.minutes)
    }

    @Test
    fun properContainsCheck() {
        val histogram = ArrayHistogram.fromPath(Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh/8C_KAT_8.csv"))
        val earlierHistogram = ArrayHistogram.fromPath(Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh/8C_KAT_7.csv"))
        assertFalse(earlierHistogram.contains(360))
        assertTrue(histogram.contains(360))
        assertTrue(histogram.contains(361))
    }
    @Test
    fun taintingWorks() {
        val histogram = ArrayHistogram.fromPath(Path("src/main/resources/edu/kit/ifv/mobitopp/actitopp/mopv14_withpkwhh/8C_KAT_5.csv"))
        val originalProbabilities = histogram.probabilities()
        val taint = histogram.copy()
        assertContentEquals(originalProbabilities, histogram.probabilities())
        taint.modify(181)
        assertContentEquals(originalProbabilities, histogram.probabilities())


    }
}