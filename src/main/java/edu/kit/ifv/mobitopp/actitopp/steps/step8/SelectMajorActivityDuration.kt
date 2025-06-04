package edu.kit.ifv.mobitopp.actitopp.steps.step8

import edu.kit.ifv.mobitopp.actitopp.RNGKeeper
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import kotlin.time.Duration

fun interface SelectMajorActivityDuration {
    fun getDuration(input: MobilityPlanInputs): Duration
}

fun interface SelectMinorActivityDuration {
    fun getDuration(input: MobilityPlanInputs): Duration
}

class AssignMinorActivityDuration(val rngHelper: RNGKeeper,
                                  val categoryDiscreteChoiceID: String,
                                  val weightedRandomDrawID: String,
                                  private val histogram: ActivityDurationHistograms<ParameterCollectionStep8J> = MINOR): SelectMinorActivityDuration {
    override fun getDuration(input: MobilityPlanInputs): Duration {
        return input.run {
            val bounds = dayPlan.activityDurationBounds(activity)

            val rnd1 = rngHelper.pull(categoryDiscreteChoiceID)
            val rnd2 = rngHelper.pull(weightedRandomDrawID)
            histogram.select(rnd1, rnd2, bounds) {
                MainDurationSituation(
                    choice = it,
                    this
                )
            }
        }


    }
}