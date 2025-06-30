package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.subsubTourAmount

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.PersonWithRoutine
import edu.kit.ifv.mobitopp.actitopp.modernization.DayStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.TourStructure
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalIndexedValue

/**
 * Keep track of the amount of "activities" placed within the day, to avoid the side effect shenanigans of the legacy
 * code. We want to determine the amount of activities in bulk, before even considering spawning a single activity.
 */
class DayAmountTracker(val day: DayStructure, val rngHelper: RNGHelper, val personWithRoutine: PersonWithRoutine) {

    private var counter = 2 * day.amountOfElements()
    private var remainingPlacements = day.minimumAmountOfActivitiesByJointActions - day.amountOfActivities()

    private val successorGenerator = FollowingSpawns(rngHelper)
    private val predecessorGenerator = PrecedingSpawns(rngHelper)

    /**
     * This map is required to fetch the calculation of the predecessor amount for each tour, since the predecessors
     * need to be calculated as a block, before the successors are calculated. Blame the original implementation for
     * that, because it makes no sense.
     */
    private val calculatedPredecessorAmount: MutableMap<BidirectionalIndexedValue<TourStructure>, Int> = mutableMapOf()

    fun generatePredecessors(indexedTours: Collection<BidirectionalIndexedValue<TourStructure>>): Map<BidirectionalIndexedValue<TourStructure>, Int> {
        val predecessorActivityAmounts = indexedTours.associateWith {
            generatePredecessor(it)
        }

        return predecessorActivityAmounts
    }

    fun generatePredecessor(indexedTour: BidirectionalIndexedValue<TourStructure>): Int {
        return generatePredecessorActivityAmounts(indexedTour, calculateMinimumAmount()).also { amount ->
            remainingPlacements -= amount
            counter--
            calculatedPredecessorAmount[indexedTour] = amount
        }
    }

    fun generateSuccessors(indexedTours: Collection<BidirectionalIndexedValue<TourStructure>>): Map<BidirectionalIndexedValue<TourStructure>, Int> {
        val successorActivityAmounts = indexedTours.associateWith {
            generateSuccessor(it)
        }
        return successorActivityAmounts
    }

    fun generateSuccessor(indexedTour: BidirectionalIndexedValue<TourStructure>): Int {
        return generateSuccessorActivityAmounts(indexedTour, calculateMinimumAmount()).also { amount ->
            remainingPlacements -= amount
            counter--
        }
    }

    private fun calculateMinimumAmount(): Int {
        return (remainingPlacements.toDouble() / counter).toInt()
    }

    private fun generatePredecessorActivityAmounts(
        input: BidirectionalIndexedValue<TourStructure>,
        minimumAmount: Int,
    ): Int {

        return predecessorGenerator.generateActivityAmount(
            SideTourActivityInput(
                personWithRoutine.person,
                personWithRoutine.routine,
                day,
                input,
                minimumAmount,
                0
            )
        )
    }

    private fun generateSuccessorActivityAmounts(
        input: BidirectionalIndexedValue<TourStructure>,
        minimumAmount: Int,
    ): Int {

        return successorGenerator.generateActivityAmount(
            SideTourActivityInput(
                personWithRoutine.person,
                personWithRoutine.routine,
                day,
                input,
                minimumAmount,
                calculatedPredecessorAmount[input] ?: 0
            )
        )
    }
}