package edu.kit.ifv.mobitopp.actitopp.tourstarttimes

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.plandurations.ActivityDurationHistograms
import edu.kit.ifv.mobitopp.actitopp.plandurations.MainDurationSituation
import edu.kit.ifv.mobitopp.actitopp.timebudgets.ArrayHistogram
import kotlin.time.Duration


class TourStartWithPreference<P>(
    private val rng: RNGHelper,
    private val startTimeHistograms: ActivityDurationHistograms<P>,
    override val preferredTourStart: ArrayHistogram? = null,
    private val usePreferredTourStart: Boolean,
) :
    SelectTourStartWithPreference {
    val strategy = UseStartViaChoiceModel(rng)
    val useStrategy = if (usePreferredTourStart) strategy else UsePreferredTourStart { _, _ -> false }
    override fun selectStartTime(input: MobilityPlanInputs, preferredTourStart: ArrayHistogram?): Duration {
        val bounds = input.dayPlan.startTimeBoundsFor(input.tourPlan)

        val rnd1 = rng.randomValue
        val rnd2 = rng.randomValue

        preferredTourStart?.let {

            if (useStrategy.usePreferredTourStart(input, it) && it.intersects(bounds)) {
                // In this instance we discard rnd1 because it is not required to find out that we want to use our preferred histogram. The legacy code will always generate the random number, so for consistency we always need to consume it
                return it.select(
                    rnd2,
                    bounds.start,
                    bounds.endInclusive
                )
            }

        }
        return startTimeHistograms.select(rnd1, rnd2, bounds) {
            MainDurationSituation(it, input)
        }
    }
}