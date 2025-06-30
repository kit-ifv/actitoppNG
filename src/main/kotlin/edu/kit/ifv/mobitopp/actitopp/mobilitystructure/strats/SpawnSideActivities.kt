package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.ByTracker
import edu.kit.ifv.mobitopp.actitopp.modernization.TrackedSecondaryActivities
import edu.kit.ifv.mobitopp.actitopp.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.PlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.modernization.SecondaryActInput
import edu.kit.ifv.mobitopp.actitopp.modernization.DeterminePlannedTourAmounts

fun interface SpawnSideActivities {
    fun spawnSideActivities(input: MobilityStructure)
}

class StandardImplementation(val rng: RNGHelper) : SpawnSideActivities {

    override fun spawnSideActivities(input: MobilityStructure) {
//        val amountGeneration = DeterminePlannedTourAmounts(input, rng)
//        val amounts = amountGeneration.calculate()
        val activityTypeGeneration = TrackedSecondaryActivities(input, rng)

        val cheatMap = input.elements().associateWith {

            val tourAmounts = ByTracker(it, rng)
            tourAmounts.determine(input)
        }

        input.elements().forEach { day ->

            day.trackedElements().forEach { indexedTour ->
                val originalTour = indexedTour.element
//                val plan = amounts[day]?.get(indexedTour) ?: PlannedTourAmounts.NONE
                val plan = cheatMap[day]?.get(indexedTour) ?: PlannedTourAmounts.NONE
//                val plan = plannedAmounts[indexedTour] ?: PlannedTourAmounts.NONE
                val (precursors, successors) = activityTypeGeneration.generateSecondaryActivityTypes(
                    SecondaryActInput(
                        day,
                        indexedTour,
                        plan
                    )
                )
                originalTour.loadPrecursors(precursors)
                originalTour.loadSuccessors(successors)
            }
        }
    }
}