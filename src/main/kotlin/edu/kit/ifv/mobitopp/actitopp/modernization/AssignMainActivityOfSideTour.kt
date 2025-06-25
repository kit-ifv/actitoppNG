package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.tourMainActivityChoiceModel
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.TourAlternative
import edu.kit.ifv.mobitopp.actitopp.steps.TourPositionAttributesByIndex
import edu.kit.ifv.mobitopp.actitopp.utils.Position

class DayWithPlans(
    val dayStructure: DayStructure,
    val personWithRoutine: PersonWithRoutine,
    val plannedTourAmounts: PlannedTourAmounts,
)


fun interface AssignMainActivityOfSideTour {
    fun generateSideTourActivities(input: DayWithPlans): Pair<List<ActivityType>, List<ActivityType>>
}


class AssignByUtilityFunction(private val mobilityStructure: MobilityStructure, val rngHelper: RNGHelper) :
    AssignMainActivityOfSideTour {
    override fun generateSideTourActivities(input: DayWithPlans): Pair<List<ActivityType>, List<ActivityType>> {
        val plannedPrecursors = input.plannedTourAmounts.precursorAmount
        val plannedSuccessors = input.plannedTourAmounts.successorAmount
        val x = (0..<plannedPrecursors).map {
            it
        }
        val y = (0..<plannedSuccessors).map {
            it + plannedPrecursors + 1
        }
        return x.calculate(Position.BEFORE, input) to y.calculate(Position.AFTER, input)


    }

    private fun List<Int>.calculate(position: Position, input: DayWithPlans): List<ActivityType> {
        return map { absoluteIndex ->
            mobilityStructure.generateTrackedActivity(input.dayStructure.startTimeDay) { day ->

                val routine = input.personWithRoutine.routine
                val availableOptions = tourMainActivityChoiceModel.registeredOptions().toMutableSet()
                if (!input.personWithRoutine.person.isAllowedToWork) availableOptions.remove(ActivityType.WORK)
                if (day.shouldNotBeWorkDay(routine)) availableOptions.remove(
                    ActivityType.WORK
                )
                if (day.shouldNotBeEducationDay(routine)) availableOptions.remove(
                    ActivityType.EDUCATION
                )

                val rnd = rngHelper.randomValue
                tourMainActivityChoiceModel.select(availableOptions, rnd) {
                    TourAlternative(
                        it,
                        input.personWithRoutine.person,
                        input.personWithRoutine.routine,
                        input.dayStructure,
                        TourPositionAttributesByIndex(absoluteIndex, position)

                    )
                }
            }
        }
    }
}