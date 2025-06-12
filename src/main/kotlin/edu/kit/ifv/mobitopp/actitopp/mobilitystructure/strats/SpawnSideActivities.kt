package edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.modernization.ExampleAssign
import edu.kit.ifv.mobitopp.actitopp.modernization.MobilityStructure
import edu.kit.ifv.mobitopp.actitopp.modernization.PlannedTourAmounts
import edu.kit.ifv.mobitopp.actitopp.modernization.SecondaryActInput
import edu.kit.ifv.mobitopp.actitopp.modernization.Step5Generator

fun interface SpawnSideActivities {
    fun spawnSideActivities(input: MobilityStructure)
}

class StandardImplementation(val rng: RNGHelper): SpawnSideActivities {

    override fun spawnSideActivities(input: MobilityStructure) {
        val amountGeneration = Step5Generator(input, rng)
        val amounts = amountGeneration.calculate()
        val activityTypeGeneration = ExampleAssign(input, rng)
        input.elements().forEach { day ->
            day.trackedElements().forEach { indexedTour ->
                val originalTour = indexedTour.element
                val plan = amounts[day]?.get(indexedTour)?: PlannedTourAmounts.NONE
                val (precursors, successors) = activityTypeGeneration.generateSecondaryActivityTypes(SecondaryActInput(day, indexedTour, plan))
                originalTour.loadPrecursors(precursors)
                originalTour.loadSuccessors(successors)
            }
        }
    }
}