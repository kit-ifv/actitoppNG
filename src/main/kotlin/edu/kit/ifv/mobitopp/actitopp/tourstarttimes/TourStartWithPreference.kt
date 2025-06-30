package edu.kit.ifv.mobitopp.actitopp.tourstarttimes

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitopp.plandurations.MainDurationAlternative
import edu.kit.ifv.mobitopp.actitopp.timebudgets.ArrayHistogram
import kotlin.time.Duration


class TourStartWithPreference<P>(
    private val rng: RNGHelper,
    private val startTimeHistograms: ActivityDurationHistograms<P>,
    override val preferredTourStart: ArrayHistogram? = null,
    private val strategy: UsePreferredTourStart,

    ) :
    SelectTourStartWithPreference {
    override fun selectStartTime(input: MobilityPlanInputs, preferredTourStart: ArrayHistogram?): Duration {
        val bounds = input.dayPlan.startTimeBoundsFor(input.tourPlan)



        preferredTourStart?.let {

            if (strategy.usePreferredTourStart(input, it) && it.intersects(bounds)) {
                // In this instance we discard rnd1 because we already know the histogram we want to use.
                // The preferredTourStart field. However for testability we need to consume rnd1.
                rng.randomValue // Important for tests to keep the randomness at the same state
                return it.select(
                    rng,
                    bounds.start,
                    bounds.endInclusive
                )
            }

        }
        return startTimeHistograms.select(rng, bounds) {
            MainDurationAlternative(it, input)
        }
    }
}