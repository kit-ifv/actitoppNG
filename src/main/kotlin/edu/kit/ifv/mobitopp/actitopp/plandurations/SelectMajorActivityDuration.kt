package edu.kit.ifv.mobitopp.actitopp.plandurations

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.plandurations.choicemodels.MINOR
import edu.kit.ifv.mobitopp.actitopp.plandurations.parameters.ParameterCollectionStep8J
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

            val rnd1 = rngHelper.randomValue
            val rnd2 = rngHelper.randomValue
            histogram.select(rnd1, rnd2, bounds) {
                MainDurationAlternative(
                    choice = it,
                    this
                )
            }
        }


    }
}