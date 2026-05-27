package edu.kit.ifv.mobitopp.actitoppNG.plandurations


import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import edu.kit.ifv.mobitopp.actitoppNG.modernization.durations.MobilityPlanInputs
import java.util.*
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun interface SelectMainActivityDuration {
    context(rng: Random)
    fun getDuration(input: MobilityPlanInputs): Duration
}

/**
 * In the legacy code the probability of a selected time increased, if the activity is using a standard duration, a proper
 * activity type: Work, Education. The original code had a hidden side effect that the first activity of a day and the
 * remaining major activities manipulated their own respective time-tables. IN a sense the selection "sticks around"
 *
 * Each time the Sticky Selector is built the tainted histograms are copied.
 */
class StickySelector<P>(
    histogram: ActivityDurationHistograms<P>,
    private val useStandardDuration: StandardDuration,
) : SelectMainActivityDuration, SelectMajorActivityDuration {
    val taintedHistogramMap: Map<ActivityType, TaintedActivityDurationHistograms<P>> = ActivityType.OUTOFHOMEACTIVITY.associateWith { histogram.taint() }

    /**
     * To avoid constant alloc/dealloc the sticky selector must be able to reset. Luckily the Tainted Histograms class
     * provides a mapping between the taintable histograms and the clean original histograms. thus we can simply reset
     * into that.
     */
    fun reset() = taintedHistogramMap.values.forEach { it.reset() }

    companion object {
        @JvmStatic val fixedTypes: EnumSet<ActivityType> = EnumSet.of(ActivityType.WORK, ActivityType.EDUCATION)
    }

    context(rng: Random)
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
    context(rng: Random)
    fun calculateFixedAndTarnish(input: MobilityPlanInputs): Duration {
        return input.run {
            val meanActivityDuration = dayPlan.getBudget(tourMainActivityType)
            val bounds = dayPlan.activityDurationBounds(activity)
            if(bounds.isEmpty()) return 1.minutes
            taintedHistogramMap[activity.activityType]?.selectAndTaint(
                bounds,
                meanActivityDuration, MainDurationAlternative(this)
            ) ?: throw NoSuchElementException("There are no tainted histograms for ${input.activity.activityType}")

        }
    }
    context(rng: Random)
    fun calculateDefault(input: MobilityPlanInputs): Duration {

        return input.run {
            val bounds = dayPlan.activityDurationBounds(tourPlan.mainActivity)
            if(bounds.isEmpty()) return 1.minutes
            taintedHistogramMap[input.activity.activityType]?.select(
                bounds,
                MainDurationAlternative(this)
            ) ?: throw NoSuchElementException("There are no tainted histograms for ${input.activity.activityType}")
        }
    }


}

