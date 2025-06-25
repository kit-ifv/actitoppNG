package edu.kit.ifv.mobitopp.actitopp.tourstarttimes

import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.timebudgets.ArrayHistogram

/**
 * This interface is tasked to determine whether the tour start should use the preferred histogram to select the concrete
 * start time or not. The existence of the preferred histogram is necessary for this evaluation to return a time
 * from said histogram.
 */
fun interface UsePreferredTourStart {
    fun usePreferredTourStart(input: MobilityPlanInputs, preferredHistogram: ArrayHistogram): Boolean

    companion object {
        /**
         * The standard [DISABLED] implementation always returns false when determining whether to use the preferred
         * histogram.
         */
        val DISABLED = UsePreferredTourStart { _, _ -> false }
    }
}


