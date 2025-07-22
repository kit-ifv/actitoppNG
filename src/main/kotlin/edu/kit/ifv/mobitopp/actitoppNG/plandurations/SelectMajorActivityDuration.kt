package edu.kit.ifv.mobitopp.actitoppNG.plandurations

import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.choicemodels.MINOR
import edu.kit.ifv.mobitopp.actitoppNG.plandurations.parameters.ParameterCollectionStep8J
import kotlin.time.Duration

fun interface SelectMajorActivityDuration {
    fun getDuration(input: MobilityPlanInputs): Duration
}

fun interface SelectMinorActivityDuration {
    fun getDuration(input: MobilityPlanInputs): Duration
}

class AssignMinorActivityDuration(
    val rngHelper: RNGHelper,
    private val histogram: ActivityDurationHistograms<ParameterCollectionStep8J> = MINOR,
) : SelectMinorActivityDuration {
    override fun getDuration(input: MobilityPlanInputs): Duration {
        return input.run {
            val bounds = dayPlan.activityDurationBounds(activity)

            histogram.select(
                rngHelper, bounds,
                MainDurationAlternative(
                    this
                )
            )

        }


    }
}