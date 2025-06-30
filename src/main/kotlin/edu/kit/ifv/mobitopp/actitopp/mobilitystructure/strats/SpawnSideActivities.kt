package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.ByTracker
import edu.kit.ifv.mobitopp.actitopp.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.SecondaryActInput
import edu.kit.ifv.mobitopp.actitopp.modernization.TrackedSecondaryActivities

fun interface SpawnSideActivities {
    fun spawnSideActivities(input: MobilityStructure)
}

class StandardImplementation(val rng: RNGHelper) : SpawnSideActivities {

    override fun spawnSideActivities(input: MobilityStructure) {
        val activityTypeGeneration = TrackedSecondaryActivities(input, rng)


        input.elements().forEach { day ->
            val plannedAmounts = ByTracker(day, rng).determine(input)
            day.trackedElements().forEach { indexedTour ->
                val originalTour = indexedTour.element

                val plan = plannedAmounts[indexedTour] ?: throw NoSuchElementException("There should be calculated tour amounts for $indexedTour")
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