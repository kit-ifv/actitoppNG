package edu.kit.ifv.mobitopp.actitoppNG.modernization

import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.enums.ActivityType
import kotlin.random.Random

class SideTourMainActivityGenerator(
    private val mobilityStructure: MobilityStructure,
) {

    private val mainActivityOfSideTours: AssignMainActivityOfSideTour =
        AssignByUtilityFunction(mobilityStructure)
    context(rng: Random, params: PlanGenerationParameters)
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

    }
    context(rng: Random, params: PlanGenerationParameters)
    fun loadSideTours(tourAmounts: Map<DurationDay, PlannedTourAmounts>) {
        val targets = generateSideTours(tourAmounts)
        targets.forEach { dayStructure, (prec, succ) ->
            dayStructure.loadPrecursors(prec)
            dayStructure.loadSuccessors(succ)
        }
    }
}