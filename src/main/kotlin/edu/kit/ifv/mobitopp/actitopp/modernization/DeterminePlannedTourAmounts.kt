package edu.kit.ifv.mobitopp.actitopp.modernization

import edu.kit.ifv.mobitopp.actitopp.RNGHelper
import edu.kit.ifv.mobitopp.actitopp.mobilitystructure.strats.subsubTourAmount.DayAmountTracker
import edu.kit.ifv.mobitopp.actitopp.utils.BidirectionalIndexedValue

fun interface DetermineTourAmounts {
    fun determine(input: MobilityStructure): Map<BidirectionalIndexedValue<TourStructure>, PlannedTourAmounts>
}

class ByTracker(val day: DayStructure, val rng: RNGHelper) : DetermineTourAmounts {

    override fun determine(input: MobilityStructure): Map<BidirectionalIndexedValue<TourStructure>, PlannedTourAmounts> {
        val tracker = DayAmountTracker(day, rng, input.weekRoutine)
        val predecessors = tracker.generatePredecessors(day.indexedElements())
        val successors = tracker.generateSuccessors(day.indexedElements())

        return predecessors.entries.associate { (k, v) -> k to PlannedTourAmounts(v, successors[k]!!) }

    }
}


