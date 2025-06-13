package edu.kit.ifv.mobitopp.actitopp.modernization.durations

import edu.kit.ifv.mobitopp.actitopp.IPerson
import edu.kit.ifv.mobitopp.actitopp.modernization.LinkedActivity
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.DayPlan
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.MobilityPlan
import edu.kit.ifv.mobitopp.actitopp.modernization.plan.TourPlan

data class MobilityPlanInputs(
    val mobilityPlan: MobilityPlan,
    val person: IPerson,
    val dayPlan: DayPlan,
    val tourPlan: TourPlan,
    val activity: LinkedActivity,
) {
    val tourMainActivityType = tourPlan.mainActivity.activityType
    val isLastTourOfDay: Boolean = dayPlan.tourPlans.last() == tourPlan

    val weekday = dayPlan.durationDay.weekday

    val isLastDay = mobilityPlan.dayPlans.last() == dayPlan
}


