package edu.kit.ifv.mobitopp.actitopp.plandurations

import edu.kit.ifv.mobitopp.actitopp.modernization.durations.MobilityPlanInputs
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan

fun MobilityPlan.assignFirstMainActivities(strategy: SelectMainActivityDuration) {
    dayPlans.forEach { dayPlan ->
        val tourPlan = dayPlan.tourPlans.first()
        val activity = tourPlan.mainActivity
        activity.duration = strategy.getDuration(
            MobilityPlanInputs(
                mobilityPlan = this,
                person = person,
                dayPlan = dayPlan,
                tourPlan = tourPlan,
                activity = activity
            )
        )

    }
}

fun MobilityPlan.assignSecondaryMainActivities(strategy: SelectMajorActivityDuration) {
    dayPlans.forEach { dayPlan ->
        dayPlan.tourPlans.drop(1).forEach { tourPlan ->
            val activity = tourPlan.mainActivity
            activity.duration = strategy.getDuration(
                MobilityPlanInputs(
                    mobilityPlan = this,
                    person = person,
                    dayPlan = dayPlan,
                    tourPlan = tourPlan,
                    activity = activity
                )
            )

        }
    }
}

fun MobilityPlan.assignMinorActivities(strategy: SelectMinorActivityDuration) {
    dayPlans.forEach { dayPlan ->
        dayPlan.tourPlans.forEach { tourPlan ->
            tourPlan.minorActivities.forEach { activity ->
                activity.duration = strategy.getDuration(
                    MobilityPlanInputs(
                        mobilityPlan = this,
                        person = person,
                        dayPlan = dayPlan,
                        tourPlan = tourPlan,
                        activity = activity,
                    )
                )

            }
        }
    }
}