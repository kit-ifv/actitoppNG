package edu.kit.ifv.mobitopp.actitoppNG.timebudgets

import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class ChangeTrackingArrayHistogramTest {
    @Test
    fun properLookupTable() {
        val expected: Double = ChangeTrackingArrayHistogram.getForSize(0)
        assertEquals(expected, 1.0 /3)

        assertEquals(ChangeTrackingArrayHistogram.getForSize(1), 5.0 / 9.0)
    }
}