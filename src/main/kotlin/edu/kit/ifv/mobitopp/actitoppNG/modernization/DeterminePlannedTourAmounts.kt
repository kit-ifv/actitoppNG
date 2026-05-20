package edu.kit.ifv.mobitopp.actitoppNG.modernization

import edu.kit.ifv.mobitopp.actitoppNG.AllChoiceModels
import edu.kit.ifv.mobitopp.actitoppNG.PlanGenerationParameters
import edu.kit.ifv.mobitopp.actitoppNG.mobilitystructure.strats.subsubTourAmount.DayAmountTracker
import edu.kit.ifv.mobitopp.actitoppNG.utils.BidirectionalIndexedValue
import kotlin.random.Random

fun interface DetermineTourAmounts {
    context(rng: Random, models: AllChoiceModels)
    fun determine(input: MobilityStructure): Map<BidirectionalIndexedValue<TourStructure>, PlannedTourAmounts>
}

class ByTracker(val day: DayStructure) : DetermineTourAmounts {
    context(rng: Random, models: AllChoiceModels)
    override fun determine(input: MobilityStructure): Map<BidirectionalIndexedValue<TourStructure>, PlannedTourAmounts> {
        val tracker = DayAmountTracker(models, day,  input.weekRoutine)
        val predecessors = tracker.generatePredecessors(day.indexedElements())
        val successors = tracker.generateSuccessors(day.indexedElements())

        return predecessors.entries.associate { (k, v) -> k to PlannedTourAmounts(v, successors[k]!!) }

    }
}


