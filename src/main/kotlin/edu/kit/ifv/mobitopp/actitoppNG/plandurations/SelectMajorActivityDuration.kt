package edu.kit.ifv.mobitopp.actitoppNG.plandurations

import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.modernization.durations.MobilityPlanInputs
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun interface SelectMajorActivityDuration {
    context(rng: Random)
    fun getDuration(input: MobilityPlanInputs): Duration
}

fun interface SelectMinorActivityDuration {
    context(rng: Random)
    fun getDuration(input: MobilityPlanInputs): Duration
}

class AssignMinorActivityDuration(
    models: AllChoiceModels,
    private val histogram: HistogramSelector = models.minorActivityDurationChoiceModel
) : SelectMinorActivityDuration {
    context(rng: Random)
    override fun getDuration(input: MobilityPlanInputs): Duration {
        return input.run {
            val bounds = dayPlan.activityDurationBounds(activity)
            if(bounds.isEmpty()) return 0.minutes
            histogram.select(
                bounds,
                MainDurationAlternative(
                    this
                )
            )
        }
    }
}