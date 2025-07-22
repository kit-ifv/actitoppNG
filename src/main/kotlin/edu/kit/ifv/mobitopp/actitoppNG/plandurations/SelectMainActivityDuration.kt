package edu.kit.ifv.mobitopp.actitoppNG.plandurations


import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.modernization.durations.MobilityPlanInputs
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
        // If the mean activity duration does not fit the remaining bounds of the day, we need to default.
        val meanActivityDurationFitsBounds = input.budgetFitsIntoBounds()
        return input.run {

            when {

                useStandardDuration && meanActivityDurationFitsBounds -> calculateFixedAndTarnish(this)
                else -> calculateDefault(this)
            }
        }
    }

    fun MobilityPlanInputs.budgetFitsIntoBounds(): Boolean {
        return dayPlan.getBudget(tourMainActivityType) in dayPlan.activityDurationBounds(activity)
    }

    fun calculateFixedAndTarnish(input: MobilityPlanInputs): Duration {
        return input.run {
            val meanActivityDuration = dayPlan.getBudget(tourMainActivityType)
            val bounds = dayPlan.activityDurationBounds(activity)
            taintedHistogramMap[activity.activityType]?.selectAndTaint(
                rng,
                bounds,
                meanActivityDuration, MainDurationAlternative(this)
            ) ?: throw NoSuchElementException("There are no tainted histograms for ${input.activity.activityType}")

        }
    }

    fun calculateDefault(input: MobilityPlanInputs): Duration {

        return input.run {
            val bounds = dayPlan.activityDurationBounds(tourPlan.mainActivity)
            taintedHistogramMap[input.activity.activityType]?.select(
                rng,
                bounds,
                MainDurationAlternative(this)
            ) ?: throw NoSuchElementException("There are no tainted histograms for ${input.activity.activityType}")
        }
    }
}