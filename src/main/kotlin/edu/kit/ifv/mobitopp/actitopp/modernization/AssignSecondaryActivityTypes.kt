package edu.kit.ifv.mobitopp.actitopp.modernization

import discreteChoice.EnumeratedDiscreteChoiceModel
import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.enums.ActivityType
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.choicemodels.sideActivityChoiceModel
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.parameters.SideActivitySet
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.shenanigans.ActivityAlternative
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalIndexedValue
import edu.kit.ifv.mobitopp.actitopp.utils.Position

data class SecondaryActInput(
    val dayStructure: DayStructure,
    val tourStructure: BidirectionalIndexedValue<TourStructure>,
    val plannedTourAmounts: PlannedTourAmounts,
)

interface AssignSecondaryActivityTypes {
    fun generateSecondaryActivityTypes(input: SecondaryActInput): Pair<List<ActivityType>, List<ActivityType>>
}

/**
 * Generate the secondary activities, with respect to the week routine, so that the options for work and education
 * are removed if the week routine is already satisfied.
 */
class TrackedSecondaryActivities(
    private val mobilityStructure: MobilityStructure,

    val rngHelper: RNGHelper,
    private val choiceModel: EnumeratedDiscreteChoiceModel<ActivityType, ActivityAlternative, *> = sideActivityChoiceModel,
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
                val availableOptions = choiceModel.choices.toMutableSet()
                if (!personWithRoutine.person.isAllowedToWork) availableOptions.remove(ActivityType.WORK)
                if (day.shouldNotBeWorkDay(routine)) availableOptions.remove(
                    ActivityType.WORK
                )
                if (day.shouldNotBeEducationDay(routine)) availableOptions.remove(
                    ActivityType.EDUCATION
                )
                val converter: (ActivityType) -> ActivityAlternative = {
                    ActivityAlternative(
                        it, personWithRoutine,
                        input.dayStructure,
                        input.tourStructure, position, input.plannedTourAmounts
                    )
                }
                choiceModel.select(availableOptions, rngHelper, converter)
            }
        }
    }


}