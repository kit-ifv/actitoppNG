package edu.kit.ifv.mobitopp.actitopp.plandurations


import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import java.util.EnumSet
import kotlin.time.Duration

fun interface SelectMainActivityDuration {
    fun getDuration(input: MobilityPlanInputs): Duration
}

/**
 * In the legacy code the probability of a selected time increased, if the activity is using a standard duration, a proper
 * activity type: Work, Education. The original code had a hidden side effect that the first activity of a day and the
 * remaining major activities manipulated their own respective time-tables. IN a sense the selection "sticks around"
 */
class StickySelector<P>(
    private val rng: RNGHelper,
    histogram: ActivityDurationHistograms<P>,

    private val useStandardDuration: StandardDuration = UtilityFunctionAssignment(rng),
) : SelectMainActivityDuration, SelectMajorActivityDuration {
    private val taintedHistogramMap = ActivityType.OUTOFHOMEACTIVITY.associateWith { histogram.taint() }
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
            val bounds = dayPlan.activityDurationBounds(input.activity)
            taintedHistogramMap[input.activity.activityType]?.selectAndTaint(
                rng,
                bounds,
                meanActivityDuration
            ) {
                MainDurationAlternative(
                    it,
                    this
                )
            } ?: throw NoSuchElementException("There are no tainted histograms for ${input.activity.activityType}")

        }
    }

    fun calculateDefault(input: MobilityPlanInputs): Duration {

        return input.run {
            val bounds = dayPlan.activityDurationBounds(tourPlan.mainActivity)
            taintedHistogramMap[input.activity.activityType]?.select(
                rng,
                bounds
            ) {
                MainDurationAlternative(
                    it,
                    this
                )
            } ?: throw NoSuchElementException("There are no tainted histograms for ${input.activity.activityType}")
        }
    }
}