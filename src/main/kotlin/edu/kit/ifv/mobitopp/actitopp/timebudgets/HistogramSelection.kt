package edu.kit.ifv.mobitopp.actitopp.timebudgets

import edu.kit.ifv.mobitopp.actitopp.IPerson

interface HistogramSelection {

    fun select(randomNumber: Double, finalizedActivityPattern: FinalizedActivityPattern, person: IPerson): ArrayHistogram
}