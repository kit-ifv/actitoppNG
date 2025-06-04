package edu.kit.ifv.mobitopp.actitopp.steps.step8


import edu.kit.ifv.mobitopp.actitopp.RNGKeeper
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import java.util.EnumSet
import kotlin.time.Duration

fun interface SelectMainActivityDuration {
    fun getDuration(input: MobilityPlanInputs): Duration
}

class StandardStep8B<P>(
    private val rng: RNGKeeper,
    histogram: ActivityDurationHistograms<P>,
    val categoryDiscreteChoiceID: String,
    val weightedRandomDrawID: String,
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
            taintedHistograms.selectAndTaint(rng.pull(categoryDiscreteChoiceID), rng.pull(weightedRandomDrawID), meanActivityDuration) {
                MainDurationSituation(
                    it,
                    this
                )
            }

        }
    }

    fun calculateDefault(input: MobilityPlanInputs): Duration {

        return input.run {
            val bounds = dayPlan.activityDurationBounds(tourPlan.mainActivity)
            taintedHistograms.select(rng.pull(categoryDiscreteChoiceID), rng.pull(weightedRandomDrawID), bounds) {
                MainDurationSituation(
                    it,
                    this
                )
            }
        }
    }
}