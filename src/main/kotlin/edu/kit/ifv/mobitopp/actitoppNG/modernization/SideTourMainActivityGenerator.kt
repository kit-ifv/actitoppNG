package edu.kit.ifv.mobitopp.actitoppNG.modernization

import edu.kit.ifv.mobitopp.actitoppNG.RNGHelper
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType

class SideTourMainActivityGenerator(
    private val mobilityStructure: MobilityStructure,
    val rngHelper: RNGHelper,
) {

    private val mainActivityOfSideTours: AssignMainActivityOfSideTour =
        AssignByUtilityFunction(mobilityStructure, rngHelper)

    fun generateSideTours(tourAmounts: Map<DurationDay, PlannedTourAmounts>): Map<TrackedDayStructure, Pair<List<ActivityType>, List<ActivityType>>> {
        return mobilityStructure.elements().associateWith {
            val input =
                DayWithPlans(
                    it,
                    mobilityStructure.weekRoutine,
                    tourAmounts[it.startTimeDay] ?: PlannedTourAmounts.NONE
                )
            mainActivityOfSideTours.generateSideTourActivities(input)
        }

//        return mobilityStructure.mobileDays().associateWith {
//            val input =
//                DayWithPlans(
//                    it,
//                    mobilityStructure.weekRoutine,
//                    tourAmounts[it.startTimeDay] ?: PlannedTourAmounts.NONE
//                )
//            mainActivityOfSideTours.generateSideTourActivities(input)
//        }
    }

    fun loadSideTours(tourAmounts: Map<DurationDay, PlannedTourAmounts>) {
        val targets = generateSideTours(tourAmounts)
        targets.forEach { dayStructure, (prec, succ) ->
            dayStructure.loadPrecursors(prec)
            dayStructure.loadSuccessors(succ)
        }
    }
}