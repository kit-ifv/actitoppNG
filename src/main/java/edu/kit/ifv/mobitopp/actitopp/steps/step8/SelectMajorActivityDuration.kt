package edu.kit.ifv.mobitopp.actitopp.steps.step8

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.ActDurationInputs
import kotlin.time.Duration

fun interface SelectMajorActivityDuration {
    fun getDuration(input: ActDurationInputs): Duration
}

fun interface SelectMinorActivityDuration {
    fun getDuration(input: ActDurationInputs): Duration
}

class AssignMinorActivityDuration(val rngHelper: RNGHelper,
                                  private val histogram: ActivityDurationHistograms<ParameterCollectionStep8J> = MINOR): SelectMinorActivityDuration {
    override fun getDuration(input: ActDurationInputs): Duration {
        return input.run {
            val bounds = dayPlan.boundsFor(activity)
            histogram.select(rngHelper, bounds) {
                MainDurationSituation(
                    choice = it,
                    this
                )
            }
        }


    }
}