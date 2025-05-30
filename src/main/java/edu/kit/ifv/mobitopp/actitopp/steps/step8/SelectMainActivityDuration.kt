package edu.kit.ifv.mobitopp.actitopp.steps.step8


import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import java.util.EnumSet
import kotlin.time.Duration

fun interface SelectMainActivityDuration {
    fun getDuration(input: MobilityPlanInputs): Duration
}

class StandardStep8B<P>(
    private val rng: RNGHelper,
    histogram: ActivityDurationHistograms<P>,
    private val useStandardDuration: StandardDuration = UtilityFunctionAssignment(rng),
) : SelectMainActivityDuration, SelectMajorActivityDuration {
    private val taintedHistograms = histogram.taint()

    private val fixedTypes = EnumSet.of(ActivityType.WORK, ActivityType.EDUCATION)
    override fun getDuration(input: MobilityPlanInputs): Duration {


        val useStandardDuration =
            input.tourMainActivityType in fixedTypes && useStandardDuration.getAssignedStandardDuration(input)
        return input.run {

            when {

                useStandardDuration -> calculateFixedAndTarnish(this)
                else -> calculateDefault(this)
            }
        }
    }

    fun calculateFixedAndTarnish(input: MobilityPlanInputs): Duration {
        return input.run {
            val meanActivityDuration = dayPlan.getBudget(tourMainActivityType)
            taintedHistograms.selectAndTaint(rng, meanActivityDuration) {
                MainDurationSituation(
                    it,
                    this
                )
            }

        }
    }

    fun calculateDefault(input: MobilityPlanInputs): Duration {

        return input.run {
            val bounds = dayPlan.boundsFor(tourPlan.mainActivity)
            taintedHistograms.select(rng, bounds) {
                MainDurationSituation(
                    it,
                    this
                )
            }
        }
    }
}