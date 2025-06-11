package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.ActivitySituation
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.step6WithParams
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalIndexedValue
import edu.kit.ifv.mobitopp.actitopp.utils.Position

data class SecondaryActInput(
    val dayStructure: DayStructure,
    val tourStructure: BidirectionalIndexedValue<MutableTourStructure>,
    val plannedTourAmounts: PlannedTourAmounts,
)

interface AssignSecondaryActivityTypes {
    fun generateSecondaryActivityTypes(input: SecondaryActInput): Pair<List<ActivityType>, List<ActivityType>>

    fun assignDirectly(input: SecondaryActInput) {
        val (predecessors, successors) = generateSecondaryActivityTypes(input)
        val tour = input.tourStructure.element
        tour.loadPrecursors(predecessors)
        tour.loadSuccessors(successors)
    }
}

fun Map<DayStructure, Map<BidirectionalIndexedValue<MutableTourStructure>, PlannedTourAmounts>>.assignDirectly(strategy: AssignSecondaryActivityTypes) {
    entries.forEach { (day, tourMap) ->
        tourMap.forEach { (tour, plannedTourAmounts) ->
            strategy.assignDirectly(SecondaryActInput(day, tour, plannedTourAmounts))
        }
    }

}

class ExampleAssign(
    val mobilityStructure: MobilityStructure,

    val rngHelper: RNGHelper,
) : AssignSecondaryActivityTypes {
    val personWithRoutine: PersonWithRoutine = mobilityStructure.weekRoutine
    override fun generateSecondaryActivityTypes(input: SecondaryActInput):
            Pair<List<ActivityType>, List<ActivityType>> {
        val precursors = input.plannedTourAmounts.precursorAmount
        val successors = input.plannedTourAmounts.successorAmount

        return (0..<precursors).calculate(Position.BEFORE, input) to
                (0..<successors).calculate(Position.AFTER, input)
    }

    private fun Iterable<Int>.calculate(position: Position, input: SecondaryActInput): List<ActivityType> {
        val day = input.dayStructure.startTimeDay
        val routine = personWithRoutine.routine
        return map { absoluteIndex ->
            mobilityStructure.generateTrackedActivity(input.dayStructure.startTimeDay) {
                val availableOptions = step6WithParams.registeredOptions().toMutableSet()
                if (!personWithRoutine.person.isAllowedToWork) availableOptions.remove(ActivityType.WORK)
                if (day.shouldNotBeWorkDay(routine)) availableOptions.remove(
                    ActivityType.WORK
                )
                if (day.shouldNotBeEducationDay(routine)) availableOptions.remove(
                    ActivityType.EDUCATION
                )
                val converter: (ActivityType) -> ActivitySituation = {
                    ActivitySituation(it, personWithRoutine,
                        input.dayStructure,
                        input.tourStructure, position, input.plannedTourAmounts)
                }
                val rnd = rngHelper.randomValue
                step6WithParams.select(availableOptions, rnd, converter)
            }
        }
    }


}